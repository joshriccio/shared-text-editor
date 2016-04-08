package model;

import java.io.Serializable;

/**
 * This class is designed to handle user account interactions. A unique
 * username, ID and password, the ability to change their password and reset a
 * forgotten one, and keep track of documents that are owned and able to be
 * edited by the user
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 *
 */
@SuppressWarnings("serial")
public class User implements Serializable {

	private String username;
	private String password;

	/**
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @return returns the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return returns the password
	 */
	public String getPassword() {
		return password;
	}
}
