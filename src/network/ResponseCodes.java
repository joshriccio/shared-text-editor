package network;

import java.io.Serializable;

/**
 * This class is designed to processing Server responses easier
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 */
public enum ResponseCodes implements Serializable{
	
	LOGIN_SUCCESSFUL(1), LOGIN_FAILED(2), ACCOUNT_CREATED_SUCCESSFULLY(3), ACCOUNT_CREATION_FAILED(4), DOCUMENT_SENT(5);
	
	private int responseCode;
	
	/**
	 * This is the constructor determining what the response code will be
	 * @param responseCode
	 * 			The integer passed in to determine the response code
	 */
	ResponseCodes(int responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * This gets the integer related to the responseCode
	 * 
	 * @return the integer related to the responseCode
	 */
	public int getResponseCode() {
		return this.responseCode;
	}
	/**
	 * This method returns the string version of the responseCodes
	 * 
	 * @return The String version of the responseCodes
	 */
	public String getResponseCodeString() {
		if (getResponseCode() == 1) {
			return "Attempt to login was SUCCESSFUL!";
		}
		else if (getResponseCode() == 2) {
			return "Attempt to login FAILED!";
		} // end if
		else if (getResponseCode() == 3) {
			return "Account has been created SUCCESSFULLY!";
		} // end else if
		else if (getResponseCode() == 4) {
			return "FAILED to create account!";
		} // end else if
		else {
			return "Document sent!";
		} // end else if
	}
}
