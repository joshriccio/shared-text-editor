package model;

import javax.swing.text.StyledDocument;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

/**
 * 
 * EditableDocument encapsulates a document with the style, time stamp, and
 * owner
 * 
 * @author Steven Connolly
 *
 */
public class EditableDocument implements Serializable, Comparable<Timestamp> {

	private static final long serialVersionUID = 1L;
	private StyledDocument document;
	private Timestamp timestamp;
	private User documentOwner = null;
	private String name;
	private Vector<User> owners = new Vector<User>();
	private Vector<User> editors = new Vector<User>();
	private String summary;

	/**
	 * Constructor
	 * 
	 * @param doc
	 *            document to be added to wrapper
	 * @param ownr
	 *            owner of the document
	 */
	public EditableDocument(StyledDocument doc, User ownr, String name) {
		document = doc;
		documentOwner = ownr;
		this.name = name;
		generateNewTimeStamp();
		this.summary = this.documentOwner.getUsername() + " made changes on " + timestamp.toString();
	}

	/**
	 * Constructor used for testing
	 * 
	 * @param doc
	 *            document to be changed in the wrapper
	 */
	public EditableDocument(StyledDocument doc, String name) {
		document = doc;
		this.name = name;
		this.summary = this.name + " changes made recently";
	}
	
	
	/**
	 * This method is used to get the String value of the name of the EditableDocument
	 * @return
	 * 		String EditableDocument.name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Receive changed data from GUI, update.
	 * 
	 * @param newDoc
	 *            sets a new document
	 */
	public void setDocumentContent(StyledDocument newDoc) {
		document = newDoc;
		generateNewTimeStamp();
	}

	/**
	 * Gets the precise current time and stamps it.
	 */
	public void generateNewTimeStamp() {
		timestamp = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * Returns most recent document
	 * 
	 * @return returns the most recent document
	 */
	public StyledDocument getDocument() {
		return document;
	}

	/**
	 * Return most recent time stamp
	 * 
	 * @return returns the most recent time stamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * Return document's owner
	 * 
	 * @return returns the documents owner
	 */
	public User getDocumentOwner() {
		return documentOwner;
	}

	// Return (+) if THIS doc is MORE RECENT than other
	@Override
	public int compareTo(Timestamp other) {
		return timestamp.compareTo(other);
	}
	
	/**
	 * This method returns a vector of owners of the document
	 * 
	 * @return vector of owners
	 */
	public Vector<User> getOwners(){
		return this.owners;
	}
	
	/**
	 * This method adds a user to the owners vector
	 * 
	 * @param user
	 * 		user who is an owner
	 */
	public void addOwner(User user){
		owners.add(user);
	}
	
	/**
	 * This method removes a user from the owners vector
	 * 
	 * @param user
	 * 			user to be removed
	 */
	public void removeOwner(User user){
		owners.remove(user);
	}
	
	/**
	 * This method returns the vector of editors
	 * 
	 * @return vector of editors
	 */
	public Vector<User> getEditors(){
		return this.editors;
	}
	
	/**
	 * This method adds a user to the editor vector
	 * 
	 * @param user
	 * 			user to be added as an editor
	 */
	public void addEditor(User user){
		editors.add(user);
	}
	
	/**
	 * This method removes a user from the editor vector
	 * 
	 * @param user
	 * 			user to be removed as an editor
	 */
	public void removeEditor(User user){
		editors.remove(user);
	}
	
	public void changeSummary(String summary){
		this.summary = summary;
	}
	
	public String getSummary(){
		return this.summary;
	}
	
	public void setSummary(String summary){
		this.summary = summary + " on " + timestamp.toString();
	}

}
