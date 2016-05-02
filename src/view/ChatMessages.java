package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

/**
 * This class handles the data for the Chat Tab in the editorGUI
 * 
 * @author Stevo
 *
 */
public class ChatMessages extends JPanel {

	private static final long serialVersionUID = 1L;
	JTextPane messages;
	JScrollPane scrollpane;

	/**
	 * The constructor for the messages window
	 */

	public ChatMessages() {
		this.messages = new JTextPane();
		this.messages.setEditable(false);
		this.messages.setBackground(Color.white);
		this.messages.setPreferredSize(new Dimension(1000, 350));
		this.scrollpane = new JScrollPane(this.messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(this.scrollpane, BorderLayout.CENTER);
		DefaultCaret caret = (DefaultCaret) messages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	/**
	 * Sets the text for the message window
	 * 
	 * @param text the text to be added to the window
	 */
	public void setText(String text) {
		this.messages.setText(text);
	}
	
	
	/**
	 * Return the messages in the chatTab
	 * 
	 * @return	JTextPane that contains all messages in chatTab
	 */
	public JTextPane getTextPane(){
		return this.messages;
	}
}

