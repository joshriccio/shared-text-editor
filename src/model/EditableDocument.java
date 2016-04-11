package model;

import javax.swing.text.StyledDocument;

import java.io.Serializable;
import java.sql.Timestamp;

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
	}
	
	
	/**
	 * This method is used to get the String value of the name of the EditableDocument
	 * @return
	 * 		String EditableDocument.name
	 */
	public String getName() {
		return name;
	}
	
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

}
