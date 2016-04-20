package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.swing.*;
import model.EditableDocument;
import model.User;
import network.Request;
import network.RequestCode;
import network.ResponseCode;
import network.Server;
import network.Response;

public class SubGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3977650348030935566L;

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
		User user = new User("Stephen", "boss");
		new SubGUI(user);
	}

	// Pass-through variables
	User user;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	// Variables for creating new documents
	private JPanel bottomPanel = new JPanel();
	private JButton newDocumentButton = new JButton("Create New Document");

	private JButton loadDocumentButton = new JButton("Load Document"); // FIXME: For testing, Phase 2
	
	private Socket socket = null;

	private JTabbedPane openDocumentSelectorPane = new JTabbedPane();
	
	public SubGUI(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, User user) {
		this.oos = objectOutputStream;
		this.ois = objectInputStream;
		this.user = user;

		organizeLayout();
		assignListeners();
	}

	public SubGUI(User user) {
		this.user = user;
//		openConnection(RequestCode.LOGIN);

		organizeLayout();
		assignListeners();
		this.setVisible(true);
	}

	private void organizeLayout() {

		this.setTitle("SubGUI (Welcome? Preferences?)");
		this.setSize(400, 450);
		
		//String arrays of the names of Documents to be added to a JScrollPane
		//String ownedDocNames[] = getNames(user.getOwnedDocuments());
		//String editableDocNames[] = getNames(user.getEditableDocuments());
		
		// Add tabbedPane
		openDocumentSelectorPane.addTab("Owned By You", new JScrollPane());
		openDocumentSelectorPane.addTab("Editable By You", new JScrollPane());
		this.add(openDocumentSelectorPane);

		bottomPanel.add(loadDocumentButton); // FIXME: Save from SubGUI - For
												// testing

		// Add newDocButton
		bottomPanel.add(newDocumentButton);
		this.add(bottomPanel, BorderLayout.SOUTH);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

	}

//Method that grabs names of documents and adds it to an 
//array of strings to be added to the JScrollPane()

//	private String[] getNames(List<EditableDocument> list) {
//		
//		int size = list.size();
//		String names[] = new String[size];
//		for(int i = 0; i<size; i++){
//			names[i] = list.get(i).getName();
//		}
//		
//		return names;
//	}

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

	}

	// Listener for testing saving and loading files
	private class LoadButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String documentName = JOptionPane.showInputDialog("What is the document named?");
			Request requestDocument = new Request(RequestCode.REQUEST_DOCUMENT);
			requestDocument.setRequestedName(documentName);
			requestDocument.setUser(user);
			
			try {
				oos.writeObject(requestDocument);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Response serverRequest = (Response) ois.readObject();
				EditableDocument openedDocument = serverRequest.getEditableDocument();
				EditorGui editor = new EditorGui(oos, ois, user, openedDocument);
				editor.setVisible(true);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

//			try {
//				FileInputStream inFile = new FileInputStream("UpdatedSaveFile");
//				ObjectInputStream inputStream = new ObjectInputStream(inFile);
//				EditableDocument document = (EditableDocument) inputStream.readObject();
//				inputStream.close();
//				EditorGui editor = new EditorGui(oos, ois, user, document);
//				editor.setVisible(true);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
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
//
//	private void openConnection(RequestCode requestCode) {
//		try {
//			// Connect to the Server
//			socket = new Socket("localhost", Server.PORT_NUMBER);
//			this.oos = new ObjectOutputStream(socket.getOutputStream());
//			this.ois = new ObjectInputStream(socket.getInputStream());
//			Request clientRequest = new Request(requestCode);
//			clientRequest.setUser(user);
//			oos.writeObject(clientRequest);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	
}