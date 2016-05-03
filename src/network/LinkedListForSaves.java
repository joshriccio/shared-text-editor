package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.EditableDocument;
import model.User;

/**
 * LinkedList to store saved documents. This data structure is used to organize all the saved
 * data in an adjacency list so that all data can be accessed linearly. 
 * 
 * @author Stephen Connolly
 * @author Cody Deeran
 * @author Brittany Paielli
 *
 */
public class LinkedListForSaves implements Serializable {

	private static final long serialVersionUID = 1L;
	private SpineNode headOfList;

	/**
	 * Nodes to store the documentName and the users who can edit it
	 *
	 */
	private class SpineNode implements Serializable {

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

		private void addNewEditor(String user) {
			editors.add(user);
		}

		private void addnewOwner(String user) {
			owners.add(user);
		}

	}

	/**
	 * Nodes which store the documents themselves
	 *
	 */
	private class EdgeNode implements Serializable {

		private static final long serialVersionUID = -3279145660210051848L;
		private String fileName;
		private String summary;
		private EdgeNode nextOlderSave;

		/**
		 * Constructor for Edgenode
		 */
		private EdgeNode(String fileName, String summary) {
			this.fileName = fileName;
			this.summary = summary;
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
	 * @param document the document to save @param fileName the file name of the document
	 */
	public void createSave(EditableDocument document, String fileName, User user) {
		SpineNode temp = headOfList;
		String documentName = document.getName();
		while (temp != null) {
			if (temp.documentName.equals(documentName)) {
				addNewSave(fileName, temp, document.getSummary());
				return;
			}
			temp = temp.nextDocumentNode;
		}
		addNewDocument(documentName, fileName, user, document.getSummary());
	}

	private void addNewDocument(String documentName, String fileName, User user, String summary) {
		SpineNode newDocumentNode = new SpineNode(documentName);
		newDocumentNode.addnewOwner(user.getUsername());
		newDocumentNode.addNewEditor(user.getUsername());
		newDocumentNode.nextDocumentNode = headOfList.nextDocumentNode;
		headOfList.nextDocumentNode = newDocumentNode;
		addNewSave(fileName, newDocumentNode, summary);
	}

	private void addNewSave(String fileName, SpineNode documentNode, String summary) {
		EdgeNode newSaveNode = new EdgeNode(fileName, summary);
		newSaveNode.nextOlderSave = documentNode.mostRecentSave;
		documentNode.mostRecentSave = newSaveNode;
	}

	/**
	 * Access the fileName of the most recent save
	 * 
	 * @param documentName @return returns the most recent save
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
	 * Returns all documents editable by the editor @param editor the editor to
	 * query @return The list of all documents editable by the user
	 */
	public String[] getDocumentsByEditor(String editor) {
		ArrayList<String> docs = new ArrayList<>();
		SpineNode node = this.headOfList;
		while (node != null) {
			if (node.editors.contains(editor)) {
				docs.add(node.documentName);
			}
			node = node.nextDocumentNode;
		}
		System.out.println("Server: Found " + docs.size() + " documents editable by " + editor);
		String[] doclist = new String[docs.size()];
		return docs.toArray(doclist);
	}

	/**
	 * Returns a list of documents owned by the user @param owner the owner of
	 * the document @return the list of documents owned by the user
	 */
	public String[] getDocumentsByOwner(String owner) {
		ArrayList<String> docs = new ArrayList<>();
		SpineNode node = this.headOfList;
		while (node != null) {
			if (node.owners.contains(owner)) {
				docs.add(node.documentName);
			}
			node = node.nextDocumentNode;
		}
		System.out.println("Server: Found " + docs.size() + " documents owned by " + owner);
		String[] doclist = new String[docs.size()];
		return docs.toArray(doclist);
	}

	/**
	 * Adds a user as an editor for the specified document @param username the
	 * user name to add as an editor @param documentname the document name to be
	 * updates @return true if successful
	 */
	public boolean addUserAsEditor(String username, String documentname) {
		SpineNode node = this.headOfList;
		while (node != null) {
			if (node.documentName.equals(documentname)) {
				node.editors.add(username);
				return true;
			}
			node = node.nextDocumentNode;
		}
		return false;
	}

	/**
	 * Adds a user as the owner of the named document, replaces previous
	 * owner 
	 * @param username the user name to add 
	 * @param documentname the name of the document 
	 * @return true if succesful, false if failed
	 */
	public boolean addUserAsOwner(String username, String documentname) {
		SpineNode node = this.headOfList;
		while (node != null) {
			if (node.documentName.equals(documentname)) {
				// remove owner on list and add the new owner
				node.owners.remove(0);
				node.owners.add(username);
				return true;
			}
			node = node.nextDocumentNode;
		}
		return false;
	}

	/**
	 * Gets the revision history for the named document @param documentname the
	 * name of the document @return the revision history
	 */
	public ArrayList<String> getRevisionHistroy(String documentname) {
		SpineNode node = this.headOfList;
		EdgeNode version = null;
		ArrayList<String> history = new ArrayList<String>();
		while (node != null) {
			if (node.documentName.equals(documentname)) {
				version = node.mostRecentSave;
				break;
			}
			node = node.nextDocumentNode;
		}
		if (node == null)
			return null;
		else {
			while (version != null) {
				history.add(version.summary);
				version = version.nextOlderSave;
			}
			return history;
		}
	}

	/**
	 * Gets the previous saved document @param documentName the name of the
	 * document @param summary the summary to use for searching @return the
	 * document as string
	 */
	public String getOldSave(String documentName, String summary) {
		SpineNode spinenode = headOfList;
		EdgeNode edgenode = null;
		while (spinenode != null) {
			if (spinenode.documentName.equals(documentName)) {
				edgenode = spinenode.mostRecentSave;
				while (edgenode != null) {
					if (edgenode.summary.equals(summary)) {
						return edgenode.fileName;
					}
					edgenode = edgenode.nextOlderSave;
				}
			}
			spinenode = spinenode.nextDocumentNode;
		}
		return null;
	}

}
