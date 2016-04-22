package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import network.Request;
import network.RequestCode;
import network.Response;
import network.ResponseCode;
import network.Server;

/**
 * 
 * A JPanel that lists all the users currently connected to the server.
 * 
 * @author Joshua Riccio
 *
 */
public class UsersOnline extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> listmodel;
	private JList<String> list;
	private JScrollPane scrollpane;
	private ObjectOutputStream oos;
	private JPopupMenu menu;

	/**
	 * The constructor takes the current objectoutputstream so that it can get
	 * the most up to date info on who is connected
	 * 
	 * @param oos
	 */
	public UsersOnline(ObjectOutputStream oos, DefaultListModel<String> listmodel, JList<String> list, JPopupMenu menu) {
		this.listmodel = listmodel;
		this.list = list;
		this.menu = menu;
		this.oos = oos;
		scrollpane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setPreferredSize(new Dimension(120, 100));
		setLayout(new BorderLayout());
		this.list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					UsersOnline.this.menu.show(e.getComponent(), e.getX(), e.getY());
				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});
		this.add(scrollpane, BorderLayout.CENTER);
	}

	/**
	 * Initializes the jpanel
	 */
	public void init() {
		Request getUsers = new Request(RequestCode.GET_USER_LIST);
		try {
			oos.writeObject(getUsers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the panel with the users who connected/ disconnected
	 * 
	 * @param userlist
	 *            the most up to date userlist
	 */
	public void updateUsers(String[] userlist) {
		System.out.println("users added");
		for (int i = 0; i < userlist.length; i++) {
			if (userlist[i].substring(0, 1).equals("-")) {
				if (listmodel.contains(userlist[i].substring(1, userlist[i].length()))) {
					listmodel.removeElement(userlist[i].substring(1, userlist[i].length()));
				}
			} else if (!listmodel.contains(userlist[i])) {
				listmodel.addElement(userlist[i]);
			}
		}
	}

}