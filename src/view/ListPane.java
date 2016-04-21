package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ListPane extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> listmodel;
	private JList<String> list;
	private JScrollPane scrollpane;
	
	public ListPane(){
		listmodel = new DefaultListModel<String>();
		list = new JList<String>(listmodel);
		scrollpane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setPreferredSize(new Dimension(120, 100));
		setLayout(new BorderLayout());
		this.add(scrollpane, BorderLayout.CENTER);
	}
	
	public void updateDocumentList(String[] documentList) {
		for (int i = 0; i < documentList.length; i++) {
			if (documentList[i].substring(0, 1).equals("-")) {
				if (listmodel.contains(documentList[i].substring(1, documentList[i].length()))) {
					listmodel.removeElement(documentList[i].substring(1, documentList[i].length()));
				}
			} else if (!listmodel.contains(documentList[i])) {
				listmodel.addElement(documentList[i]);
			}
		}
	}
	
	public void addDemoDocuments(){
		String[] docs = new String[10];
		docs[0] = "Office Supplies";
		docs[1] = "Class Schedule";
		docs[2] = "ShoppingList";
		docs[3] = "Resume";
		docs[4] = "Football Schedule";
		docs[5] = "Birthdays";
		docs[6] = "Mailing List";
		docs[7] = "Chores";
		docs[8] = "televisionchannels";
		docs[9] = "SportsScores";
		
		this.updateDocumentList(docs);
	}

}
