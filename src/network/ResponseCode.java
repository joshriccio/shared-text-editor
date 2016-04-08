package network;

import java.io.Serializable;

/**
 * ResponseCode is designed to processing Server responses easier
 * 
 * @author Cody Deeran (cdeeran11@email.arizona.edu)
 */
public enum ResponseCode implements Serializable {

	LOGIN_SUCCESSFUL(1), LOGIN_FAILED(2), ACCOUNT_CREATED_SUCCESSFULLY(3), ACCOUNT_CREATION_FAILED(4), DOCUMENT_SENT(5);

	/**
	 * Constructor used to build ResponseCode Enum
	 */
	ResponseCode(int responseCode) {
	}
}
