package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

/**
 * This is class launches the chat application for 
 * global and private chats
 * 
 * @author Josh Riccio
 * @author Cody Deeran
 *
 */

public class ChatTab extends JPanel {

	private static final long serialVersionUID = 1L;
	ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ChatMessages messages;
	private ChatTextArea chatArea;
	private JTextPane chatpane;
	private String conversation;
	private ArrayList<PrivateChatWindow> privateChatList;
	 static String name;
	private Socket socket;
	boolean newMessage = false;

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
			ChatTab.name = username;
			request.setUsername(ChatTab.name);
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
		this.add(messages, BorderLayout.CENTER);
		this.add(chatArea, BorderLayout.SOUTH);
		setListeners();
		ChatServerListener csl = new ChatServerListener();
		csl.start();

	}

	/**
	 * Starts up the private chat windows for communications between two users
	 * @param sendersUsername
	 * 				The user who started the chat
	 * @param receiversUsername
	 * 				The user who the one who started the chat wants to chat with
	 */
	public void sendPrivateMessage(String sendersUsername, String receiversUsername) {

		PrivateChatWindow pcw = new PrivateChatWindow(receiversUsername, oos);
		privateChatList.add(pcw);
		pcw.setVisible(true);
	}

	/**
	 * Initializes the listener for enter key -- send message
	 */
	private void setListeners() {
		this.chatpane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = "";
					if (chatArea.getMessage().length() > 1) {
						message = chatArea.getMessage().substring(0, chatArea.getMessage().length() - 1);
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
		});
	}

	/**
	 * Updates the global chat
	 * @param sendersUsername
	 * 			the username of the sender to be appended with the message
	 * @param message
	 * 			the message the sender whats to send
	 */
	public void updateConversation(String sendersUsername, String message) {
	        String notificationMessage = sendersUsername + ": " + message;
	        DesktopNotification desktopNotification = new DesktopNotification(notificationMessage);
	        desktopNotification.setVisible(true);
		conversation = conversation + sendersUsername + ": " + message + "\n";
		messages.setText(conversation);
		newMessage = true;
	}
	
	/**
	 * Updates the private chat window
	 * @param sendersUsername
	 * 			The username of the sender to be appended with the message
	 * @param message
	 * 			the message the sender wants to write
	 */
	private void updatePrivateConversation(String sendersUsername, String message) {
	        System.out.println("Sender: " + sendersUsername);
		boolean windowExist = false;
		for(PrivateChatWindow pcw : privateChatList){
			if(pcw.getPrivateChatUsername().equals(sendersUsername)){
				pcw.updatePrivateConversation(sendersUsername + ": " + message + "\n");
				windowExist = true;
				pcw.setVisible(true);
			}
		}
		
		if(!windowExist){
			PrivateChatWindow pcw = new PrivateChatWindow(sendersUsername, oos);
			privateChatList.add(pcw);
			pcw.updatePrivateConversation(pcw.getPrivateChatUsername() + ": " + message + "\n");
			pcw.setVisible(true);
		}
	}
	
	
	
	/**
	 * Get the messages in the conversation
	 * @return	the messages in the conversation
	 */
	public ChatMessages getMessageWindow(){
		return this.messages;
	}

	/**
	 * A thread that ensures that the ChatTab is kept up to date
	 * 
	 * @author Stevo
	 *
	 */
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
	private String username;
	private String privateConversation = "";
	private ObjectOutputStream oos;

	/**
	 * The chat window's constructor
	 * 
	 * @param username
	 * 			the person who the user would like to chat with
	 */
	public PrivateChatWindow(String username, ObjectOutputStream oos) {
		this.username = username;
		this.setTitle("Private Chatting: " + this.username);
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.messageWindow = new ChatMessages();
		this.messageWindow.getTextPane().setPreferredSize(new Dimension(560, 250));
		this.textpane = new JTextPane();
		setListeners();
		this.textarea = new ChatTextArea(textpane);
		this.add(messageWindow, BorderLayout.CENTER);
		this.add(textarea, BorderLayout.SOUTH);
		this.oos = oos;
	}

	/**
	 * Initializes the listener for enter key -- send message
	 */
	private void setListeners() {
		this.textpane.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					String message = "";
					if (textarea.getMessage().length() > 1) {
						message = textarea.getMessage().substring(0, textarea.getMessage().length() - 1);
					}
					try {
						Request request = new Request(RequestCode.SEND_PRIVATE_MESSAGE);
						request.setUsername(username);
						request.setMessage(message);
						privateConversation = privateConversation + ChatTab.name + ": " + message + "\n";
						messageWindow.setText(privateConversation);
						textarea.clearText();
						oos.writeObject(request);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});
	}
	
	/**
	 * Updates the private conversation between users
	 * @param conversation
	 * 		The message to be appended to the overall conversation on screen
	 */
	public void updatePrivateConversation(String conversation){
		this.privateConversation = this.privateConversation + conversation;
		this.messageWindow.setText(this.privateConversation);
	}
	
	/**
	 * Get the messageWindow of the user who is to receive the message
	 * @return
	 *  the messageWindow of the user who is to receive the message
	 * 	
	 */
	public ChatMessages getMessageWindow(){
		return this.messageWindow;
	}
	/**
	 * Get the overall conversation of the private message between users
	 * @return
	 * 		the overall conversation of the private message between users
	 */
	public String getPrivateConversation(){
		return this.privateConversation;
	}
	/**
	 * Get the username of the user to receive message
	 * @return
	 * 		the username of the user to receive message
	 */
	public String getPrivateChatUsername(){
		return this.username;
	}
}