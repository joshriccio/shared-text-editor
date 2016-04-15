package network;

import java.io.Serializable;

import javax.swing.JLabel;

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

	private static final long serialVersionUID = 1L;
	private RequestCode requestCode;
	private User user;
	private String username;
	private String password;
	private EditableDocument doc;

	/**
	 * Constructor for request code
	 * 
	 * @param requestCode
	 *            the request code
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
	

}
