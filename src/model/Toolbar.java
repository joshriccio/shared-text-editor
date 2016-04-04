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

	public void setFont(Font font) {
		this.f = font;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setIsBold(boolean b){
		this.isBold = b;
	}
	
	public void setIsItalic(boolean b){
		this.isItalic = b;
	}
	
	public void setIsUnderlined(boolean b){
		this.isUnderlined = b;
	}
	
	public Font getFont() {
		return this.f;
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public Color getColor() {
		return this.color;
	}

	public boolean isBold(){
		return isBold;
	}
	
	public boolean isItalic(){
		return isItalic;
	}
	
	public boolean isUnderlined(){
		return isUnderlined;
	}
	
	
}
