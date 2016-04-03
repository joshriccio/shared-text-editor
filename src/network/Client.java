package network;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.User;

/**
 * This is just a test class so I can understand and experiment with the user
 * authentication to the server.
 * 
 * @author codydeeran
 */
public class Client extends JFrame {
	private JPanel loginPanel;
	private JButton loginButton = new JButton("Login");
	private JButton createAccountButton = new JButton("Create Account");
	private JButton forgottenAccountButton = new JButton("Forgot Login");
	private JTextField loginTextField = new JTextField(15);
	private JPasswordField passwordField = new JPasswordField(15);
	private JLabel username;
	private JLabel password;
	private boolean loginButtonState = true;
	private static final String ADDRESS = "localhost";
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	public Client() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,150);
		setLocation(300,20);
		setLayout(new FlowLayout());
		username = new JLabel("Username");
		password = new JLabel("Password");
		loginPanel = new JPanel();
		loginPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// Login Constraints
		c.gridx = 0;
		c.gridy = 0;
		loginPanel.add(username,c);
		c.gridy = 1;
		loginPanel.add(password,c);
		c.gridx = 1;
		c.gridy = 0;
		loginPanel.add(loginTextField,c);
		c.gridx = 1;
		c.gridy = 1;
		loginPanel.add(passwordField,c);
		c.gridx = 0;
		c.gridy = 2;
		loginPanel.add(loginButton,c);
		c.gridx = 1;
		c.gridy = 2;
		loginPanel.add(createAccountButton,c);
		c.gridx = 2;
		c.gridy = 2;
		loginPanel.add(forgottenAccountButton,c);
		add(loginPanel);
		this.openConnection();
//		ServerListener serverListener = new ServerListener();
//		serverListener.start();
	}
	/**
	 * Opens a new connection between server and client
	 */
	private void openConnection() {
		try {
			// Connect to the Server
			socket = new Socket(ADDRESS, Server.PORT_NUMBER);
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			this.ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	loginButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		if (loginButtonState) {
			if (oos.writeObject(authenticate(loginTextField.getText(), String.valueOf(passwordField.getPassword())))) {
//				JOptionPane.showMessageDialog(null, "Welcome " + loginTextField.getText() + "!");
//				loginButtonState = false;
//				loginButton.setText("Sign Out");
//				loginTextField.setEditable(false);
//				passwordField.setEditable(false);
			} // end if
		}
	}
	
//	/**
//	 * The listener that waits for new text to be written to the server and/or authentication.
//	 * 
//	 * @author Josh Riccio
//	 * @author Cody Deeran
//	 *
//	 */
//	private class ServerListener extends Thread {
//		@SuppressWarnings("unchecked")
//		@Override
//		public void run() {
//			try {
//				
//			} catch (ClassNotFoundException | IOException e1) {
//				e1.printStackTrace();
//			}
//			while (true) {
//				try {
//					
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	public static void main(String[] args) {
		Client c = new Client();
		c.setVisible(true);
	}
}
