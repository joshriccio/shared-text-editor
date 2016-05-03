package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.EditableDocument;
import model.User;
import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;
import network.Server;

/**
 * This is the panel that the revision history will generate in. @author Joshua
 * Riccio
 *
 */
public class RevisionList extends JPanel {

	private static final long serialVersionUID = 1651418921573666127L;
	private DefaultListModel<String> listmodel;
	private JList<String> list;
	private JScrollPane scrollpane;
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private User user;
	private JPopupMenu menu;
	private TabbedPane tabs;
	private JButton refreshButton;

	/**
	 * The constructor for the revision list @param user the client user @param
	 * tabs the tabs in the editor gui
	 */
	public RevisionList(User user, TabbedPane tabs) {
		this.user = user;
		this.tabs = tabs;
		this.listmodel = new DefaultListModel<String>();
		this.list = new JList<String>(listmodel);
		this.menu = new JPopupMenu();
		JMenuItem editorItem = new JMenuItem("Load Document");
		menu.add(editorItem);
		JMenuItem messageItem = new JMenuItem("Delete Document");
		menu.add(messageItem);

		this.scrollpane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.scrollpane.setPreferredSize(new Dimension(200, 1000));

		refreshButton = new JButton("Refresh");
		setListeners();
		setLayout(new BorderLayout());
		this.add(scrollpane);
		this.add(refreshButton, BorderLayout.NORTH);
		connect();
		makeRequest();
	}

	/**
	 * Assign listeners to various fields
	 */
	private void setListeners() {
		refreshButton.addActionListener(new refreshButtonListener());

		this.list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2 && list.getSelectedValue() != null && !list.getSelectedValue().equals("") && tabs.getSelectedIndex() != -1) {
					launchDocument(tabs.getTitleAt(tabs.getSelectedIndex()), list.getSelectedValue());
				}
			}
		});

		tabs.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (!tabs.getTitleAt(tabs.getSelectedIndex()).equals("Chat")) {
					listmodel.clear();
					makeRequest();
				}
			}

		});
	}

	/**
	 * Launches the document in the revision history list
	 * @param documentname
	 * 			The name of the document to open
	 * @param summary
	 * 			The summary of the document changes
	 */
	private void launchDocument(String documentname, String summary) {
		try {
			Request requestDocument = new Request(RequestCode.REQUEST_DOCUMENT);
			requestDocument.setRequestedName(documentname);
			requestDocument.setSummary(summary);
			oos.writeObject(requestDocument);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Establish connection with server
	 */
	private void connect() {
		try {
			socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			ServerListener serverlistener = new ServerListener();
			serverlistener.start();
			Request request = new Request(RequestCode.START_DOCUMENT_STREAM);
			request.setUsername(user.getUsername());
			oos.writeObject(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Request the revision history from the server
	 */
	private void makeRequest() {
		Request request = new Request(RequestCode.GET_REVISION_HISTORY);
		request.setDocumentName(tabs.getTitleAt(tabs.getSelectedIndex()));
		try {
			oos.writeObject(request);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * A private class for handling responses from the server
	 * @author Stevo
	 *
	 */
	private class ServerListener extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Response response = (Response) ois.readObject();
					if (response.getResponseID() == ResponseCode.DOCUMENT_LISTS_SENT) {
						updateDocumentList(response);
					} else if (response.getResponseID() == ResponseCode.DOCUMENT_SENT) {
						openDocumentInTab(response.getEditableDocument());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Open the selected document revision in a new tab
		 * @param doc
		 * 			Document to open in a new tab
		 */
		private void openDocumentInTab(EditableDocument doc) {
			tabs.getCurrentTextPane().setStyledDocument(doc.getDocument());
		}

		/**
		 * Update the list of document editors
		 * @param response
		 * 			response must contain the list of users who can edit document
		 */
		private void updateDocumentList(Response response) {
			for (String doc : response.getEditorList()) {
				if (!RevisionList.this.listmodel.contains(doc))
					RevisionList.this.listmodel.addElement(doc);
			}
		}
	}

	/**
	 * Update the list of revisions upon button click
	 * @author Stevo
	 *
	 */
	private class refreshButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!tabs.getTitleAt(tabs.getSelectedIndex()).equals("Chat")) {
				listmodel.clear();
				makeRequest();
			}
		}
	}
}
