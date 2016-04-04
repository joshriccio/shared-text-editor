package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import model.*;

public class EditorTests {

		@Test
		public void usertestpass(){
			User user1 = new User("Josh", "123");
			assertEquals(user1.getPassword(), "123");
		}
		
		@Test
		public void usertestname(){
			User user1 = new User("Josh", "123");
			assertEquals(user1.getUsername(), "Josh");
		}
		
		@Test
		public void toolbartestColor(){
			Toolbar tb = new Toolbar();
			tb.setColor(Color.RED);
			assertEquals(tb.getColor(), Color.RED);
		}
}
