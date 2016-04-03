package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import model.User;

/**
 * This class is designed to manage user interactions across a network.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu)
 *
 */
public class Server {
	
	public static int PORT_NUMBER = 4000;
	private static ServerSocket serverSocket;
	private static Map<ObjectOutputStream, User> networkAccounts = Collections.synchronizedMap(new HashMap<ObjectOutputStream, User>());
	/**
	 * Read data from client server which will contain canvas drawings. And
	 * displays the drawings for each user to see.
	 * 
	 * @param args
	 *            Never used
	 */
	public static void main(String[] args) {
		Socket socket = null;
		serverSocket = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try{
			serverSocket = new ServerSocket(PORT_NUMBER);
			while(true){
				socket = serverSocket.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public static boolean authenticate(String username, String password) {
		if (networkAccounts.get(username) != null && networkAccounts.get(username).getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public static Map<ObjectOutputStream, User>  getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

}
