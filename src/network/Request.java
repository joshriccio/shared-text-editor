package network;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import model.User;
/**
 * id 1 = login request
 * id 2 = create new user request
 * id 3 = update document
 * @author Josh
 *
 */
public class Request implements Serializable{
	
	int id;
	User user;
	StyledDocument doc;

	public Request(int id) {
		this.id = id;
	}
	
	public int getRequestType(){
		return id;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public void setDocumnt(StyledDocument doc){
		this.doc = doc;
	}

}
