package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class TabbedPane extends JTabbedPane{
	
	private HashMap<String, JTextPane> textpanemap;

	public TabbedPane(String docName) {
		textpanemap = new HashMap<>();
		JTextPane textpane = new JTextPane();
		textpanemap.put(docName, textpane);
		textpane.setPreferredSize(new Dimension(100, 100));
		textpane.setBackground(Color.WHITE);
		textpane.setText("");
		JScrollPane scrollpane = new JScrollPane(textpane);
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textpane.setBorder(borderOutline);
		this.addTab(docName, scrollpane);
	}
	
	public void addNewTab(String docName){
		JTextPane textpane = new JTextPane();
		textpanemap.put(docName, textpane);
		textpane.setPreferredSize(new Dimension(100, 100));
		textpane.setBackground(Color.WHITE);
		textpane.setText("");
		JScrollPane scrollpane = new JScrollPane(textpane);
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textpane.setBorder(borderOutline);
		this.addTab(docName, scrollpane);
	}
	
	public JTextPane getContext(){
		return textpanemap.get(this.getTitleAt(this.getSelectedIndex()));
	}

}
