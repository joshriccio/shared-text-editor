package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;
import network.Server;

public class ChatTab extends JPanel {

	private static final long serialVersionUID = 1L;
	static ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ChatMessages messages;
	private ChatTextArea chatArea;
	private JTextPane chatpane;
	private String conversation;
	private ArrayList<PrivateChatWindow> privateChatList;
	static String name;
	private Socket socket;

	/**
	 * The constructor to build a new chat tab @param username the clients
	 * username
	 */
	public ChatTab(String username) {
		try {
			this.socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
			oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
			Request request = new Request(RequestCode.START_CHAT_HANDLER);
			this.name = username;
			request.setUsername(this.name);
			oos.writeObject(request);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.privateChatList = new ArrayList<PrivateChatWindow>();
		this.messages = new ChatMessages();
		this.conversation = new String();
		this.chatpane = new JTextPane();
		this.chatArea = new ChatTextArea(chatpane);
		this.chatArea.setPreferredSize(new Dimension(1000, 350));
		this.add(messages, BorderLayout.CENTER);
		this.add(chatArea, BorderLayout.SOUTH);
		setListeners();
		ChatServerListener csl = new ChatServerListener();
		csl.start();

	}

	public void sendPrivateMessage(String sendersUsername, String receiversUsername) {
		PrivateChatWindow pcw = new PrivateChatWindow(receiversUsername);
		privateChatList.add(pcw);
		pcw.setVisible(true);
	}

	private void setListeners() {
		this.chatpane.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = "";
					if (chatArea.getMessage().length() > 1) {
						message = chatArea.getMessage().substring(0, chatArea.getMessage().length() - 2);
					}
					try {
						Request request = new Request(RequestCode.SEND_MESSAGE);
						request.setUsername(name);
						request.setMessage(message);
						chatArea.clearText();
						oos.writeObject(request);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent event) {
			}
		});
	}

	/**
	 * Updates the conversation with the latest message @param name the message
	 * senders name @param message the message that was sent
	 */
	public void updateConversation(String sendersUsername, String message) {
		conversation = conversation + sendersUsername + ": " + message + "\n";
		messages.setText(conversation);
	}
	
	private void updatePrivateConversation(String sendersUsername, String message) {
		boolean windowExist = false;
		for(PrivateChatWindow pcw : privateChatList){
			if(pcw.getPrivateChatUsername().equals(sendersUsername)){
				pcw.updatePrivateConversation(pcw.getPrivateChatUsername() + ": " + message + "\n");
				windowExist = true;
				pcw.setVisible(true);
			}
		}
		
		if(!windowExist){
			PrivateChatWindow pcw = new PrivateChatWindow(sendersUsername);
			privateChatList.add(pcw);
			pcw.updatePrivateConversation(pcw.getPrivateChatUsername() + ": " + message + "\n");
			pcw.setVisible(true);
		}
	}

	private class ChatServerListener extends Thread {
		@Override
		public void run() {
			boolean isRunning = true;
			while (isRunning) {
				try {
					Response response = ((Response) ois.readObject());
					if (response.getResponseID() == ResponseCode.NEW_MESSAGE) {
						updateConversation(response.getUsername(), response.getMessage());
					} else if (response.getResponseID() == ResponseCode.NEW_PRIVATE_MESSAGE) {
						updatePrivateConversation(response.getUsername(), response.getMessage());
					}
				} catch (IOException | ClassNotFoundException e) {
					isRunning = false;
					e.printStackTrace();
				}
			}
		}
	}
}

/**
 * The main user interface for the chat client
 * 
 * @author Joshua Riccio
 *
 */
class PrivateChatWindow extends JFrame {
	private static final long serialVersionUID = 5875046651800072284L;
	private ChatMessages messageWindow;
	private ChatTextArea textarea;
	private JTextPane textpane;
	private String name;
	private String privateConversation = "";

	/**
	 * The chat window's constructor
	 * 
	 * @param username
	 * @param conversation
	 * @param messageWindow
	 */
	public PrivateChatWindow(String username) {
		this.name = username;
		this.setTitle("Private Chatting" + name);
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.messageWindow = new ChatMessages();
		this.messageWindow.gettextpane().setPreferredSize(new Dimension(560, 250));
		this.textpane = new JTextPane();
		setListeners();
		this.textarea = new ChatTextArea(textpane);
		this.add(messageWindow, BorderLayout.CENTER);
		this.add(textarea, BorderLayout.SOUTH);
	}

	private void setListeners() {
		this.textpane.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent event) {
			}

			@Override
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = "";
					if (textarea.getMessage().length() > 1) {
						message = textarea.getMessage().substring(0, textarea.getMessage().length() - 1);
					}
					try {
						Request request = new Request(RequestCode.SEND_PRIVATE_MESSAGE);
						request.setUsername(name);
						request.setMessage(message);
						privateConversation = privateConversation + ChatTab.name + ": " + message + "\n";
						messageWindow.setText(privateConversation);
						textarea.clearText();
						ChatTab.oos.writeObject(request);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent event) {
			}
		});
	}
	
	public void updatePrivateConversation(String conversation){
		this.privateConversation = this.privateConversation + conversation;
		this.messageWindow.setText(this.privateConversation);
	}
	
	public ChatMessages getMessageWindow(){
		return this.messageWindow;
	}
	public String getPrivateConversation(){
		return this.privateConversation;
	}
	public String getPrivateChatUsername(){
		return this.name;
	}
}