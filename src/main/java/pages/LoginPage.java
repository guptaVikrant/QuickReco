package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.testng.annotations.Test;

public class LoginPage {
	private WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		this.driver=driver;
	}
	
					By username=By.id("username");
					By password=By.xpath("//input[@text='xy']");

					public void login(String user, String pass) {
						System.out.println("Logging in with username: " + user + " and password: " + pass);
						if (driver == null) {
							System.err.println("WebDriver is null in LoginPage - cannot perform UI actions");
							return;
						}

						try {
							// If the test already navigated to the base URL (BaseTest does this), we can just try to interact
							// Attempt to enter username/password if elements exist; otherwise just navigate to the login page
							try {
								driver.findElement(username).clear();
								driver.findElement(username).sendKeys(user);
								driver.findElement(password).clear();
								driver.findElement(password).sendKeys(pass);
								// Attempt to submit the form
								driver.findElement(password).submit();
							} catch (Exception e) {
								// Fallback: navigate to a default login path if elements are not present
								String base = utils.ConfigReader.get("baseUrl", "https://example.com/login");
								driver.get(base);
							}
						} catch (Exception e) {
							System.err.println("Exception during login: " + e.getMessage());
						}
					}
}