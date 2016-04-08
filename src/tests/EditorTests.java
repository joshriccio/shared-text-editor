package tests;

import static org.junit.Assert.*;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import org.junit.Test;
import model.EditableDocument;
import model.Toolbar;
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
	public void usertestpass() {
		User user1 = new User("Josh", "123");
		assertEquals(user1.getPassword(), "123");
	}

	@Test
	public void usertestname() {
		User user1 = new User("Josh", "123");
		assertEquals(user1.getUsername(), "Josh");
	}

	@Test
	public void toolbartestColor() {
		Toolbar tb = new Toolbar();
		tb.setColor(Color.RED);
		assertEquals(tb.getColor(), Color.RED);
	}

	@Test
	public void EditableDocTest1() {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr);
		EditableDocument doc2 = new EditableDocument(jtp.getStyledDocument());

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
	public void EditableDocTestOwner() {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr);
		assertEquals(doc.getDocumentOwner(), usr);
	}

	@Test
	public void EditableDocTestTimestamp() {
		User usr = new User("Josh", "123");
		JTextPane jtp = new JTextPane();
		EditableDocument doc = new EditableDocument(jtp.getStyledDocument(), usr);
		EditableDocument doc2 = new EditableDocument(jtp.getStyledDocument(), usr);

		assertTrue(doc.getTimestamp().compareTo(doc2.getTimestamp()) == 0);
		assertTrue(doc.compareTo(doc2.getTimestamp()) == 0);
	}
}
