package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

public class DriverManager {
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			String browser = ConfigReader.get("browser", "chrome").toLowerCase();
			switch (browser) {
				case "firefox":
					WebDriverManager.firefoxdriver().setup();
					FirefoxOptions fo = new FirefoxOptions();
					driver.set(new FirefoxDriver(fo));
					break;
				case "chrome":
				default:
					WebDriverManager.chromedriver().setup();
					ChromeOptions co = new ChromeOptions();
					// You can add default options here, e.g., headless based on config
					driver.set(new ChromeDriver(co));
					break;
			}
		}
		return driver.get();
	}

	public static void quitDriver() {
		WebDriver wd = driver.get();
		if (wd != null) {
			try {
				wd.quit();
			} catch (Exception ignored) {
			}
			driver.remove();
		}
	}
}
