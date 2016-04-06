package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.text.StyledDocument;

import model.EditableDocument;
import model.User;


/**
 * This class is designed to manage user interactions across a network.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu)
 */
public class Server {
	public static int PORT_NUMBER = 4000;
	private static ServerSocket serverSocket;
	private static Map<ObjectOutputStream, String> networkAccounts = Collections
			.synchronizedMap(new HashMap<ObjectOutputStream, String>());
	private static Map<String, String> users = Collections.synchronizedMap(new HashMap<String, String>());
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;

	/**
	 * Read data from client server which will contain canvas drawings. And
	 * displays the drawings for each user to see.
	 * 
	 * @param args
	 *            Never used
	 */
	public static void main(String[] args) {
		users.put("Josh", "123");
		users.put("Cody", "456");
		socket = null;
		serverSocket = null;
		ois = null;
		oos = null;
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			// serverAddressInUse = true;
			while (true) {
				socket = serverSocket.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				Request r = (Request) ois.readObject();
				if(r.getRequestType() == 1){
					User user = r.getUser();
					if (authenticate(user)) {
						Response rsp = new Response(1);
						networkAccounts.put(oos, user.getUsername());
						oos.writeObject(rsp);
						ClientHandler c = new ClientHandler(ois, networkAccounts);
						c.start();
					} else {
						Response rsp = new Response(2);
						System.out.println(networkAccounts.get(oos));
						oos.writeObject(rsp);
					}
				}else if(r.getRequestType() == 2){
					User user = r.getUser();
					if (authenticateNewUser(user)) {
						users.put(user.getUsername(), user.getPassword());
						Response rsp = new Response(3);
						oos.writeObject(rsp);
					} else {
						Response rsp = new Response(4);
						oos.writeObject(rsp);
					}
				}

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static boolean authenticateNewUser(User user) {
		if (users.containsKey(user.getUsername())) {
			return false;
		} else {
			return true;
		}
	}

	private static boolean authenticate(User user) {
		if (users.containsKey(user.getUsername()) && users.get(user.getUsername()).equals(user.getPassword())) {
			return true;
		}
		return false;
	}

	public static Map<ObjectOutputStream, User> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * The new thread to manage client activity
 * 
 * @author Josh Riccio (jriccio@email.arizona.edu)
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 * 
 */
class ClientHandler extends Thread {
	private ObjectInputStream input;
	private Map<ObjectOutputStream, String> networkAccounts;
	private volatile boolean isRunning = true;

	public ClientHandler(ObjectInputStream input, Map<ObjectOutputStream, String> networkAccounts) {
		this.input = input;
		this.networkAccounts = networkAccounts;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				Request r = (Request)input.readObject();
				if(r.getRequestType() == 3){
					this.writeDocumentToClients(r.doc);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				this.cleanUp();
			}
		}
	}

	/**
	 * Safely ends the client thread
	 */
	private void cleanUp() {
		isRunning = false;
		System.out.println("Client has been disconnected");
	}
	
	/**
	 * Sends new shape to all connected clients
	 * @param shape the shape to write to clients
	 */
	private void writeDocumentToClients(EditableDocument doc) {
		synchronized (networkAccounts) {
			Set<ObjectOutputStream> closedClientsList = new HashSet<ObjectOutputStream>();
		    Iterator it = networkAccounts.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        ObjectOutputStream oos = (ObjectOutputStream)pair.getKey();
		        Response r = new Response(5, doc);
		        try {
					oos.writeObject(r);
				} catch (IOException e) {
					networkAccounts.remove((ObjectOutputStream)pair.getKey());
					e.printStackTrace();
				}
		        //NOT THREAD SAFE
		    }
		}
	}
}
