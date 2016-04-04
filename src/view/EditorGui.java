package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
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

	private JTextArea textArea = new JTextArea();

	// set up JtoolBar with buttons and drop downs
	private JToolBar javaToolBar = new JToolBar();
	private JButton boldFontButton;
	private JButton italicFontButton;
	private JButton underlineFontButton;
	private Integer[] fontSizes = { 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 26, 48, 72 };
	private JComboBox sizeFontDropDown = new JComboBox(fontSizes);
	private String fonts[] =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private JComboBox fontDropDown = new JComboBox(fonts);

	private Toolbar myToolBar = new Toolbar();

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

		// add listeners to buttons and drop boxes
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

	/**
	 * This method sets up the tool bar.
	 */
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

		// add buttons to the tool bar
		javaToolBar.add(boldFontButton);
		javaToolBar.add(italicFontButton);
		javaToolBar.add(underlineFontButton);

		// add drop down menus to the tool bar
		javaToolBar.addSeparator();
		javaToolBar.add(sizeFontDropDown);
		javaToolBar.addSeparator();
		javaToolBar.add(fontDropDown);

		// add the tool bar to the frame
		this.add(javaToolBar, BorderLayout.NORTH);

	}

	/**
	 * This method adds listeners to the buttons and drop down boxes on the tool
	 * bar
	 */
	public void setButtonListeners() {
		boldFontButton.addActionListener(new boldListener());
		italicFontButton.addActionListener(new italicListener());
		underlineFontButton.addActionListener(new underlineListener());
		sizeFontDropDown.addActionListener(new sizeFontDropDownListener());
		fontDropDown.addActionListener(new fontDropDownListener());
	}

	private class boldListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!myToolBar.isBold()) {
				myToolBar.setIsBold(true);
			} else {
				myToolBar.setIsBold(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String selectedText = textArea.getSelectedText();

				// TODO: set selected text to bold on document
			}

		}
	}

	private class italicListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!myToolBar.isItalic()) {
				myToolBar.setIsItalic(true);
			} else {
				myToolBar.setIsItalic(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String selectedText = textArea.getSelectedText();

				// TODO: set selected text to Italic on document
			}

		}
	}

	private class underlineListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (!myToolBar.isUnderlined()) {
				myToolBar.setIsUnderlined(true);
			} else {
				myToolBar.setIsUnderlined(false);
			}

			// text is selected
			if (textArea.getSelectedText() != null) {
				String selectedText = textArea.getSelectedText();

				// TODO: set selected text on document to Underlined
			}
		}
	}

	private class sizeFontDropDownListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Integer fontSize = (int) sizeFontDropDown.getSelectedItem();
			myToolBar.setFontSize(fontSize);

			if (textArea.getSelectedText() != null) {
				String selectedText = textArea.getSelectedText();

				// TODO: change selected text size on document
			}
		}

	}

	private class fontDropDownListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String stringFont = (String) fontDropDown.getSelectedItem();
			Font f;

			if (myToolBar.isBold() && myToolBar.isItalic()) {
				f = new Font(stringFont, Font.BOLD + Font.ITALIC, myToolBar.getFontSize());
				myToolBar.setFont(f);
			}

			if (myToolBar.isBold()) {
				f = new Font(stringFont, Font.BOLD, myToolBar.getFontSize());
				myToolBar.setFont(f);
			}

			if (myToolBar.isItalic()) {
				f = new Font(stringFont, Font.ITALIC, myToolBar.getFontSize());
				myToolBar.setFont(f);
			}

			else {
				f = new Font(stringFont, Font.PLAIN, myToolBar.getFontSize());
			}
			

			if (textArea.getSelectedText() != null) {
				String selectedText = textArea.getSelectedText();

				// TODO: change selected text font on document
			}
		}

	}

}
