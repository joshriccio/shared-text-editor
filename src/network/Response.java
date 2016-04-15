package network;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import model.EditableDocument;
import model.User;

/**
 * The Response class uses the command design pattern to wrap multiple commands
 * in one object. The commands are listed under the ResponseCode Enum
 * 
 * @author Joshua Riccio
 *
 */
public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private ResponseCode responseCode;
	private EditableDocument doc;
	private String[] userlist;
	private User user;

	/**
	 * Constructor
	 * 
	 * @param responseCode
	 *            the code specifying specific response
	 */
	public Response(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Constructor with ability to store document
	 * 
	 * @param responseCode
	 *            the code specifying specific response
	 * @param doc
	 *            the document to send
	 */
	public Response(ResponseCode responseCode, EditableDocument doc) {
		this.responseCode = responseCode;
		this.doc = doc;
	}

	/**
	 * @return returns the response ID
	 */
	public ResponseCode getResponseID() {
		return responseCode;
	}

	/**
	 * @return returns the document
	 */
	public EditableDocument getEditableDocument() {
		return doc;
	}

	/**
	 * @return returns the styled document
	 */
	public StyledDocument getStyledDocument() {
		return doc.getDocument();
	}

	/**
	 * 
	 * @param userlist sets the list of users in system
	 */
	public void setUserList(String[] userlist) {
		this.userlist = userlist;
	}
	
	/**
	 * 
	 * @param userlist sets the list of users in system
	 */
	public String[] getUserList() {
		return this.userlist;
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
