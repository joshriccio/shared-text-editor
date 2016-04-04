package model;

import java.io.Serializable;

/**
 * This class is designed to handle user account interactions.
 * 
 * Such as:
 * 
 * A unique username, ID and password
 * 
 * The ability to change their password and reset a forgotten one
 * 
 * Keep track of documents that are owned and able to be edited by the user
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 *
 */
public class User implements Serializable{
	
	private String username;
	
	private String password;

	public User(String username, String password){
		this.username = username;
		this.password = password;
	}

	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
}// end User class
