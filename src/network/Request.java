package network;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import model.EditableDocument;
import model.User;
/**
 * id 1 = login request
 * id 2 = create new user request
 * id 3 = update document
 * @author Josh
 *
 */
public class Request implements Serializable{
	
	RequestCode requestCode;
	User user;
	EditableDocument doc;

	public Request(RequestCode requestCode) {
		this.requestCode = requestCode;
	}
	
	public int getRequestType(){
		return requestCode.getRequestCode();
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public void setDocument(EditableDocument doc){
		this.doc = doc;
	}

}
