package network;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import model.EditableDocument;
import model.User;

/**
 * The Response class uses the command design pattern to wrap multiple commands
 * in one object. The commands are listed under the ResponseCode Enum
 * 
 * @author Stephen Connolly
 * @author Cody Deeran
 * @author Brittany Paielli
 * @author Joshua Riccio
 *
 */
public class Response implements Serializable {

	private static final long serialVersionUID = -6676768090079836369L;
	private ResponseCode responseCode;
	private EditableDocument doc;
	private String[] userlist;
	private String[] editorList;
	private String[] ownerList;
	private String message;
	private User user;
	private String username;

	/**
	 * Constructor for response object
	 * 
	 * @param responseCode the code specifying specific response @param
	 * message @param username
	 */
	public Response(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Constructor with ability to store document
	 * 
	 * @param responseCode the code specifying specific response @param doc the
	 * document to send
	 */
	public Response(ResponseCode responseCode, EditableDocument doc) {
		this.responseCode = responseCode;
		this.doc = doc;
	}

	/**
	 * Gets the response ID @return returns the response ID
	 */
	public ResponseCode getResponseID() {
		return responseCode;
	}

	/**
	 * Gets the editable document @return returns the document
	 */
	public EditableDocument getEditableDocument() {
		return doc;
	}

	/**
	 * Gets the styled document @return returns the styled document
	 */
	public StyledDocument getStyledDocument() {
		return doc.getDocument();
	}

	/**
	 * Sets the user list @param userlist sets the list of users in system
	 */
	public void setUserList(String[] userlist) {
		this.userlist = userlist;
	}

	/**
	 * Gets the user list @param userlist gets the list of users in system
	 */
	public String[] getUserList() {
		return this.userlist;
	}

	/**
	 * Gets the user @param user the user @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the user @param user the user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sets the editorlist @param editorlist sets the document list
	 */
	public void setEditorList(String[] editorlist) {
		this.editorList = editorlist;
	}

	/**
	 * Gets the editor list Gets the list of documents
	 */
	public String[] getEditorList() {
		return this.editorList;
	}

	/**
	 * Sets the owner list @param ownerList sets the document list
	 */
	public void setOwnerList(String[] ownerList) {
		this.ownerList = ownerList;
	}

	/**
	 * Gets the owner list Gets the list of documents
	 */
	public String[] getOwnerList() {
		return this.ownerList;
	}

	/**
	 * Sets the message @param message the message to be sent
	 */
	public void setMessage(String message) {
		this.message = message;

	}

	/**
	 * Gets the requests code
	 * 
	 * @return Returns the response's message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the username @param username the users name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the user name @return the username
	 */
	public String getUsername() {
		return this.username;
	}
}
