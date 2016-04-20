package model;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.EditableDocument;
import model.User;

public class LinkedListForSaves {
	
	private SpineNode headOfList;
	
	// Nodes to store the documentName and the users who can edit it
	private class SpineNode {
		private String documentName;
		private List<User> editors;
		private EdgeNode mostRecentSave;
		private SpineNode nextDocumentNode;
		
		private SpineNode(String documentName) {
			this.documentName = documentName;
		}
	}
	
	// Nodes which store the documents themselves
	private class EdgeNode {
		private String fileName;
		private EdgeNode nextOlderSave;
		private Timestamp timeSaved;
		
		private EdgeNode(String fileName) {
			this.fileName = fileName;
		}
	}
	
	// Create a new list of SpineNodes
	public LinkedListForSaves() {
		
		headOfList = new SpineNode("Head of List");
	}
	
	// This will be the function call from the server to create a new save, pass in the documentName and the fileName
	public void createSave(EditableDocument document, String fileName) {
		
		SpineNode temp = headOfList;
		String documentName = document.getName();
		
		while (temp != null) {
			if (temp.documentName.equals(documentName)) {
				addNewSave(fileName, temp);
				return;
			}
			temp = temp.nextDocumentNode;
		}
		
		addNewDocument(documentName, fileName);
		
	}
	
	// Create a new SpineNode and insert it immediately after headOfList, then create the first saveNode
	private void addNewDocument(String documentName, String fileName) {
		
		SpineNode newDocumentNode = new SpineNode(documentName);
		
		newDocumentNode.nextDocumentNode = headOfList.nextDocumentNode;
		headOfList.nextDocumentNode = newDocumentNode;
		
		addNewSave(fileName, newDocumentNode);
		
	}

	// Create a new edge node with info for a saved file and attach it to the appropriate spine node.
	private void addNewSave(String fileName, SpineNode documentNode) {
		
		EdgeNode newSaveNode = new EdgeNode(fileName);
		
		newSaveNode.timeSaved = new Timestamp(System.currentTimeMillis());
		newSaveNode.nextOlderSave = documentNode.mostRecentSave;
		documentNode.mostRecentSave = newSaveNode;
	}
	
	// Access the fileName of the most recent save
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
	
	
//	
//	public ArrayList<Timestamp> getOlderSaves(int numberOfSavesToFetch, EditableDocument document) {
//		ArrayList<Timestamp> olderSaveTimestamps = new ArrayList<>();
//		String documentName = document.getName();
//		
//		SpineNode temp = headOfList;
//		
//		while (temp != null) {
//			if (temp.documentName.equals(documentName)) {
//				return temp.mostRecentSave.fileName;
//			}
//			temp = temp.nextDocumentNode;
//		}
//		
//		
//		for (int i = 0; i < numberOfSavesToFetch; i++) {
//			
//		}
//		
//		return olderSaveTimestamps;
//	}

}
