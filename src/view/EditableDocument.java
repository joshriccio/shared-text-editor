package view;

import javax.swing.text.StyledDocument;

import model.User;

import java.io.Serializable;
import java.sql.Timestamp;

public class EditableDocument implements Serializable {

	private StyledDocument document;
	private Timestamp timestamp;
	private User documentOwner;
	
	// Constructor
	public EditableDocument( StyledDocument doc, User ownr ) {
		document = doc;
		documentOwner = ownr;
		generateNewTimeStamp();
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

}
