package network;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import model.EditableDocument;
import model.User;

/**
 * The Server class acts as the communication portal between clients. The Server
 * receives requests and generates responses.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu)
 * @author Joshua Riccio
 */
public class Server {
	public static int PORT_NUMBER = 4001;
	private static ServerSocket serverSocket;
	private static Map<ObjectOutputStream, String> networkAccounts = Collections
			.synchronizedMap(new HashMap<ObjectOutputStream, String>());
	private static Map<String, String> users = Collections.synchronizedMap(new HashMap<String, String>());
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private static Request clientRequest;
	private static Response serverResponse;
	private static User user;

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
		users.put("Brittany", "789");
		users.put("Stephen", "boss");
		socket = null;
		serverSocket = null;
		ois = null;
		oos = null;
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			while (true) {
				socket = serverSocket.accept();
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				clientRequest = (Request) ois.readObject();
				if (clientRequest.getRequestType() == RequestCode.LOGIN) {
					user = clientRequest.getUser();
					if (authenticate(user)) {
						serverResponse = new Response(ResponseCode.LOGIN_SUCCESSFUL);
						networkAccounts.put(oos, user.getUsername());
						System.out.println(serverResponse.getResponseID());
						oos.writeObject(serverResponse);
						ClientHandler c = new ClientHandler(ois, networkAccounts);
						c.start();
					} else {
						serverResponse = new Response(ResponseCode.LOGIN_FAILED);
						System.out.println(networkAccounts.get(oos));
						oos.writeObject(serverResponse);
					}
				} else if (clientRequest.getRequestType() == RequestCode.CREATE_ACCOUNT) {
					user = clientRequest.getUser();
					if (authenticateNewUser(user)) {
						users.put(user.getUsername(), user.getPassword());
						serverResponse = new Response(ResponseCode.ACCOUNT_CREATED_SUCCESSFULLY);
						oos.writeObject(serverResponse);
					} else {
						serverResponse = new Response(ResponseCode.ACCOUNT_CREATION_FAILED);
						oos.writeObject(serverResponse);
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

	/**
	 * @return returns the map
	 */
	public static Map<ObjectOutputStream, User> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}
}

/**
 * ClientHandler gerates a new thread to manage client activity
 * 
 * @author Josh Riccio (jriccio@email.arizona.edu)
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 * 
 */
class ClientHandler extends Thread {
	private ObjectInputStream input;
	private Map<ObjectOutputStream, String> networkAccounts;
	private volatile boolean isRunning = true;
	private Request clientRequest;
	private Response serverResponse;

	/**
	 * Constructor
	 * 
	 * @param input
	 *            the object input stream
	 * @param networkAccounts
	 *            the list of uses connected
	 */
	public ClientHandler(ObjectInputStream input, Map<ObjectOutputStream, String> networkAccounts) {
		this.input = input;
		this.networkAccounts = networkAccounts;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				clientRequest = (Request) input.readObject();
				if (clientRequest.getRequestType() == RequestCode.DOCUMENT_SENT) {
					EditableDocument document = clientRequest.getDocument();
					//this.saveDocument(document);		// FIXME: must be able to save from server
					this.writeDocumentToClients(document);
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

	private void saveDocument(EditableDocument doc) {
		// TODO: Let the server do the actual saving
	}

	/**
	 * Sends new shape to all connected clients
	 * 
	 * @param shape
	 *            the shape to write to clients
	 */
	private void writeDocumentToClients(EditableDocument doc) {
		synchronized (networkAccounts) {
			Set<ObjectOutputStream> closedClientsList = new HashSet<ObjectOutputStream>();
			Iterator it = networkAccounts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				ObjectOutputStream oos = (ObjectOutputStream) pair.getKey();
				serverResponse = new Response(ResponseCode.DOCUMENT_SENT, doc);
				try {
					oos.writeObject(serverResponse);
				} catch (IOException e) {
					networkAccounts.remove((ObjectOutputStream) pair.getKey());
					e.printStackTrace();
				}
				// NOT THREAD SAFE
			}
		}
	}
}
