package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import network.Request;
import network.RequestCode;
import network.UserStreamModel;

public class UsersOnline extends JPanel{
	
	private DefaultListModel<String> listmodel;
	private JList<String> list;
	private JScrollPane scrollpane;
	private ObjectOutputStream oos;
	
	public UsersOnline(ObjectOutputStream oos){
		this.oos = oos;
		listmodel = new DefaultListModel<String>();
		list = new JList<String>(listmodel);
		scrollpane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setPreferredSize(new Dimension(120, 100));
	    setLayout(new BorderLayout());
	    this.add(scrollpane, BorderLayout.CENTER);
	}
	
	public void init(){
		Request getUsers = new Request(RequestCode.GET_USER_LIST);
		try {
			oos.writeObject(getUsers);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateUsers(String[] userlist){
		System.out.println("users added");
		for(int i=0; i< userlist.length; i++){
			if(userlist[i].substring(0, 1).equals("-")){
				if(listmodel.contains(userlist[i].substring(1, userlist[i].length()))){
					listmodel.removeElement(userlist[i].substring(1, userlist[i].length()));
				}
			}else if(!listmodel.contains(userlist[i])){
				listmodel.addElement(userlist[i]);
			}
		}
	}

}
