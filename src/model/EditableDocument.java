package model;

import javax.swing.text.StyledDocument;

import java.io.Serializable;
import java.sql.Timestamp;

public class EditableDocument implements Serializable, Comparable<Timestamp> {

	private StyledDocument document;
	private Timestamp timestamp;
	private User documentOwner = null;
	
	// Constructor
	public EditableDocument( StyledDocument doc, User ownr ) {
		document = doc;
		documentOwner = ownr;
		generateNewTimeStamp();
	}
	
	// Constructor for testing
	public EditableDocument(StyledDocument doc) {
		document = doc;
	}

	// Receive changed data from GUI, update.
	public void setDocumentContent(StyledDocument newDoc) {
		document = newDoc;
		generateNewTimeStamp();
	}
	
	// Gets the precise current time and stamps it.
	public void generateNewTimeStamp() {
		timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	// Return most recent document
	public StyledDocument getDocument() {
		return document;
	}
	
	// Return most recent timestamp
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	// Return document's owner
	public User getDocumentOwner() {
		return documentOwner;
	}

	// Return (+) if THIS doc is MORE RECENT than other
	@Override
	public int compareTo(Timestamp other) {
		return timestamp.compareTo(other);
	}

}
