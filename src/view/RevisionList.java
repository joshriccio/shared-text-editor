package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
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
	
	private void setListeners() {
		refreshButton.addActionListener(new refreshButtonListener());
	
		this.list.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() == 2) {
					launchDocument(tabs.getTitleAt(tabs.getSelectedIndex()), list.getSelectedValue());
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
		
		tabs.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(!tabs.getTitleAt(tabs.getSelectedIndex()).equals("Chat")){
					listmodel.clear();
					makeRequest();
				}
			}
			
		});
	}
	
	private void launchDocument(String documentname, String summary) {
		try {
			Request requestDocument = new Request(RequestCode.REQUEST_DOCUMENT, null, null);
			requestDocument.setRequestedName(documentname);
			requestDocument.setSummary(summary);
			oos.writeObject(requestDocument);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void connect() {
        try {
			socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
	        this.oos = new ObjectOutputStream(socket.getOutputStream());
	        this.ois = new ObjectInputStream(socket.getInputStream());
	        ServerListener serverlistener = new ServerListener();
	        serverlistener.start();
	        Request request = new Request(RequestCode.START_DOCUMENT_STREAM, null, null);
	        request.setUsername(user.getUsername());
	        oos.writeObject(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makeRequest() {
		Request request = new Request(RequestCode.GET_REVISION_HISTORY, null, null);
		request.setDocumentName(tabs.getTitleAt(tabs.getSelectedIndex()));
		try {
			oos.writeObject(request);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private class ServerListener extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Response response = (Response) ois.readObject();
					if(response.getResponseID() == ResponseCode.DOCUMENT_LISTS_SENT){
						updateDocumentList(response);
					}else if(response.getResponseID() == ResponseCode.DOCUMENT_SENT){
						openDocumentInTab(response.getEditableDocument());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void openDocumentInTab(EditableDocument doc) {
			tabs.getCurrentTextPane().setStyledDocument(doc.getDocument());
		}

		private void updateDocumentList(Response response) {
			for(String doc : response.getEditorList()){
				if(!RevisionList.this.listmodel.contains(doc))
					RevisionList.this.listmodel.addElement(doc);
			}
		}
	}

	private class refreshButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!tabs.getTitleAt(tabs.getSelectedIndex()).equals("Chat")){
				listmodel.clear();
				makeRequest();
			}
		}
		
	}
}
