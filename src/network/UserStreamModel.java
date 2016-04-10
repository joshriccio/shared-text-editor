package network;

import java.io.ObjectOutputStream;

import model.User;

/**
 * Wraps the user to their objectOutputStream with their current online status.
 * @author Joshua Riccio
 *
 */
public class UserStreamModel {

	private User user;
	private ObjectOutputStream oos;
	private boolean online;
	
	/**
	 * Constructor
	 * @param user the current user
	 * @param oos the users ObjectOutputStream
	 */
	public UserStreamModel(User user, ObjectOutputStream oos) {
		this.user = user;
		this.oos = oos;
		online = false;
	}
	
	/**
	 * Gets the user
	 * @return returns the user
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * Gets the oos
	 * @return returns the users oos
	 */
	public ObjectOutputStream getOuputStream(){
		return oos;
	}
	
	/**
	 * Updates the users oos
	 * @param oos the objectOutputStream
	 */
	public void setOutputStream(ObjectOutputStream oos){
		this.oos = oos;
	}
	
	/**
	 * Toggles user online status
	 */
	public void toggleOnline(){
		this.online = !this.online;
	}
	
	/**
	 * Returns the users online status
	 * @return returns the users online status
	 */
	public boolean isOnline(){
		return this.online;
	}

}
