package network;

import java.io.Serializable;

import model.EditableDocument;
import model.User;

/**
 * The Request class uses the command design pattern to wrap multiple commands
 * in one object. The commands are listed under the RequestCode Enum
 * 
 * @author Joshua Riccio
 *
 */
public class Request implements Serializable {

	/**
	 * 
	 */
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
	 * @param requestCode
	 *            the request code
	 * @param message2
	 */
	public Request(RequestCode requestCode) {
		this.requestCode = requestCode;
	}

	/**
	 * @return returns the request type
	 */
	public RequestCode getRequestType() {
		return requestCode;
	}

	/**
	 * @return returns the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            sets the user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param doc
	 *            sets the document
	 */
	public void setDocument(EditableDocument doc) {
		this.doc = doc;
	}

	/**
	 * 
	 * @return returns the current EditableDocument
	 */
	public EditableDocument getDocument() {
		return doc;
	}

	/**
	 * @param String
	 *            sets the name of the requestedDoc
	 */
	public void setRequestedName(String name) {
		this.documentName = name;
	}

	/**
	 * 
	 * @return returns the name of the requestedDoc
	 */
	public String getRequestedName() {
		return documentName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;

	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;

	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentName() {
		return this.documentName;
	}

	public String getSummary() {

		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setMessage(String message) {
		this.message = message;

	}

	public String getMessage() {
		return this.message;
	}

}
