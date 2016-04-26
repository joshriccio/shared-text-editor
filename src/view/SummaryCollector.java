package view;

public class SummaryCollector {

	private String username;

	private boolean boldEvent;
	private boolean italicEvent;
	private boolean underLineEvent;
	private boolean fontSizeEvent;
	private boolean fontColorEvent;
	private boolean fontEvent;
	private boolean bulletEvent;

	public SummaryCollector(String username) {
		this.username = username;

		this.boldEvent = false;
		this.italicEvent = false;
		this.underLineEvent = false;
		this.fontSizeEvent = false;
		this.fontColorEvent = false;
		this.fontEvent = false;
		this.bulletEvent = false;
	}

	public void boldEvent() {
		this.boldEvent = true;
	}

	public void italicEvent() {
		this.italicEvent = true;
	}

	public void underLineEvent() {
		this.underLineEvent = true;
	}

	public void fontSizeEvent() {
		this.fontSizeEvent = true;
	}

	public void fontColorEvent() {
		this.fontColorEvent = true;
	}

	public void fontEvent() {
		this.fontEvent = true;
	}
	
	public void bulletEvent() {
		this.bulletEvent = true;
	}

	public void reset() {
		this.boldEvent = false;
		this.italicEvent = false;
		this.underLineEvent = false;
		this.fontSizeEvent = false;
		this.fontColorEvent = false;
		this.fontEvent = false;
		this.bulletEvent = false;
	}

	public String getSummary() {
		String summary = username;
		if (boldEvent) {
			summary = summary + " added bold text,";
		} if (italicEvent) {
			summary = summary + " added italic text,";
		} if (underLineEvent) {
			summary = summary + " added underline text,";
		} if (fontSizeEvent) {
			summary = summary + " changed font size,";
		} if (fontColorEvent) {
			summary = summary + " changed font color,";
		} if (fontEvent) {
			summary = summary + " changed font,";
		} if (bulletEvent) {
			summary = summary + " added list,";
		} else{
			summary = summary + " made changes,";
		}
			reset();
			return summary;
	}

}
