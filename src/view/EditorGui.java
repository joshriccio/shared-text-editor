package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import model.EditableDocument;
import model.ToolBar;
import model.User;
import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;

/**
 * 
 * @author Brittany, Josh, Steven, Cody
 *
 */
public class EditorGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextPane textpane = new JTextPane();
	// set up JtoolBar with buttons and drop downs
	private JToolBar javaToolBar = new JToolBar();
	private JButton boldFontButton;
	private JButton italicFontButton;
	private JButton underlineFontButton;
	private JButton colorButton;
	private Integer[] fontSizes = { 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 26, 48, 72 };
	private JComboBox sizeFontDropDown = new JComboBox(fontSizes);
	private String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private JComboBox fontDropDown = new JComboBox(fonts);
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private User user;
	private ToolBar myToolBar = new ToolBar();

	/**
	 * Constructor
	 */
	public EditorGui(ObjectOutputStream oos, ObjectInputStream ois, User user) {
		this.oos = oos;
		this.ois = ois;
		this.user = user;
		ServerListener serverListener = new ServerListener();
        serverListener.start();
        
		// Set Frame
		this.setTitle("Collaborative Editing");
		this.setSize(1350, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFont(new Font("Courier New", Font.ITALIC, 12));
		// initialize the text area
		setTextArea();
		// initialize the JToolbar
		setJToolBar();
		// add listeners to buttons and drop boxes
		setButtonListeners();
		// Add Timer for saving every period: 5s
		Timer timer = new Timer();
		timer.schedule(new BackupDocument(), 0, 5000);
	}

	/**
	 * This method sets up the text area.
	 */
	public void setTextArea() {
		textpane.setPreferredSize(new Dimension(100, 100));
		textpane.setBackground(Color.WHITE);
		JScrollPane scrollpane = new JScrollPane(textpane);
		// Outlined text area with a border
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textpane.setBorder(borderOutline);
		//textpane.addKeyListener(new TextChangeListener()); //Disabled for now, maybe we add an option to turn this on if the userwants
		// add text/scrollpane are to JFrame
		this.add(scrollpane, BorderLayout.CENTER);
	}

	/**
	 * This method sets up the tool bar.
	 */
	public void setJToolBar() {
		// initialize images
		Image boldImage = null;
		Image italicImage = null;
		Image underlineImage = null;
		Image colorImage = null;
		// load images
		try {
			boldImage = ImageIO.read(new File("./images/boldImage.png"));
			italicImage = ImageIO.read(new File("./images/italicImage.png"));
			underlineImage = ImageIO.read(new File("./images/underlineImage.png"));
			colorImage = ImageIO.read(new File("./images/colorImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't load an image on the toolbar");
		}
		// set buttons with icons in them
		boldFontButton = new JButton(new ImageIcon(boldImage));
		italicFontButton = new JButton(new ImageIcon(italicImage));
		underlineFontButton = new JButton(new ImageIcon(underlineImage));
		colorButton = new JButton(new ImageIcon(colorImage));
		// add buttons to the tool bar
		javaToolBar.add(boldFontButton);
		javaToolBar.add(italicFontButton);
		javaToolBar.add(underlineFontButton);
		javaToolBar.add(colorButton);
		// add drop down menus to the tool bar
		javaToolBar.addSeparator();
		javaToolBar.add(sizeFontDropDown);
		javaToolBar.addSeparator();
		javaToolBar.add(fontDropDown);
		// add the tool bar to the frame
		this.add(javaToolBar, BorderLayout.NORTH);
	}

	private class BackupDocument extends TimerTask {
		public void run() {
			EditableDocument currentDoc = new EditableDocument(textpane.getStyledDocument());
			// TODO: Send doc to server
		}
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
		colorButton.addActionListener(new colorListener());
	}

	private class boldListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// text is selected
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("Bold", null);
				StyleConstants.setBold(style, true);
				
				if (!myToolBar.isBold()) {
					StyleConstants.setBold(style, true);
					myToolBar.setIsBold(true);
				} else {
					StyleConstants.setBold(style, false);
					myToolBar.setIsBold(false);
				}
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}

	private class italicListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// text is selected
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("Italic", null);
				if (!myToolBar.isItalic()) {
					StyleConstants.setItalic(style, true);
					myToolBar.setIsItalic(true);
				} else {
					StyleConstants.setItalic(style, false);
					myToolBar.setIsItalic(false);
				}
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}

	private class underlineListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// text is selected
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("UnderLine", null);
				StyleConstants.setUnderline(style, true);
				
				if (!myToolBar.isUnderlined()) {
					StyleConstants.setUnderline(style, true);
					myToolBar.setIsUnderlined(true);
				} else {
					StyleConstants.setUnderline(style, false);
					myToolBar.setIsUnderlined(false);
				}
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}

	private class sizeFontDropDownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Integer fontSize = (int) sizeFontDropDown.getSelectedItem();
			
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("FontSize", null);
				StyleConstants.setFontSize(style, fontSize);
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}

	private class fontDropDownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String stringFont = (String) fontDropDown.getSelectedItem();
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("FontFamily", null);
				StyleConstants.setFontFamily(style, stringFont);
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}

	private class colorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JColorChooser colorChooser = new JColorChooser();
			Color newColor = JColorChooser.showDialog(colorChooser, "Choose Text Color", Color.BLACK);
			if (textpane.getSelectedText() != null) {
				int selectStart = textpane.getSelectionStart();
				int selectEnd = textpane.getSelectionEnd();
				StyledDocument doc = (StyledDocument) textpane.getStyledDocument();
				Style style = textpane.addStyle("FontColor", null);
				StyleConstants.setForeground(style, newColor);
				doc.setCharacterAttributes(selectStart, selectEnd - selectStart, style, false);
			}
		}
	}
	
	private class TextChangeListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {		
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Request rq = new Request(RequestCode.DOCUMENT_SENT);
			EditableDocument doc = new EditableDocument(textpane.getStyledDocument());
			rq.setDocument(doc);
			try {
				oos.writeObject(rq);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
		
	}

	private class ServerListener extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Response response = (Response) ois.readObject();
					if (response.getResponseID() == ResponseCode.DOCUMENT_SENT){
						EditorGui.this.textpane.setStyledDocument(response.getStyledDocument());
						EditorGui.this.textpane.setCaretPosition(textpane.getText().length());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
