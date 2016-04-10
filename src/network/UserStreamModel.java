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
	
	public ObjectOutputStream getOuput(){
		return oos;
	}
	
	public void setoos(ObjectOutputStream oos){
		this.oos = oos;
	}
	
	public void toggleOnline(){
		this.online = !this.online;
	}
	
	public boolean getOnlineStatus(){
		return this.online;
	}

}
