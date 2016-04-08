package network;

import java.io.Serializable;

/**
 * This class is designed to make processing Client requests easier
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 *
 */
public enum RequestCode implements Serializable {
	LOGIN(1), CREATE_ACCOUNT(2), RESET_PASSWORD(3), SIGN_OUT(4),DOCUMENT_SENT(5);
	
	private int requestCode;
	/**
	 * This is the constructor determining what the request code will be
	 * @param requestCode
	 * 			The integer passed in to determine the request code
	 */
	RequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	/**
	 * This gets the integer related to the request code
	 * 
	 * @return the integer related to the request code
	 */
	public int getRequestCode(){
		return this.requestCode;
	}
}
