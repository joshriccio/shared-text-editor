package model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
	private  String salt;

	/**
	 * Costructor for the user class, assigns both the username and password
	 * to the Strings passed in.
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 */
	public User(String username, String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.username = username;
		this.salt = Password.generateSaltValue();
		this.password = Password.generateSecurePassword(password, this.salt);
	}

	/**
	 * This retures the username for the respective user
	 * @return returns the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This returns the password for the respective user
	 * @return returns the password
	 */
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSalt(String inSalt){
		this.salt = inSalt;
	}
	
	public String getSalt(){
		return this.salt;
	}
}
