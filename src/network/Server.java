package network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;
import model.EditableDocument;
import model.User;
/**
 * The Server class acts as the communication portal between clients. The Server
 * receives requests and generates responses.
 * 
 * @author Cody Deeran
 * @author Joshua Riccio
 */
public class Server {
	public static int PORT_NUMBER = 4000;
	private static ServerSocket serverSocket;
	private static Vector<UserStreamModel> networkAccounts;
	private static HashMap<String,Integer> usersToIndex;
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private static Request clientRequest;
	private static Response serverResponse;
	private static User user;
	/**
	 * Receives requests from the client and processes responses.
	 * networkAccounts in a list of users mapped to their objectOutputStreams
	 * with their current online status. usersToIndex maps the user name to the
	 * index location in networkAccounts. This gives an O(1) search time to find
	 * users inside networkAccounts.
	 * 
	 * @param args
	 *            Never used
	 */
	public static void main(String[] args) {
		networkAccounts = new Vector<UserStreamModel>();
		usersToIndex = new HashMap<String,Integer>();
		setDefaultAccounts();
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
						networkAccounts.get(usersToIndex.get(user.getUsername())).setOutputStream(oos);
						System.out.println(serverResponse.getResponseID());
						oos.writeObject(serverResponse);
						ClientHandler c = new ClientHandler(ois,networkAccounts);
						c.start();
					}
					else {
						serverResponse = new Response(ResponseCode.LOGIN_FAILED);
						System.out.println(serverResponse.getResponseID());
						oos.writeObject(serverResponse);
					}
				}
				else if (clientRequest.getRequestType() == RequestCode.CREATE_ACCOUNT) {
					user = clientRequest.getUser();
					if (authenticateNewUser(user)) {
						UserStreamModel usm = new UserStreamModel(user,null);
						usersToIndex.put(user.getUsername(),networkAccounts.size());
						networkAccounts.add(usm);
						serverResponse = new Response(ResponseCode.ACCOUNT_CREATED_SUCCESSFULLY);
						oos.writeObject(serverResponse);
					}
					else {
						serverResponse = new Response(ResponseCode.ACCOUNT_CREATION_FAILED);
						oos.writeObject(serverResponse);
					}
				}
				else if (clientRequest.getRequestType() == RequestCode.RESET_PASSWORD) {
					user = clientRequest.getUser();
					if (!authenticateNewUser(user)) {
						networkAccounts.get(usersToIndex.get(user.getUsername())).getUser().setPassword(user.getPassword());
						serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_SUCCESSFUL);
						oos.writeObject(serverResponse);
					}
					else {
						serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_FAILED);
						oos.writeObject(serverResponse);
					}
				}
			}
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	private static void setDefaultAccounts() {
		User usr = new User("Josh","123");
		UserStreamModel usm = new UserStreamModel(usr,null);
		usersToIndex.put(usr.getUsername(),networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Cody","456");
		usm = new UserStreamModel(usr,null);
		usersToIndex.put(usr.getUsername(),networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Brittany","789");
		usm = new UserStreamModel(usr,null);
		usersToIndex.put(usr.getUsername(),networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Stephen","boss");
		usm = new UserStreamModel(usr,null);
		usersToIndex.put(usr.getUsername(),networkAccounts.size());
		networkAccounts.add(usm);
	}
	private static boolean authenticateNewUser(User user) {
		if (usersToIndex.containsKey(user.getUsername())) {
			return false;
		}
		else {
			return true;
		}
	}
	private static boolean authenticate(User user) {
		int index;
		if (usersToIndex.containsKey(user.getUsername())) {
			index = usersToIndex.get(user.getUsername());
			if (networkAccounts.get(index).getUser().getPassword().equals(user.getPassword())) {
				networkAccounts.get(index).toggleOnline();
				return true;
			}
			else
				return false;
		}
		return false;
	}
}
/**
 * ClientHandler gerates a new thread to manage client activity
 * 
 * @author Josh Riccio (jriccio@email.arizona.edu)
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 */
class ClientHandler extends Thread {
	private ObjectInputStream input;
	private Vector<UserStreamModel> networkAccounts;
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
	public ClientHandler(ObjectInputStream input, Vector<UserStreamModel> networkAccounts) {
		this.input = input;
		this.networkAccounts = networkAccounts;
	}
	@Override
	public void run() {
		while (isRunning) {
			try {
				clientRequest = (Request) input.readObject();
				if (clientRequest.getRequestType() == RequestCode.DOCUMENT_SENT) {
					this.writeDocumentToClients(clientRequest.getDocument());
				}
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				this.cleanUp();
			}
		}
	}
	private void cleanUp() {
		isRunning = false;
		System.out.println("Client has been disconnected");
	}
	/**
	 * Sends new shape to all connected clients
	 * 
	 * @param shape
	 *            the shape to write to clients
	 */
	private void writeDocumentToClients(EditableDocument doc) {
		synchronized (networkAccounts) {
			serverResponse = new Response(ResponseCode.DOCUMENT_SENT,doc);
			for (UserStreamModel user : networkAccounts) {
				try {
					if (user.isOnline())
						user.getOuputStream().writeObject(serverResponse);
				}
				catch (IOException e) {
					// If user is no longer online, exception occurs, changes
					// their status to offline
					user.toggleOnline();
					e.printStackTrace();
				}
			}
		}
	}
}
