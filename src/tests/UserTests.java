package tests;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.swing.JTextPane;

import org.junit.Test;

import model.EditableDocument;
import model.Password;
import model.User;

/**
 * 
 * @author Joshua Riccio
 *
 */
public class UserTests {
	
	@Test
	public void usertestpass() throws NoSuchAlgorithmException, NoSuchProviderException {
		User user1 = new User("Josh", "123");
		assertEquals(user1.getPassword(), Password.generateSecurePassword("123", user1.getSalt()));
	}
	
	@Test
	public void usertestname() throws NoSuchAlgorithmException, NoSuchProviderException {
		User user1 = new User("Josh", "123");
		assertEquals(user1.getUsername(), "Josh");
	}
	
	@Test
	public void userSetPass() throws NoSuchAlgorithmException, NoSuchProviderException {
		User user1 = new User("Josh", "123");
		user1.setPassword("abc");
		assertEquals(user1.getPassword(), "abc");
	}
	
	@Test
	public void userSetSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
		User user1 = new User("Josh", "123");
		user1.setSalt("abc");
		assertEquals(user1.getSalt(), "abc");
	}
	
	@Test
	public void userOwnership() throws NoSuchAlgorithmException, NoSuchProviderException {
		User user1 = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), user1, "doc1");
		user1.addAsOwner(doc);
		assertTrue(user1.getOwnedDocuments().contains(doc));
		user1.removeAsOwner(doc);
		assertFalse(user1.getOwnedDocuments().contains(doc));
		user1.addAsEditor(doc);
		assertTrue(user1.getEditableDocuments().contains(doc));
		user1.removeAsEditor(doc);
		assertFalse(user1.getEditableDocuments().contains(doc));
	}
	

}
