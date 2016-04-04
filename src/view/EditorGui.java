package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import model.Toolbar;

public class EditorGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextArea textArea = new JTextArea(5, 10);

	// set up JtoolBar with buttons
	private JToolBar toolBar = new JToolBar();
	private JButton boldFontButton;
	private JButton italicFontButton;
	private JButton underlineFontButton;
	private JComboBox sizeFontDropDown = new JComboBox(
			new String[] { "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" });
	private JComboBox fontDropDown = new JComboBox(new String[] { "Sans Serif", "Times New Roman", "Add more fonts" });

	private Toolbar tBar = new Toolbar();

	/**
	 * Constructor
	 */
	public EditorGui() {

		// Set Frame
		this.setTitle("Collaborative Editing");
		this.setSize(1350, 700);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setFont(new Font("Courier New", Font.ITALIC, 12));

		// initialize the text area
		setTextArea();

		// initialize the JToolbar
		setJToolBar();

		// add listeners to buttons
		setButtonListeners();
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
	public void setTextArea() {
		textArea.setPreferredSize(new Dimension(100, 100));
		textArea.setBackground(Color.WHITE);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(false);

		// Outlined text area with a border
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textArea.setBorder(
				BorderFactory.createCompoundBorder(borderOutline, BorderFactory.createEmptyBorder(100, 100, 100, 100)));

		// add text are to JFrame
		this.add(textArea, BorderLayout.CENTER);
	}

	public void setJToolBar() {
		// initialize images
		Image boldImage = null;
		Image italicImage = null;
		Image underlineImage = null;

		// load images
		try {
			boldImage = ImageIO.read(new File("./images/boldImage.png"));
			italicImage = ImageIO.read(new File("./images/italicImage.png"));
			underlineImage = ImageIO.read(new File("./images/underlineImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't load an image on the toolbar");
		}

		// set buttons with icons in them
		boldFontButton = new JButton(new ImageIcon(boldImage));
		italicFontButton = new JButton(new ImageIcon(italicImage));
		underlineFontButton = new JButton(new ImageIcon(underlineImage));

		// add buttons to the toolbar
		toolBar.add(boldFontButton);
		toolBar.add(italicFontButton);
		toolBar.add(underlineFontButton);
		toolBar.addSeparator();
		toolBar.add(sizeFontDropDown);
		toolBar.addSeparator();
		toolBar.add(fontDropDown);

		this.add(toolBar, BorderLayout.NORTH);

	}

	public void setButtonListeners() {
		boldFontButton.addActionListener(new boldListener());
		italicFontButton.addActionListener(new italicListener());
		underlineFontButton.addActionListener(new underlineListener());
		sizeFontDropDown.addActionListener(new sizeFontDropDownListener());
		fontDropDown.addActionListener(new fontDropDownListener());
	}

	private class boldListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!tBar.isBold()) {
				tBar.setIsBold(true);
			} else {
				tBar.setIsBold(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String text = textArea.getSelectedText();
				// setBold
			}

		}
	}

	private class italicListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!tBar.isItalic()) {
				tBar.setIsItalic(true);
			} else {
				tBar.setIsItalic(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String text = textArea.getSelectedText();
				// set Italic
			}

		}
	}

	private class underlineListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!tBar.isUnderlined()) {
				tBar.setIsUnderlined(true);
			} else {
				tBar.setIsUnderlined(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String text = textArea.getSelectedText();
				// set to Underlined
			}
		}
	}

	private class sizeFontDropDownListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int fontSize = (int) sizeFontDropDown.getSelectedItem();
			tBar.setFontSize(fontSize);
		}

	}
	
	private class fontDropDownListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Font font = (Font) fontDropDown.getSelectedItem();
			tBar.setFont(font);
		}
		
	}

}
