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
	
	private ResponseCode responseCode;
	private EditableDocument doc;

	public Response(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	
	public Response(ResponseCode responseCode, EditableDocument doc) {
		this.responseCode = responseCode;
		this.doc = doc;
	}
	
	public int getResponseID(){
		return responseCode.getResponseCode();
	}
	
	public EditableDocument getDoc(){
		return doc;
	}
	

}
