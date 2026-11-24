package com.fieldforce.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration properties from config.properties.
 */
public class ConfigReader {
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                // Fallback to local file path
                try (FileInputStream fileInput = new FileInputStream("src/main/resources/config.properties")) {
                    properties.load(fileInput);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.trim().isEmpty()) {
            return systemProperty.trim();
        }
        return properties != null ? properties.getProperty(key) : null;
    }

    public static String getUrl() {
        return getProperty("url");
    }

    public static String getBrowser() {
        return getProperty("browser") != null ? getProperty("browser") : "chrome";
    }

    public static boolean isHeadless() {
        String headless = getProperty("headless");
        return headless == null || Boolean.parseBoolean(headless);
    }

    public static int getImplicitWait() {
        String wait = getProperty("implicit.wait");
        return wait != null ? Integer.parseInt(wait) : 10;
    }

    public static int getExplicitWait() {
        String wait = getProperty("explicit.wait");
        return wait != null ? Integer.parseInt(wait) : 15;
    }

    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    public static String getValidPassword() {
        return getProperty("valid.password");
    }
}
