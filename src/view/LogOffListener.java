package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import network.Request;
import network.RequestCode;

public class LogOffListener implements WindowListener{
	
	private String user;
	private ObjectOutputStream oos;
	
	public LogOffListener(String user, ObjectOutputStream oos){
		this.user = user;
		this.oos = oos;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Request exit = new Request(RequestCode.USER_EXITING);
		exit.setUsername(user);
		try {
			oos.writeObject(exit);
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
