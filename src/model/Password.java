package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import model.User;

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
	public static String generateSecurePassword(User user, String salt)
			throws NoSuchAlgorithmException, NoSuchProviderException {

		// salt is null when an account is being created and the user doesn't
		// have a salt value yet.
		if (salt == null) {
			salt = generateSaltValue();
			user.setSalt(salt);
		}

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(salt.getBytes());
		// Get the hash's bytes
		byte[] bytes = md.digest(user.getPassword().getBytes());
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