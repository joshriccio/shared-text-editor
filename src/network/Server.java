package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import model.LinkedListForSaves;
import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;
import network.Server;

/**
 * The Server class acts as the communication portal between clients. The Server
 * receives requests and generates responses.
 * 
 * @author Cody Deeran(cdeeran11@email.arizona.edu) @author Joshua Riccio
 */
public class Server {
    public static final String ADDRESS = "localhost";
    // public static final String ADDRESS =
    // "ec2-52-39-48-243.us-west-2.compute.amazonaws.com"; //Used for production
    // server
    public static int PORT_NUMBER = 4001;
    private static ServerSocket serverSocket;
    static Vector<UserStreamModel> networkAccounts = new Vector<UserStreamModel>();
    static HashMap<String, Integer> usersToIndex = new HashMap<String, Integer>();
    private static Socket socket;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private static Request clientRequest;
    private static Response serverResponse;
    private static User user;
    private static String securePassword;
    public static LinkedListForSaves savedFileList;

    /**
     * Receives requests from the client and processes responses.
     * networkAccounts in a list of users mapped to their objectOutputStreams
     * with their current online status. usersToIndex maps the user name to the
     * index location in networkAccounts. This gives an O(1) search time to find
     * users inside networkAccounts.
     * 
     * @param args
     *            Never used @throws Exception @throws
     *            NoSuchProviderException @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, Exception {
        setDefaultAccounts();
        socket = null;
        serverSocket = null;
        ois = null;
        oos = null;
        if (loadServerState()) {
            System.out.println("Server: Previous server state loaded");
        } else
            savedFileList = new LinkedListForSaves();
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                saveServerState();
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
                } else if (clientRequest.getRequestType() == RequestCode.START_DOCUMENT_STREAM) {
                    processNewDocumentStream(ois, oos);
                } else if (clientRequest.getRequestType() == RequestCode.SEND_MESSAGE) {
                    processSendMessage(ois, clientRequest.getUser());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean loadServerState() {
        File serverbackup = new File("server.bak");
        if (serverbackup.exists() && !serverbackup.isDirectory()) {
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(serverbackup));
                savedFileList = (LinkedListForSaves) input.readObject();
                input.close();
                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void saveServerState() {
        try {
            FileOutputStream outFile = new FileOutputStream("server.bak");
            ObjectOutputStream outputStream;
            outputStream = new ObjectOutputStream(outFile);
            outputStream.writeObject(savedFileList);
            System.out.println("Server: Server state backed up");
            outputStream.close();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void processNewDocumentStream(ObjectInputStream input, ObjectOutputStream output) {
        DocumentHandler d = new DocumentHandler(input, output);
        d.start();
    }

    private static void processSendMessage(ObjectInputStream ois, User user) {
        ChatHandler ch = new ChatHandler(ois, user);
        ch.start();
    }

    private static void processLogin() throws IOException {
        if (authenticate(clientRequest.getUsername(), clientRequest.getPassword())) {
            serverResponse = new Response(ResponseCode.LOGIN_SUCCESSFUL, null, null);
            serverResponse.setUser(networkAccounts.get(usersToIndex.get(clientRequest.getUsername())).getUser());
            networkAccounts.get(usersToIndex.get(clientRequest.getUsername())).setOutputStream(oos);
            System.out.println("Server: User " + clientRequest.getUsername() + " has logged in");
            oos.writeObject(serverResponse);
            ClientHandler c = new ClientHandler(ois, clientRequest.getUsername());
            c.start();
        } else {
            serverResponse = new Response(ResponseCode.LOGIN_FAILED, null, null);
            oos.writeObject(serverResponse);
        }
    }

    private static void processAccountCreation() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        if (!userExists(clientRequest.getUsername()) && clientRequest.getUsername().charAt(0) != '-') {
            user = new User(clientRequest.getUsername(), clientRequest.getPassword());
            UserStreamModel usm = new UserStreamModel(user, null);
            usersToIndex.put(user.getUsername(), networkAccounts.size());
            networkAccounts.add(usm);
            serverResponse = new Response(ResponseCode.ACCOUNT_CREATED_SUCCESSFULLY, null, null);
            oos.writeObject(serverResponse);
        } else {
            serverResponse = new Response(ResponseCode.ACCOUNT_CREATION_FAILED, null, null);
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
            serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_SUCCESSFUL, null, null);
            oos.writeObject(serverResponse);
        } else {
            serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_FAILED, null, null);
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

    /**
     * Returns the Network Accounts vector
     * 
     * @return the Network Accounts vector
     */
    public static Vector<UserStreamModel> getNetworkAccounts() {
        return networkAccounts;

    }

    /**
     * Returns the UsersToIndex map
     * 
     * @return the UsersToIndex map
     */
    public static HashMap<String, Integer> getUsersToIndex() {
        return usersToIndex;

    }
}

/**
 * ClientHandler generates a new thread to manage client activity
 * 
 * @author Josh Riccio (jriccio@email.arizona.edu)
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 */
class ClientHandler extends Thread {
    private ObjectInputStream input;
    private volatile boolean isRunning = true;
    private Request clientRequest;
    private Response serverResponse;
    private String username;

    /**
     * Constructor
     * 
     * @param input
     *            the object input stream @param networkAccounts the list of
     *            uses connected
     */
    public ClientHandler(ObjectInputStream input, String username) {
        this.input = input;
        this.username = username;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                clientRequest = (Request) input.readObject();

                if (clientRequest.getRequestType() == RequestCode.GET_USER_LIST) {
                    writeUsersToClients();
                } else if (clientRequest.getRequestType() == RequestCode.USER_EXITING) {
                    Server.getNetworkAccounts().get(Server.getUsersToIndex().get(clientRequest.getUsername()))
                                    .toggleOnline();
                    writeUsersToClients();
                } else if (clientRequest.getRequestType() == RequestCode.REQUEST_DOCUMENT) {
                    processDocumentRequest();
                } else if (clientRequest.getRequestType() == RequestCode.RESET_PASSWORD) {
                    processPasswordReset();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                this.cleanUp();
            }
        }

    }

    private void processDocumentRequest() {
        String requestedDocumentName = clientRequest.getRequestedName();
        User client = clientRequest.getUser();
        String mostRecentFile = "./" + Server.savedFileList.getMostRecentSave(requestedDocumentName);
        ObjectOutputStream oos = null;
        oos = Server.networkAccounts.get(Server.usersToIndex.get(client.getUsername())).getOuputStream();

        try {
            FileInputStream inFile = new FileInputStream(mostRecentFile);
            ObjectInputStream inputStream = new ObjectInputStream(inFile);
            EditableDocument document = (EditableDocument) inputStream.readObject();
            inputStream.close();
            Response sendDocRequest = new Response(ResponseCode.DOCUMENT_SENT, document);
            oos.writeObject(sendDocRequest);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void cleanUp() {
        isRunning = false;
        System.out.println("Server: " + username + " has been disconnected");
    }

    private void processPasswordReset() throws IOException {
        User updatepassword = Server.getNetworkAccounts().get(Server.getUsersToIndex().get(clientRequest.getUsername()))
                        .getUser();
        try {
            updatepassword.setPassword(
                            Password.generateSecurePassword(clientRequest.getPassword(), updatepassword.getSalt()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        serverResponse = new Response(ResponseCode.ACCOUNT_RESET_PASSWORD_SUCCESSFUL, null, null);
        Server.getNetworkAccounts().get(Server.getUsersToIndex().get(clientRequest.getUsername())).getOuputStream()
                        .writeObject(serverResponse);

    }

    private void writeUsersToClients() {
        synchronized (Server.getNetworkAccounts()) {
            serverResponse = new Response(ResponseCode.USER_LIST_SENT, null, null);
            serverResponse.setUserList(usersToArray());
            for (UserStreamModel user : Server.getNetworkAccounts()) {
                try {
                    if (user.isOnline())
                        user.getOuputStream().writeObject(serverResponse);
                } catch (IOException e) {
                    user.toggleOnline();
                }
            }
        }
    }

    /**
     * This method converts networkAccounts to a string array of usernames. If
     * the user is offline the username is prefaced by a - symbol. When the
     * client recieves the list they now are able to differentiate between users
     * online and users offline.
     * 
     * @return an array of type string, all users in networkAccounts
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

/**
 * This class handles document processing in a new thread
 * 
 * @author Joshua Riccio
 *
 */
class DocumentHandler extends Thread {
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Builds a new DocumentHandler
     * 
     * @param ois
     *            ObjectInputStream
     * @param oos
     *            ObjectOutputStream
     */
    public DocumentHandler(ObjectInputStream ois, ObjectOutputStream oos) {
        this.input = ois;
        this.output = oos;
    }

    @Override
    public void run() {
        try {
            Request clientRequest = (Request) input.readObject();
            if (clientRequest.getRequestType() == RequestCode.DOCUMENT_SENT) {
                EditableDocument document = clientRequest.getDocument();
                this.saveDocument(document);
            } else if (clientRequest.getRequestType() == RequestCode.REQUEST_DOCUMENT_LIST) {
                processDocumentListRequest(clientRequest.getUsername());
            } else if (clientRequest.getRequestType() == RequestCode.ADD_USER_AS_EDITOR) {
                processAddUserAsEditor(clientRequest);
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error: Document Stream Disconnected");
        }
    }

    private void processAddUserAsEditor(Request clientRequest) {
        Response response;
        if (Server.savedFileList.addUserAsEditor(clientRequest.getUsername(), clientRequest.getDocumentName())) {
            response = new Response(ResponseCode.USER_ADDED, null, null);
            try {
                this.output.writeObject(response);
                this.output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response = new Response(ResponseCode.USER_NOT_ADDED, null, null);
            try {
                this.output.writeObject(response);
                this.output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDocument(EditableDocument doc) {
        synchronized (Server.savedFileList) {
            String newDocName = "revisionhistory/" + doc.getName() + System.currentTimeMillis();
            try {

                FileOutputStream outFile = new FileOutputStream(newDocName);
                ObjectOutputStream outputStream = new ObjectOutputStream(outFile);
                outputStream.writeObject(doc);
                System.out.println("Server: File saved - " + doc.getName());
                outputStream.close();
                outFile.close();

                Server.savedFileList.createSave(doc, newDocName, doc.getDocumentOwner());
            } catch (IOException e) {
                System.out.println("Error: Couldn't create a new save file");
                e.printStackTrace();
            }
        }
    }

    private void processDocumentListRequest(String username) {
        System.out.println("Server: Processing Document lists for: " + username);
        String[] editorlist = Server.savedFileList.getDocumentsByEditor(username);
        String[] ownerlist = Server.savedFileList.getDocumentsByOwner(username);

        Response response = new Response(ResponseCode.DOCUMENT_LISTS_SENT, null, null);
        response.setEditorList(editorlist);
        response.setOwnerList(ownerlist);
        try {
            output.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ChatHandler extends Thread {
    private ObjectInputStream incomingMessage;
    private String username;

    public ChatHandler(ObjectInputStream ois, User user) {
        this.incomingMessage = ois;
        this.username = user.getUsername();
    }

    @Override
    public void run() {
        try {
            Request request = (Request) incomingMessage.readObject();
            if (request.getRequestType() == RequestCode.SEND_MESSAGE) {
                sendMessageToClients(request.getMessage());
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToClients(String message) {
        synchronized (Server.getNetworkAccounts()) {
            Response response = new Response(ResponseCode.NEW_MESSAGE, this.username, message);
            response.setUserList(usersToArray());
            for (UserStreamModel user : Server.getNetworkAccounts()) {
                try {
                    if (user.isOnline())
                        System.out.println("Server: Sending the following repsonse" + response.getResponseID()
                                        + this.username + response.getMessage());
                    user.getOuputStream().writeObject(response);
                } catch (IOException e) {
                    user.toggleOnline();
                }
            }
        }
    }

    /**
     * This method converts networkAccounts to a string array of usernames. If
     * the user is offline the username is prefaced by a - symbol. When the
     * client recieves the list they now are able to differentiate between users
     * online and users offline.
     * 
     * @return an array of type string, all users in networkAccounts
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