package network;

import java.io.ObjectOutputStream;

import model.User;

public class UserStreamModel {

	private User user;
	private ObjectOutputStream oos;
	private boolean online;
	
	public UserStreamModel(User user, ObjectOutputStream oos) {
		this.user = user;
		this.oos = oos;
		online = false;
	}
	
	public User getUser(){
		return user;
	}
	
	public ObjectOutputStream getOuputStream(){
		return oos;
	}
	
	public void setOutputStream(ObjectOutputStream oos){
		this.oos = oos;
	}
	
	public void toggleOnline(){
		this.online = !this.online;
	}
	
	public boolean isOnline(){
		return this.online;
	}

}
