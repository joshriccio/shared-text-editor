package network;

import java.io.Serializable;

/**
 * ResponseCode is designed to processing Server responses easier
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 */
public enum ResponseCode implements Serializable {

	LOGIN_SUCCESSFUL(1), LOGIN_FAILED(2), ACCOUNT_CREATED_SUCCESSFULLY(3), ACCOUNT_CREATION_FAILED(4), DOCUMENT_SENT(5), 
	ACCOUNT_RESET_PASSWORD_SUCCESSFUL(6), ACCOUNT_RESET_PASSWORD_FAILED(7), CORRECT_USERNAME(8), USER_LIST_SENT(9), 
	DOCUMENT_LISTS_SENT(10), USER_ADDED(11), USER_NOT_ADDED(12);

	/**
	 * Constructor used to build ResponseCode Enum
	 */
	ResponseCode(int responseCode) {
	}
}
