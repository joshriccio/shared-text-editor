package controller;

import view.LoginScreen;

/**
 * Main method - entry point for Collaborative Editor. This class launches the
 * login panel.
 * 
 * @author Brittany Paielli
 * @author Joshua Riccio
 * @author Cody Deeran
 * @author Steven Connolly
 *
 */
public class EditorMain {
	/**
	 * @param args
	 *            arguments for main method
	 */
	public static void main(String[] args) {
		LoginScreen loginPrompt = new LoginScreen();
		loginPrompt.setVisible(true);
	}
}
