package network;

import java.io.Serializable;

/**
 * RequestCode's represents the codes used to send messages to the server
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 *
 */
public enum RequestCode implements Serializable {
	LOGIN(1), CREATE_ACCOUNT(2), RESET_PASSWORD(3), 
	SIGN_OUT(4), DOCUMENT_SENT(5), REQUEST_DOCUMENT(6), 
	GET_USER_LIST(7), USER_EXITING(8), START_DOCUMENT_STREAM(9), 
	REQUEST_DOCUMENT_LIST(10), ADD_USER_AS_EDITOR(11);

	/**
	 * Constructor,used to build a RequestCode enum
	 */
	RequestCode(int requestCode) {
	}
}
