package network;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import model.EditableDocument;

/**
 * responseid 1 = authenticated
 * responseid 2 = auth failed
 * responseid 3 = account creation success
 * responseid 4 = account creation failed
 * responseid 5 = sent document
 * @author Josh
 *
 */
public class Response implements Serializable{
	
	private int responseID;
	private EditableDocument doc;

	public Response(int responseID) {
		this.responseID = responseID;
	}
	
	public Response(int responseID, EditableDocument doc) {
		this.responseID = responseID;
		this.doc = doc;
	}
	
	public int getResponseID(){
		return responseID;
	}
	
	public EditableDocument getDoc(){
		return doc;
	}
	

}
