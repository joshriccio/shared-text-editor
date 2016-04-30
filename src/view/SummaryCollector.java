package view;

/**
 * The summary collector that generates automated summary based on user
 * interactions @author Joshua Riccio
 *
 */
public class SummaryCollector{

	private String username;
	private boolean boldEvent;
	private boolean italicEvent;
	private boolean underLineEvent;
	private boolean fontSizeEvent;
	private boolean fontColorEvent;
	private boolean fontEvent;
	private boolean bulletEvent;

	/**
	 * The constructor that builds a new Summary Collector @param username
	 */
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

	/**
	 * When a user clicks bold
	 */
	public void boldEvent() {
		this.boldEvent = true;
	}

	/**
	 * When a user clicks italic
	 */
	public void italicEvent() {
		this.italicEvent = true;
	}

	/**
	 * When a user clicks underline
	 */
	public void underLineEvent() {
		this.underLineEvent = true;
	}

	/**
	 * When a user changes font size
	 */
	public void fontSizeEvent() {
		this.fontSizeEvent = true;
	}

	/**
	 * When a user changes font color
	 */
	public void fontColorEvent() {
		this.fontColorEvent = true;
	}

	/**
	 * When a user changes font face
	 */
	public void fontEvent() {
		this.fontEvent = true;
	}

	/**
	 * When a user adds a bullet list
	 */
	public void bulletEvent() {
		this.bulletEvent = true;
	}

	/**
	 * Resets all summary values back to false;
	 */
	public void reset() {
		this.boldEvent = false;
		this.italicEvent = false;
		this.underLineEvent = false;
		this.fontSizeEvent = false;
		this.fontColorEvent = false;
		this.fontEvent = false;
		this.bulletEvent = false;
	}

	/**
	 * Gets the automated summary in the form of a string @return the summary
	 */
	public String getSummary() {
		String summary = username;
		if (boldEvent) {
			summary = summary + " added bold text,";
		}
		if (italicEvent) {
			summary = summary + " added italic text,";
		}
		if (underLineEvent) {
			summary = summary + " added underline text,";
		}
		if (fontSizeEvent) {
			summary = summary + " changed font size,";
		}
		if (fontColorEvent) {
			summary = summary + " changed font color,";
		}
		if (fontEvent) {
			summary = summary + " changed font,";
		}
		if (bulletEvent) {
			summary = summary + " added list,";
		} else {
			summary = summary + " made changes,";
		}
		reset();
		return summary;
	}
}
