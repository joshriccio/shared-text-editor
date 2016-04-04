package model;

public class DocChars {
	
	public char asciiValue;			// PUBLIC FIXME: Might be easier to leave this public
	private boolean isBold = false;
	private boolean isItalic = false;
	private boolean isUnderlined = false;
	
	public DocChars(char value) {
		asciiValue = value;
	}
	
	public boolean isBold() {
		return isBold;
	}
	
	public boolean isItalic() {
		return isItalic;
	}
	
	public boolean isUnderlined() {
		return isUnderlined;
	}

}
