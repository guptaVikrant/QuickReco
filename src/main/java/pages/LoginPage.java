package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class LoginPage {
	
@Test
public void login(String username, String password) {
	// Code to perform login action using the provided username and password
	System.out.println("Logging in with username: " + username + " and password: " + password);
	// Here you would typically interact with the web elements to enter the credentials and submit the form		
	
	WebDriver driver = new ChromeDriver(); // Assuming you are using ChromeDriver
	driver.get("https://example.com/login"); // Navigate to the login page
}
}