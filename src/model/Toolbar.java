package model;

import java.awt.Color;
import java.awt.Font;

public class Toolbar {
	private Font f;
	private int fontSize;
	private Color color;
	private boolean isBold = false;
	private boolean isItalic = false;
	private boolean isUnderlined = false;

	/**
	 * This method sets the font
	 * 
	 * @param font
	 *            font to set to current font
	 */
	public void setFont(Font font) {
		this.f = font;
	}

	/**
	 * This method sets the font size
	 * 
	 * @param fontSize
	 *            font size to set to current font size
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * This method sets the color of the text
	 * 
	 * @param color
	 *            color to set to the current text color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * This method changes the bold boolean
	 * 
	 * @param b
	 *            boolean that tells if text is bold
	 */
	public void setIsBold(boolean b) {
		this.isBold = b;
	}

	/**
	 * This method changes the Italic boolean
	 * 
	 * @param b
	 *            boolean that tells if text is italic
	 */
	public void setIsItalic(boolean b) {
		this.isItalic = b;
	}

	/**
	 * This method changes the Underline boolean
	 * 
	 * @param b
	 *            boolean that tells if the text is underlined
	 */
	public void setIsUnderlined(boolean b) {
		this.isUnderlined = b;
	}

	/**
	 * This method returns the font
	 * 
	 * @return the current font
	 */
	public Font getFont() {
		return this.f;
	}

	/**
	 * This method returns the font size
	 * 
	 * @return the current font size
	 */
	public int getFontSize() {
		return this.fontSize;
	}

	/**
	 * This method returns the current color
	 * 
	 * @return the current color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * This method returns the boolean if the text is bold
	 * 
	 * @return boolean if text is bold
	 */
	public boolean isBold() {
		return isBold;
	}

	/**
	 * This method returns the boolean if the text is italic
	 * 
	 * @return boolean if text is italic
	 */
	public boolean isItalic() {
		return isItalic;
	}

	/**
	 * This method returns the boolean if the text is underlined
	 * 
	 * @return boolean if the text is underlined
	 */
	public boolean isUnderlined() {
		return isUnderlined;
	}

}
