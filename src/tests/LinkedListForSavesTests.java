package tests;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.junit.Test;

import model.EditableDocument;
import model.User;
import network.LinkedListForSaves;

public class LinkedListForSavesTests {
	
	public static LinkedListForSaves savedFileList = new LinkedListForSaves();
	private String docName1 = "docName1";
	private String docName2 = "docName2";
	private String docName3 = "docName3";
	private EditableDocument edDoc1 = new EditableDocument(null, docName1);
	private EditableDocument edDoc2 = new EditableDocument(null, docName2);
	private EditableDocument edDoc3 = new EditableDocument(null, docName3);
	
	private String fakeDocName = "noSuchDocument";
	
	
	@Test
	public void getMostRecentSaveTest() throws NoSuchAlgorithmException, NoSuchProviderException {
		User Brittany = new User("Brittany", "password");
		User Cody = new User("Cody", "password");
		savedFileList.createSave(edDoc1, docName1, Brittany);
		assertTrue(savedFileList.getMostRecentSave(docName1).equals("docName1"));
		assertTrue(savedFileList.getMostRecentSave(fakeDocName).equals("Document Not Found"));
		savedFileList.createSave(edDoc2, docName2, Cody);
		assertFalse(savedFileList.getMostRecentSave(docName2).equals("docName1"));
		assertTrue(savedFileList.getMostRecentSave(docName2).equals("docName2"));
		
	}
	
	@Test
	public void getDocByEditorTests() throws NoSuchAlgorithmException, NoSuchProviderException{
		User Brittany = new User("Brittany", "password");
		savedFileList.createSave(edDoc1, docName1, Brittany);
		savedFileList.createSave(edDoc2, docName2, Brittany);
		savedFileList.createSave(edDoc3, docName3, Brittany);
		System.out.println("This is the list: " +  savedFileList.getDocumentsByEditor("Brittany"));
		savedFileList.getDocumentsByEditor("Brittany");
		savedFileList.getDocumentsByOwner("Brittany");
	}
	
	@Test 
	public void addUserAsEditorTest() throws NoSuchAlgorithmException, NoSuchProviderException{
		new User("Brittany", "password");
		new User("Cody", "password");
		assertTrue(savedFileList.addUserAsEditor("Brittany", docName1));
		assertTrue(savedFileList.addUserAsEditor("Cody", docName1));
		assertFalse(savedFileList.addUserAsEditor("Cody", fakeDocName));
	}
}
