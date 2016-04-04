package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import org.junit.Test;
import model.Toolbar;

public class ToolbarTests {
	
	private Toolbar testBar = new Toolbar();
	
	public ToolbarTests() {
		testToolbarGettersAndSetters();
	}
	
	@Test
	public void testToolbarGettersAndSetters() {
		Font fnt = new Font("Helvetica", 0, 14);
		Font fnt2 = new Font("Times New Roman", 0, 16);
		
		testBar.setColor(Color.BLACK);
		testBar.setFont(fnt);
		testBar.setFontSize(16);
		testBar.setIsBold(false);
		testBar.setIsItalic(false);
		testBar.setIsUnderlined(false);
		
		assertEquals(fnt, testBar.getFont());
		assertEquals(Color.BLACK, testBar.getColor());
		assertEquals(16, testBar.getFontSize());
		assertFalse(testBar.isBold());
		assertFalse(testBar.isItalic());
		assertFalse(testBar.isUnderlined());
		
		testBar.setFont(fnt2);
		assertEquals(fnt2, testBar.getFont());
		
		testBar.setColor(Color.GREEN);
		assertEquals(Color.GREEN, testBar.getColor());
		
		testBar.setFontSize(24);
		assertEquals(24, testBar.getFontSize());
		
		testBar.setIsBold(true);
		testBar.setIsItalic(true);
		testBar.setIsUnderlined(true);
		assertTrue(testBar.isBold());
		assertTrue(testBar.isItalic());
		assertTrue(testBar.isUnderlined());
		
	}
}
