package tests;

import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import org.junit.Test;
import model.EditableDocument;
import model.User;

/**
 * This class tests the functionality of the EditorDocument and it's
 * dependancies
 * 
 * @author Joshua Riccio
 *
 */
public class EditorTests {

	@Test
	public void EditableDocTest1() throws NoSuchAlgorithmException, NoSuchProviderException {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr, "doc1");
		EditableDocument doc2 = new EditableDocument(jtp.getStyledDocument(), "doc2");

		try {
			doc2.getDocument().insertString(0, "Test", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		doc.setDocumentContent(doc2.getDocument());
		try {
			assertEquals(doc.getDocument().getText(0, doc.getDocument().getLength()), "Test");
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void EditableDocTestOwner() throws NoSuchAlgorithmException, NoSuchProviderException {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr, "doc1");
		assertEquals(doc.getDocumentOwner(), usr);
	}

	@Test
	public void EditableDocTestTimestamp() throws NoSuchAlgorithmException, NoSuchProviderException {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr, "doc1");
		EditableDocument doc2 = new EditableDocument(jtp.getStyledDocument(), usr, "doc2");

		assertTrue(doc.getTimestamp().compareTo(doc2.getTimestamp()) == 0);
		assertTrue(doc.compareTo(doc2.getTimestamp()) == 0);
	}
	
	@Test
	public void editableDocOwner() throws NoSuchAlgorithmException, NoSuchProviderException {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr, "doc1");
		assertEquals(doc.getName(), "doc1");
		
		doc.addOwner(usr);
		assertTrue(doc.getOwners().get(0).equals(usr));
		doc.removeOwner(usr);
		assertFalse(doc.getOwners().size() == 1);
		
		doc.addEditor(usr);
		assertTrue(doc.getEditors().get(0).equals(usr));
		doc.removeEditor(usr);
		assertFalse(doc.getEditors().size() == 1);
	}
}
