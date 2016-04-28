package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import network.Request;
import network.RequestCode;

/**
 * A listener that checks when a user has exiting the system
 * 
 * @author Joshua Riccio
 *
 */
public class LogOffListener implements WindowListener {

	private String user;
	private ObjectOutputStream oos;
	private boolean closedWindow;

	/**
	 * 
	 * @param user the user that has logged off @param oos the user's object
	 * output stream
	 */
	public LogOffListener(String user, ObjectOutputStream oos) {
		this.user = user;
		this.oos = oos;
		closedWindow = false;
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (!closedWindow) {
			Request exit = new Request(RequestCode.USER_EXITING);
			exit.setUsername(user);
			try {
				oos.writeObject(exit);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			closedWindow = true;
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (!closedWindow) {
			Request exit = new Request(RequestCode.USER_EXITING);
			exit.setUsername(user);
			try {
				oos.writeObject(exit);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			closedWindow = true;
		}

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}