package network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import model.User;
/**
 * This class is designed to manage user interactions across a network.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu)
 */
public class Server {
	public static int PORT_NUMBER = 4000;
	private static ServerSocket serverSocket;
	private static Map<ObjectOutputStream,String> networkAccounts = Collections.synchronizedMap(new HashMap<ObjectOutputStream,String>());
	private static Map<String,String> users = Collections.synchronizedMap(new HashMap<String,String>());
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private static boolean serverAddressInUse = false;
	/**
	 * Read data from client server which will contain canvas drawings. And
	 * displays the drawings for each user to see.
	 * 
	 * @param args
	 *            Never used
	 */
	public static void main(String[] args) {
		users.put("Josh","123");
		socket = null;
		serverSocket = null;
		ois = null;
		oos = null;
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			serverAddressInUse = true;
			while (true) {
				if (Client.loginButtonState) {
					socket = serverSocket.accept();
					ois = new ObjectInputStream(socket.getInputStream());
					oos = new ObjectOutputStream(socket.getOutputStream());
					User user = (User) ois.readObject();
					if (authenticate(user)) {
						oos.writeObject("authenticate!");
						JOptionPane.showMessageDialog(null,"Welcome " + user.getUsername() + "!");
					}
					else {
						oos.writeObject("failed!");
						JOptionPane.showMessageDialog(null,"Username or Password is incorrect. Please try again.","Login Failed",JOptionPane.OK_OPTION);
					}
				}
				else if(Client.createAccountButtonState){
					checkNewUserAuthenication();
					break;
				}
			}
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkNewUserAuthenication() {
	//	if (serverAddressInUse == false) {
			try {
				serverSocket = new ServerSocket(PORT_NUMBER);
				while (true) {
					socket = serverSocket.accept();
					ois = new ObjectInputStream(socket.getInputStream());
					oos = new ObjectOutputStream(socket.getOutputStream());
					User newUser = (User) ois.readObject();
					if (authenticateNewUser(newUser)) {
						users.put(newUser.getUsername(),newUser.getPassword());
						oos.writeObject("New user SUCCESSFULLY created");
						JOptionPane.showMessageDialog(null,"New user was SUCCESSFULLY created!");
					}
					else {
						oos.writeObject("Attempt to create new user FAILED");
						JOptionPane.showMessageDialog(null,"The USERNAME you have choosen has already \n" + "been choosen. Please try again.","Account Creation Failed",JOptionPane.OK_OPTION);
					}
				}
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
//		}
//		else {
//			try {
//				ois = new ObjectInputStream(socket.getInputStream());
//				oos = new ObjectOutputStream(socket.getOutputStream());
//				User newUser = (User) ois.readObject();
//				if (authenticateNewUser(newUser)) {
//					users.put(newUser.getUsername(),newUser.getPassword());
//					oos.writeObject("New user SUCCESSFULLY created");
//					JOptionPane.showMessageDialog(null,"New user was SUCCESSFULLY created!");
//				}
//				else {
//					oos.writeObject("Attempt to create new user FAILED");
//					JOptionPane.showMessageDialog(null,"The USERNAME you have choosen has already \n" + "been choosen. Please try again.","Account Creation Failed",JOptionPane.OK_OPTION);
//				}
//			}
//			catch (IOException | ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
	}
	private static boolean authenticateNewUser(User user){
		if(users.containsKey(user.getUsername())){
			return false;
		}
		else{
			return true;
		}
	}
	
	private static boolean authenticate(User user) {
		if (users.containsKey(user.getUsername()) && users.get(user.getUsername()).equals(user.getPassword())) {
			return true;
		}
		return false;
	}
	public static Map<ObjectOutputStream,User> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}
}
