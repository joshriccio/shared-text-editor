package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * The class converts the password into a hashed form
 * @author Brittany 
 *
 */
public class Password {

	/**
	 * This method generates a secure password
	 * 
	 * @param password
	 *            The password that is not hashed
	 * @param salt
	 *            The salt value for the user
	 * @return a secure password
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static String generateSecurePassword(String pass, String salt)
			throws NoSuchAlgorithmException, NoSuchProviderException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(salt.getBytes());
		// Get the hash's bytes
		byte[] bytes = md.digest(pass.getBytes());
		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	/**
	 * This method generates a salt value.
	 * 
	 * @return a salted string
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static String generateSaltValue() throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		byte[] saltVal = new byte[16];
		sr.nextBytes(saltVal);
		return saltVal.toString();
	}

}