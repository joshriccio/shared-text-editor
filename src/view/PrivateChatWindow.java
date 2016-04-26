package view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import network.Request;
import network.RequestCode;
import network.Response;
import network.Server;

/**
 * The main user interface for the chat client
 * 
 * @author Joshua Riccio
 *
 */
public class PrivateChatWindow extends JFrame {
	private static final long serialVersionUID = 5875046651800072284L;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private ChatMessages messages;
	private ChatTextArea textarea;
	private JTextPane textpane;
	private String conversation;
	private String name;

	/**
	 * The chat window's constructor
	 * 
	 * @param username
	 * @param conversation
	 * @param messages
	 */
	public PrivateChatWindow(String initiaterUsername) {
		try {
			socket = new Socket(Server.ADDRESS, Server.PORT_NUMBER);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			Request request = new Request(RequestCode.START_PRIVATE_CHAT_HANDLER);
			this.name = initiaterUsername;
			request.setUsername(this.name);
			oos.writeObject(request);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle("Private Chatting");
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.messages = new ChatMessages();
		this.conversation = new String();
		this.textpane = new JTextPane();
		setListeners();
		this.textarea = new ChatTextArea(textpane);
		this.add(messages, BorderLayout.CENTER);
		this.add(textarea, BorderLayout.SOUTH);
		PrivateChatServerListener pcsl = new PrivateChatServerListener();
		pcsl.start();

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
						message = textarea.getMessage().substring(0, textarea.getMessage().length() - 2);
					}
					try {
						Request request = new Request(RequestCode.SEND_MESSAGE);
						request.setUsername(name);
						request.setMessage(message);
						textarea.clearText();
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

	public void updateConversation(String name, String message) {
		conversation = conversation + name + ": " + message + "\n";
		messages.setText(conversation);
	}

	private class PrivateChatServerListener extends Thread {
		@Override
		public void run() {
			boolean isRunning = true;
			while (isRunning) {
				try {
					Response response = ((Response) ois.readObject());
					updateConversation(response.getUsername(), response.getMessage());
				} catch (IOException | ClassNotFoundException e) {
					isRunning = false;
					e.printStackTrace();
				}
			}
		}
	}
}