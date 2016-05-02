package network;

import java.io.Serializable;

import model.EditableDocument;
import model.User;

/**
 * The Request class uses the command design pattern to wrap multiple commands
 * in one object. The commands are listed under the RequestCode Enum
 * 
 * @author Joshua Riccio
 * @author Stephen Connolly
 * @author Cody Deeran
 * @author Brittany Paielli
 *
 */
public class Request implements Serializable {

	private static final long serialVersionUID = 8964365134630346582L;
	private RequestCode requestCode;
	private User user;
	private String username;
	private String password;
	private String message;
	private EditableDocument doc;
	private String documentName;
	private String summary;

	/**
	 * Constructor for request code
	 * 
	 * @param requestCode the request code @param message2
	 */
	public Request(RequestCode requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * Gets the request type @return returns the request type
	 */
	public RequestCode getRequestType() {
		return requestCode;
	}

	/**
	 * Gets the users name @return returns the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the users name @param user sets the user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * sets the document @param doc sets the document
	 */
	public void setDocument(EditableDocument doc) {
		this.doc = doc;
	}

	/**
	 * Gets the document @return returns the current EditableDocument
	 */
	public EditableDocument getDocument() {
		return doc;
	}

	/**
	 * Sets the document name @param String sets the name of the requestedDoc
	 */
	public void setRequestedName(String name) {
		this.documentName = name;
	}

	/**
	 * Gets the document name @return returns the name of the requestedDoc
	 */
	public String getRequestedName() {
		return documentName;
	}

	/**
	 * Sets the user name in the request @param username the name to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets the password in the request @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;

	}

	/**
	 * Gets the user name in the request @return the user name
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Gets the password that was set @return the password
	 */
	public String getPassword() {
		return this.password;

	}

	/**
	 * Sets the document name @param documentName the name to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/**
	 * Gets the document name @return the name of the document
	 */
	public String getDocumentName() {
		return this.documentName;
	}

	/**
	 * Gets the summary that was set @return the summary
	 */
	public String getSummary() {
		return this.summary;
	}

	/**
	 * Sets the summary of the request @param summary the summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Sets the message @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;

	}

	/**
	 * Gets the message that was set @return the message
	 */
	public String getMessage() {
		return this.message;
	}

}
