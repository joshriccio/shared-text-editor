package tests;

import static org.junit.Assert.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import org.junit.Test;
import model.EditableDocument;
import model.LinkedListForSaves;
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
	
	@Test
	public void docTest4() {
		LinkedListForSaves list = new LinkedListForSaves();
		User usr = null;
		User usr1 = null;
		try {
			usr = new User("Josh", "123");
			usr1 = new User("Cody", "456");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		JTextPane jtp = new JTextPane();
		EditableDocument doc1 = new EditableDocument(jtp.getStyledDocument(), usr, "doc1");
		EditableDocument doc2 = new EditableDocument(jtp.getStyledDocument(), usr, "doc2");
		EditableDocument doc3 = new EditableDocument(jtp.getStyledDocument(), usr, "doc3");
		list.createSave(doc1, "doc1234", usr);
		list.createSave(doc1, "doc12345", usr);
		list.createSave(doc2, "doc12345", usr);
		list.createSave(doc3, "doc12345", usr);
		assertTrue(list.getMostRecentSave(doc1.getName()).equals("doc12345"));
		String[] editables = list.getDocumentsByEditor("Josh");
		assertEquals(editables[0],"doc3");
		String[] ownables = list.getDocumentsByOwner("Josh");
		assertEquals(ownables[0],"doc3");
		list.addUserAsEditor("Cody", "doc1");
		
		ArrayList<String> revisions = list.getRevisionHistroy("doc1");
		assertEquals(revisions.get(0), doc1.getSummary());
		String summary = list.getOldSave("doc1", doc1.getSummary());
		assertEquals(summary, list.getOldSave("doc1", doc1.getSummary()));
	}
	
	@Test
	public void docTest5() {
		User usr1 = null;
		try {
			usr1 = new User("Josh", "123");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		JTextPane jtp = new JTextPane();
		EditableDocument doc1 = new EditableDocument(jtp.getStyledDocument(), usr1, "doc1");
		doc1.setSummary("New changes");
		doc1.changeSummary("Old summary");
		assertEquals(doc1.getSummary(), "Old summary");
	}
}
