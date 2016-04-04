package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class EditorGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea = new JTextArea(5,10);

	/**
	 *  Constructor
	 */
	public EditorGui() {
		
		//Set Frame 	
		this.setTitle("Collaborative Editing");
		this.setSize(1350, 700);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setFont(new Font("Courier New", Font.ITALIC, 12));
		
		//initialize the text area
		setTextArea();
	}

	/**
	 * Initialize JFrame and set visible
	 * 
	 * @param args
	 *            input
	 */
	public static void main(String[] args) {
		JFrame editorGUI = new EditorGui();
		editorGUI.setVisible(true);
		
	}	
	
	/**
	 * This method sets up the text area. 
	 */
	public void setTextArea(){
		 textArea.setPreferredSize(new Dimension(100,100));
		textArea.setBackground(Color.WHITE);
	    textArea.setLineWrap(true);
	    textArea.setWrapStyleWord(false);

	    this.add(textArea, BorderLayout.CENTER);
	}

}
