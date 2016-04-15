package model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed to handle user account interactions. A unique
 * username, ID and password, the ability to change their password and reset a
 * forgotten one, and keep track of documents that are owned and able to be
 * edited by the user
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	private String username;
	private String password;
	private String salt;
	private List<EditableDocument> editableDocs;
	private List<EditableDocument> ownedDocs;

	/**
	 * Costructor for the user class, assigns both the username and password
	 * to the Strings passed in.
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 */
	public User(String username, String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.username = username;
		this.salt = Password.generateSaltValue();
		this.password = Password.generateSecurePassword(password, this.salt);
		this.editableDocs = new ArrayList<EditableDocument>();
		this.ownedDocs = new ArrayList<EditableDocument>();
	}

	/**
	 * This retures the username for the respective user
	 * @return returns the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This returns the password for the respective user
	 * @return returns the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * This sets the new user password
	 * @param password
	 * 					this new password that will be set for the user
	 */	
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This sets the salt value for the user's password
	 * @param inSalt
	 * 			the salt value for the user's password
	 */
	public void setSalt(String inSalt){
		this.salt = inSalt;
	}
	
	/**
	 * Get the salt value for the user's password
	 * @return the salt value for the user's password
	 */
	public String getSalt(){
		return this.salt;
	}
	
	/**
	 * Returns a list of the documents owned by the user
	 * @return a list of the documents ownded by the user
	 */
	public List<EditableDocument> getOwnedDocuments(){
		return this.ownedDocs;
	}
	
	/**
	 * Make the user the owner of the document
	 * @param document
	 * 			the name of the document that the user will own
	 */
	public void addAsOwner(EditableDocument document){
		this.ownedDocs.add(document);
	}
	
	/**
	 * Change the permissions to where the user does not own the document
	 * @param document
	 * 					the document to change the permissions on
	 */
	public void removeAsOwner(EditableDocument document){
		this.ownedDocs.remove(document);
	}
	/**
	 * Get the list of documents that the user can edit
	 * @return the list of documents the user can edit
	 */
	public List<EditableDocument> getEditableDocuments(){
		return this.editableDocs;
	}
	/**
	 * Give the user permissions to edit the document
	 * @param document
	 * 					the document to give editable permissions to.
	 */
	public void addAsEditor(EditableDocument document){
		this.editableDocs.add(document);
	}
	
	/**
	 * Take away the user's permissions to edit the document
	 * @param document
	 * 					the to remove the user's editable permissions from
	 */
	public void removeAsEditor(EditableDocument document){
		this.editableDocs.remove(document);
	}
}
