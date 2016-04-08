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

	/**
	 * Constructor
	 * 
	 * @param doc
	 *            document to be added to wrapper
	 * @param ownr
	 *            owner of the document
	 */
	public EditableDocument(StyledDocument doc, User ownr) {
		document = doc;
		documentOwner = ownr;
		generateNewTimeStamp();
	}

	/**
	 * Constructor used for testing
	 * 
	 * @param doc
	 *            document to be changed in the wrapper
	 */
	public EditableDocument(StyledDocument doc) {
		document = doc;
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
