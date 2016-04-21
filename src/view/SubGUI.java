package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;
import model.User;
import network.Request;
import network.RequestCode;
import network.Server;
import network.Response;

public class SubGUI extends JFrame {

	private User user;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private JPanel bottomPanel = new JPanel();
	private JButton newDocumentButton = new JButton("Create New Document");
	private JButton loadDocumentButton = new JButton("Refresh Document List"); 
	private Socket socket = null;
	private JTabbedPane openDocumentSelectorPane = new JTabbedPane();
	private ListPane editorList = new ListPane();
	private ListPane ownerList = new ListPane();

	public SubGUI(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, User user) {
		this.oos = objectOutputStream;
		this.ois = objectInputStream;
		this.user = user;
		organizeLayout();
		assignListeners();
	}

	public SubGUI(User user) {
		this.user = user;
		organizeLayout();
		assignListeners();
		this.setVisible(true);
	}
	
	private void loadDocuments(){
		ObjectOutputStream documentOutput = null;
		ObjectInputStream documentInput = null;
		try {
			Request r = new Request(RequestCode.START_DOCUMENT_STREAM);
			socket = new Socket("localhost", Server.PORT_NUMBER);
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
			Response response = (Response)documentInput.readObject();
			editorList.updateDocumentList(response.getEditorList());
			ownerList.updateDocumentList(response.getOwnerList());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void organizeLayout() {
		this.setTitle("Welcome");
		this.setSize(400, 450);
		// Add tabbedPane
		openDocumentSelectorPane.addTab("Owned By You", ownerList);
		openDocumentSelectorPane.addTab("Editable By You", editorList);
		//editorList.addDemoDocuments();
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

}