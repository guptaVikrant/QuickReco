package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	private static final Properties props = new Properties();

	static {
		try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (is != null) {
				props.load(is);
			} else {
				System.err.println("config.properties not found on classpath");
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}
	}

	public static String get(String key) {
		return props.getProperty(key);
	}

	public static String get(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		String v = props.getProperty(key);
		if (v == null) return defaultValue;
		try {
			return Integer.parseInt(v);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
