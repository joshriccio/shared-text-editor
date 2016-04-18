package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Vector;
import model.EditableDocument;
import model.Password;
import model.User;

/**
 * The Server class acts as the communication portal between clients. The Server
 * receives requests and generates responses.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu) @author Joshua Riccio
 */
public class Server {
	public static int PORT_NUMBER = 4001;
	private static ServerSocket serverSocket;
	private static Vector<UserStreamModel> networkAccounts = new Vector<UserStreamModel>();
	private static HashMap<String, Integer> usersToIndex = new HashMap<String, Integer>();
	private static Socket socket;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	private static Request clientRequest;
	private static Response serverResponse;
	private static User user;
	private static String securePassword;

	/**
	 * Receives requests from the client and processes responses.
	 * networkAccounts in a list of users mapped to their objectOutputStreams
	 * with their current online status. usersToIndex maps the user name to the
	 * index location in networkAccounts. This gives an O(1) search time to find
	 * users inside networkAccounts.
	 * 
	 * @param args Never used @throws Exception @throws
	 * NoSuchProviderException @throws NoSuchAlgorithmException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, Exception {
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
					processLogin();
				} else if (clientRequest.getRequestType() == RequestCode.CREATE_ACCOUNT) {
					processAccountCreation();
				} else if (clientRequest.getRequestType() == RequestCode.RESET_PASSWORD) {
					processPasswordReset();
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void processLogin() throws IOException {
		if (authenticate(clientRequest.getUsername(), clientRequest.getPassword())) {
			serverResponse = new Response(ResponseCode.LOGIN_SUCCESSFUL);
			serverResponse.setUser(networkAccounts.get(usersToIndex.get(clientRequest.getUsername())).getUser());
			networkAccounts.get(usersToIndex.get(clientRequest.getUsername())).setOutputStream(oos);
			System.out.println(serverResponse.getResponseID());
			oos.writeObject(serverResponse);
			ClientHandler c = new ClientHandler(ois);
			c.start();
		} else {
			serverResponse = new Response(ResponseCode.LOGIN_FAILED);
			oos.writeObject(serverResponse);
		}
	}

	private static void processAccountCreation() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
		if (!userExists(clientRequest.getUsername()) && clientRequest.getUsername().charAt(0) != '-') {
			user = new User(clientRequest.getUsername(), clientRequest.getPassword());
			UserStreamModel usm = new UserStreamModel(user, null);
			usersToIndex.put(user.getUsername(), networkAccounts.size());
			networkAccounts.add(usm);
			serverResponse = new Response(ResponseCode.ACCOUNT_CREATED_SUCCESSFULLY);
			oos.writeObject(serverResponse);
		} else {
			serverResponse = new Response(ResponseCode.ACCOUNT_CREATION_FAILED);
			oos.writeObject(serverResponse);
		}
	}

	private static void processPasswordReset() throws IOException {
		if (userExists(clientRequest.getUsername())) {
			User updatepassword = networkAccounts.get(usersToIndex.get(clientRequest.getUsername())).getUser();
			try {
				updatepassword.setPassword(
						Password.generateSecurePassword(clientRequest.getPassword(), updatepassword.getSalt()));
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				e.printStackTrace();
			}
			serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_SUCCESSFUL);
			oos.writeObject(serverResponse);
		} else {
			serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_FAILED);
			oos.writeObject(serverResponse);
		}
	}

	private static void setDefaultAccounts() throws NoSuchAlgorithmException, NoSuchProviderException, Exception {
		User usr = new User("Josh", "123");
		UserStreamModel usm;
		usm = new UserStreamModel(usr, null);
		usersToIndex.put(usr.getUsername(), networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Cody", "456");
		usm = new UserStreamModel(usr, null);
		usersToIndex.put(usr.getUsername(), networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Brittany", "789");
		usm = new UserStreamModel(usr, null);
		usersToIndex.put(usr.getUsername(), networkAccounts.size());
		networkAccounts.add(usm);
		usr = new User("Stephen", "boss");
		usm = new UserStreamModel(usr, null);
		usersToIndex.put(usr.getUsername(), networkAccounts.size());
		networkAccounts.add(usm);
	}

	private static boolean userExists(String username) {
		if (usersToIndex.containsKey(username)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean authenticate(String username, String password) {
		int index;
		if (usersToIndex.containsKey(username)) {
			index = usersToIndex.get(username);
			try {
				securePassword = Password.generateSecurePassword(password,
						networkAccounts.get(index).getUser().getSalt());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			}
			if (networkAccounts.get(index).getUser().getPassword().equals(securePassword)) {
				networkAccounts.get(index).toggleOnline();
				return true;
			} else
				return false;
		}
		return false;
	}

	public static Vector<UserStreamModel> getNetworkAccounts() {
		return networkAccounts;

	}

	public static HashMap<String, Integer> getUsersToIndex() {
		return usersToIndex;

	}
}

/**
 * ClientHandler gerates a new thread to manage client activity
 * 
 * @author Josh Riccio (jriccio@email.arizona.edu) @author Cody Deeran
 * (cdeeran11@email.arizona.edu)
 */
class ClientHandler extends Thread {
	private ObjectInputStream input;
	private volatile boolean isRunning = true;
	private Request clientRequest;
	private Response serverResponse;

	/**
	 * Constructor
	 * 
	 * @param input the object input stream @param networkAccounts the list of
	 * uses connected
	 */
	public ClientHandler(ObjectInputStream input) {
		this.input = input;
	}

	@Override
	public void run() {
		while (isRunning) {
			try {
				clientRequest = (Request) input.readObject();
				if (clientRequest.getRequestType() == RequestCode.DOCUMENT_SENT) {
					EditableDocument document = clientRequest.getDocument();
					// this.saveDocument(document); // FIXME: must be able to
					// save from server
					this.writeDocumentToClients(document);
				} else if (clientRequest.getRequestType() == RequestCode.GET_USER_LIST) {
					writeUsersToClients();
				} else if (clientRequest.getRequestType() == RequestCode.USER_EXITING) {
					Server.getNetworkAccounts().get(Server.getUsersToIndex().get(clientRequest.getUsername()))
							.toggleOnline();
					writeUsersToClients();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				this.cleanUp();
			}
		}
	}

	private void cleanUp() {
		isRunning = false;
		System.out.println("Client has been disconnected");
	}

	@SuppressWarnings("unused")
    private void saveDocument(EditableDocument doc) {
		// TODO: Let the server do the actual saving
	}

	/**
	 * Sends new shape to all connected clients
	 * 
	 * @param shape the shape to write to clients
	 */
	private void writeDocumentToClients(EditableDocument doc) {
		synchronized (Server.getNetworkAccounts()) {
			serverResponse = new Response(ResponseCode.DOCUMENT_SENT, doc);
			for (UserStreamModel user : Server.getNetworkAccounts()) {
				try {
					if (user.isOnline())
						user.getOuputStream().writeObject(serverResponse);
				} catch (IOException e) {
					// If user is no longer online, exception occurs, changes
					// their status to offline
					user.toggleOnline();
				}
			}
		}
	}

	/**
	 * Sends new shape to all connected clients
	 * 
	 * @param shape the shape to write to clients
	 */
	private void writeUsersToClients() {
		synchronized (Server.getNetworkAccounts()) {
			serverResponse = new Response(ResponseCode.USER_LIST_SENT);
			serverResponse.setUserList(usersToArray());
			for (UserStreamModel user : Server.getNetworkAccounts()) {
				try {
					if (user.isOnline())
						user.getOuputStream().writeObject(serverResponse);
				} catch (IOException e) {
					// If user is no longer online, exception occurs, changes
					// their status to offline
					user.toggleOnline();
				}
			}
		}
	}

	/**
	 * This method converts networkAccounts to a string array of usernames. If the user is offline 
	 * the username is prefaced by a - symbol. When the client recieves the list they now are able
	 * to differentiate between users online and users offline. 
	 * @return
	 * 		an array of type string, all users in networkAccounts
	 */
	private String[] usersToArray() {
		String[] userlist = new String[Server.getNetworkAccounts().size()];
		for (int i = 0; i < userlist.length; i++) {
			if (Server.getNetworkAccounts().get(i).isOnline())
				userlist[i] = Server.getNetworkAccounts().get(i).getUser().getUsername();
			else
				userlist[i] = "-" + Server.getNetworkAccounts().get(i).getUser().getUsername();
		}
		return userlist;
	}
}
