package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;

import model.EditableDocument;
import model.User;
import network.Request;
import network.RequestCode;
import network.Server;
import network.Response;

/**
 * The intermediary gui between the main gui and the login screen
 * 
 * @author Stephen Connolly
 *
 */
public class SubGUI extends JFrame {

	private static final long serialVersionUID = -7332966050995052441L;
	private User user;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private JPanel bottomPanel = new JPanel();
	private JButton newDocumentButton = new JButton("Create New Document");
	private JButton loadDocumentButton = new JButton("Refresh Document List");
	private Socket socket = null;
	private JTabbedPane openDocumentSelectorPane = new JTabbedPane();

	private JList<String> editorlist = new JList<String>();
	private JList<String> ownerlist = new JList<String>();
	private DefaultListModel<String> olistmodel = new DefaultListModel<String>();
	private DefaultListModel<String> elistmodel = new DefaultListModel<String>();

	/**
	 * The constructor when Streams are also being sent into the gui
	 * 
	 * @param objectOutputStream
	 *            The current stream that has been authenticated
	 * @param objectInputStream
	 *            The current stream that has been authenticated
	 * @param user
	 *            The user that has been authenticated
	 */
	public SubGUI(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, User user) {
		this.oos = objectOutputStream;
		this.ois = objectInputStream;
		this.user = user;
		organizeLayout();
		assignListeners();
	}

	/**
	 * A constructor for testing
	 * 
	 * @param user
	 *            the authenticated user
	 */
	public SubGUI(User user) {
		this.user = user;
		organizeLayout();
		assignListeners();
		this.setVisible(true);
	}

	private void loadDocuments() {
		ObjectOutputStream documentOutput = null;
		ObjectInputStream documentInput = null;
		try {
			Request r = new Request(RequestCode.START_DOCUMENT_STREAM);
			socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
			documentOutput = new ObjectOutputStream(socket.getOutputStream());
			documentInput = new ObjectInputStream(socket.getInputStream());
			documentOutput.writeObject(r);
		} catch (IOException e1) {
			System.out.println("Couldn't start stream");
			e1.printStackTrace();
		}

		Request request = new Request(RequestCode.REQUEST_DOCUMENT_LIST);
		request.setUsername(user.getUsername());

		try {
			documentOutput.writeObject(request);
			Response response = (Response) documentInput.readObject();
			updateDocumentList(response.getEditorList(), elistmodel);
			updateDocumentList(response.getOwnerList(), olistmodel);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void organizeLayout() {
		this.setTitle("Welcome");
		this.setSize(400, 450);
		// Add tabbedPane
		JScrollPane escrollpane;
		elistmodel = new DefaultListModel<String>();
		editorlist = new JList<String>(elistmodel);
		escrollpane = new JScrollPane(editorlist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		escrollpane.setPreferredSize(new Dimension(120, 100));
		setLayout(new BorderLayout());

		JScrollPane oscrollpane;
		ownerlist = new JList<String>(this.olistmodel);
		oscrollpane = new JScrollPane(ownerlist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		oscrollpane.setPreferredSize(new Dimension(120, 100));
		setLayout(new BorderLayout());

		openDocumentSelectorPane.addTab("Owned By You", oscrollpane);
		openDocumentSelectorPane.addTab("Editable By You", escrollpane);

		// editorList.addDemoDocuments();
		this.add(openDocumentSelectorPane);
		bottomPanel.add(loadDocumentButton);
		// Add newDocButton
		bottomPanel.add(newDocumentButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	private void assignListeners() {
		loadDocumentButton.addActionListener(new LoadButtonListener());
		// FIXME: ADD list listeners
		newDocumentButton.addActionListener(new CreateNewDocumentListener());

		ownerlist.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					System.out.println("double clicked on " + ownerlist.getSelectedValue());
					launchDocument(ownerlist.getSelectedValue());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});

		editorlist.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					System.out.println("double clicked on " + editorlist.getSelectedValue());
					launchDocument(editorlist.getSelectedValue());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
	}

	// Listener for testing saving and loading files
	private class LoadButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			loadDocuments();
		}
	}

	private class CreateNewDocumentListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String newDocumentName = JOptionPane.showInputDialog("What would you like to name your new document?");
			EditorGui editor = new EditorGui(oos, ois, user, newDocumentName);
			editor.setVisible(true);
			dispose();
		}
	}

	/**
	 * Updates the document list with all currently available documents
	 * 
	 * @param documentList
	 *            The document list
	 * @param listmodel
	 *            the default list model
	 */
	public void updateDocumentList(String[] documentList, DefaultListModel<String> listmodel) {
		for (int i = 0; i < documentList.length; i++) {
			if (documentList[i].substring(0, 1).equals("-")) {
				if (listmodel.contains(documentList[i].substring(1, documentList[i].length()))) {
					listmodel.removeElement(documentList[i].substring(1, documentList[i].length()));
				}
			} else if (!listmodel.contains(documentList[i])) {
				listmodel.addElement(documentList[i]);
			}
		}
	}

	private void launchDocument(String documentName) {
		Request requestDocument = new Request(RequestCode.REQUEST_DOCUMENT);
		requestDocument.setRequestedName(documentName);
		requestDocument.setUser(user);

		try {
			oos.writeObject(requestDocument);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Response serverRequest = (Response) ois.readObject();
			EditableDocument openedDocument = serverRequest.getEditableDocument();
			EditorGui editor = new EditorGui(oos, ois, user, openedDocument);
			editor.setVisible(true);
			dispose();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}