package nl.timtwiest.simplejavaapi.integration.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final String ENV = System.getProperty("env", "default");
    private static final Properties PROPERTIES = new Properties();

    static {
        String resource = "properties/" + ENV + ".properties";
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("Properties file not found for environment: " + ENV);
            }
            PROPERTIES.load(input);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load properties for environment: " + ENV, exception);
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
