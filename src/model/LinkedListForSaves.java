package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.EditableDocument;
import model.User;

/**
 * LinkedList to store saved documents
 *
 */
public class LinkedListForSaves implements Serializable{

	private static final long serialVersionUID = 1L;
	private SpineNode headOfList;

	/**
	 * Nodes to store the documentName and the users who can edit it
	 *
	 */
	private class SpineNode implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String documentName;
		private List<String> editors;
		private List<String> owners;
		private EdgeNode mostRecentSave;
		private SpineNode nextDocumentNode;

		private SpineNode(String documentName) {
			this.documentName = documentName;
			editors = new ArrayList<String>();
			owners = new ArrayList<String>();
		}
		
		private void addNewEditor(String user){
			editors.add(user);
		}
		
		private void addnewOwner(String user){
			owners.add(user);
		}


	}

	/**
	 * Nodes which store the documents themselves
	 *
	 */
	private class EdgeNode implements Serializable{

		private static final long serialVersionUID = -3279145660210051848L;
		private String fileName;
		private String summary;
		private EdgeNode nextOlderSave;
		private Timestamp timeSaved;

		private EdgeNode(String fileName) {
			this.fileName = fileName;
		}
	}

	/**
	 * Create a new list of SpineNodes
	 */
	public LinkedListForSaves() {
		headOfList = new SpineNode("Head of List");
	}

	/**
	 * This will be the function call from the server to create a new save, pass
	 * in the documentName and the fileName
	 * 
	 * @param document
	 * @param fileName
	 */
	public void createSave(EditableDocument document, String fileName, User user) {
		SpineNode temp = headOfList;
		String documentName = document.getName();
		while (temp != null) {
			if (temp.documentName.equals(documentName)) {
				addNewSave(fileName, temp);
				return;
			}
			temp = temp.nextDocumentNode;
		}
		addNewDocument(documentName, fileName, user);
	}

	private void addNewDocument(String documentName, String fileName, User user) {
		SpineNode newDocumentNode = new SpineNode(documentName);
		newDocumentNode.addnewOwner(user.getUsername());
		newDocumentNode.addNewEditor(user.getUsername());
		newDocumentNode.nextDocumentNode = headOfList.nextDocumentNode;
		headOfList.nextDocumentNode = newDocumentNode;
		addNewSave(fileName, newDocumentNode);
	}

	private void addNewSave(String fileName, SpineNode documentNode) {
		EdgeNode newSaveNode = new EdgeNode(fileName);
		newSaveNode.timeSaved = new Timestamp(System.currentTimeMillis());
		newSaveNode.nextOlderSave = documentNode.mostRecentSave;
		documentNode.mostRecentSave = newSaveNode;
	}

	/**
	 * Access the fileName of the most recent save
	 * 
	 * @param documentName
	 * @return
	 */
	public String getMostRecentSave(String documentName) {
		SpineNode temp = headOfList;
		while (temp != null) {
			if (temp.documentName.equals(documentName)) {
				return temp.mostRecentSave.fileName;
			}
			temp = temp.nextDocumentNode;
		}
		return "Document Not Found";
	}
	
	/**
	 * 
	 * @param editor
	 * @return
	 */
	public String[] getDocumentsByEditor(String editor){
		ArrayList<String> docs = new ArrayList<>();
		SpineNode node = this.headOfList;
		while(node != null){
			if(node.editors.contains(editor)){
				docs.add(node.documentName);
			}
			node = node.nextDocumentNode;
		}
		System.out.println("Server: Found " + docs.size() + " documents editable by " + editor);
		String[] doclist = new String[docs.size()];
		return docs.toArray(doclist);
	}
	
	/**
	 * 
	 * @param owner
	 * @return
	 */
	public String[] getDocumentsByOwner(String owner){
		ArrayList<String> docs = new ArrayList<>();
		SpineNode node = this.headOfList;
		while(node != null){
			if(node.owners.contains(owner)){
				docs.add(node.documentName);
			}
			node = node.nextDocumentNode;
		}
		System.out.println("Server: Found " + docs.size() + " documents owned by " + owner);
		String[] doclist = new String[docs.size()];
		return docs.toArray(doclist);
	}
	
	public boolean addUserAsEditor(String username, String documentname){
		SpineNode node = this.headOfList;
		while(node != null){
			if(node.documentName.equals(documentname)){
				node.editors.add(username);
				return true;
			}
			node = node.nextDocumentNode;
		}
		return false;
	}
	
	public ArrayList<String> getRevisionHistroy(String documentname){
		SpineNode node = this.headOfList;
		EdgeNode version = null;
		ArrayList<String> history = new ArrayList<String>();
		while(node != null){
			if(node.documentName.equals(documentname)){
				version = node.mostRecentSave;
				break;
			}
			node = node.nextDocumentNode;
		}
		if(node == null)
			return null;
		else{
			while(version != null){
				history.add(version.summary);
				System.out.println(version.summary);
				version = version.nextOlderSave;
			}
			return history;
		}
	
	}

}
