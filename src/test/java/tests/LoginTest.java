package tests;

import org.testng.annotations.Test;
import base.BaseTest;
import pages.LoginPage;

public class LoginTest extends BaseTest {

	@Test
	public void testLogin() {
		System.out.println("I am in login test");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("testuser", "testpass");
	}
}