package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.ConfigReader;

public class BaseTest {
	protected WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		driver = DriverManager.getDriver();
		try {
			driver.manage().window().maximize();
		} catch (Exception ignored) {
		}
		String baseUrl = ConfigReader.get("baseUrl");
		if (baseUrl != null && !baseUrl.isEmpty()) {
			try {
				driver.get(baseUrl);
			} catch (Exception ignored) {
			}
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		DriverManager.quitDriver();
	}
}
