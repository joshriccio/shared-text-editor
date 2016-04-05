package tests;

public class ModelTester {
	
	private static int getFullCoverage = 1;
	
	public static void main(String[] args) {
		getFullCoverage = 2;
		new ToolbarTests();
		new UserTests();
	}

}
