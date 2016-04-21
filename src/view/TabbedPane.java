package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * TabbedPane extends JTabbedPane to add additional functionality, including
 * getting context regarding with tab is currently open
 * 
 * @author Joshua Riccio
 *
 */
public class TabbedPane extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, JTextPane> textpanemap;
	private JPopupMenu menu;

	/**
	 * The constructor takes in the name of the new document
	 * 
	 * @param docName
	 *            the name of the new document
	 */
	public TabbedPane(String docName) {
		textpanemap = new HashMap<>();
		JTextPane textpane = new JTextPane();
		textpanemap.put(docName, textpane);
		this.menu = new JPopupMenu();
		this.setupMenu();
		textpane.setPreferredSize(new Dimension(100, 100));
		textpane.setBackground(Color.WHITE);
		JScrollPane scrollpane = new JScrollPane(textpane);
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textpane.setBorder(borderOutline);
		this.addTab(docName, scrollpane);
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					TabbedPane.this.menu.show(e.getComponent(), e.getX(), e.getY());
				}

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
	}

	private void setupMenu() {
		JMenuItem item = new JMenuItem("Close Tab");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TabbedPane.this.remove(TabbedPane.this.getSelectedIndex());

			}

		});
		this.menu.add(item);

	}

	/**
	 * Adds a new tab with the new document name
	 * 
	 * @param docName
	 *            the name of the new document
	 */
	public void addNewTab(String docName) {
		JTextPane textpane = new JTextPane();
		textpanemap.put(docName, textpane);
		textpane.setPreferredSize(new Dimension(100, 100));
		textpane.setBackground(Color.WHITE);
		JScrollPane scrollpane = new JScrollPane(textpane);
		Border borderOutline = BorderFactory.createLineBorder(Color.GRAY);
		textpane.setBorder(borderOutline);
		this.addTab(docName, scrollpane);
	}

	/**
	 * Gets the TextPane of the currently viewed tab
	 * 
	 * @return returns the textpane of the currently viewed tab
	 */
	public JTextPane getCurrentTextPane() {
		return textpanemap.get(this.getTitleAt(this.getSelectedIndex()));
	}

	/**
	 * Gets the title of the currently vied tab
	 * 
	 * @return returns the title of the currently viewed tab
	 */
	public String getName() {
		return this.getTitleAt(this.getSelectedIndex());
	}

}
