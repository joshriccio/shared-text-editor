package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.Border;

public class EditorGui extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextArea textArea = new JTextArea(5,10);
	
	//set up JtoolBar with buttons
	private JToolBar toolBar = new JToolBar();
	private JButton boldFont;
	private JButton italicFont;
	private JButton underlineFont;
	
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
		
		//initialize the JToolbar
		setJToolBar();
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
	    
	    //Outlined text area with a border
	    Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
	    textArea.setBorder(BorderFactory.createCompoundBorder(borderOutline, 
	            BorderFactory.createEmptyBorder(100, 100, 100, 100)));
	    
	    //add text are to JFrame
	    this.add(textArea, BorderLayout.CENTER);
	}
	
	private void setJToolBar() {
		//initialize images, follow with try/catch to load images
		 Image boldImage = null;
		 Image italicImage = null;
		 Image underlineImage =null;
		
		try {
			boldImage = ImageIO.read(new File("./images/boldImage.png"));
			italicImage = ImageIO.read(new File("./images/italicImage.png"));
			underlineImage = ImageIO.read(new File("./images/underlineImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Couldn't load an image on the toolbar");
		}
		
		//resize images
		boldImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
		italicImage.getScaledInstance(10, 10, Image.SCALE_DEFAULT);
		underlineImage.getScaledInstance(10, 10, Image.SCALE_DEFAULT);
		
		//Initialize icons
		 ImageIcon boldIcon = new ImageIcon(boldImage);
		 ImageIcon italicIcon = new ImageIcon(italicImage);
		 ImageIcon underlineIcon= new ImageIcon(underlineImage);
		 
		//set buttons with icons in them
		 boldFont = new JButton(boldIcon);
		 italicFont  = new JButton(italicIcon);
		 underlineFont = new JButton(underlineIcon);
		 
		//add buttons to the toolbar
		toolBar.add(boldFont);
		toolBar.add(italicFont);
		toolBar.add(underlineFont);
		
		this.add(toolBar, BorderLayout.NORTH);
	}

}
