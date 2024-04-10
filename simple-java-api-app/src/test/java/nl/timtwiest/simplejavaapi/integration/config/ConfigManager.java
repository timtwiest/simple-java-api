package nl.timtwiest.simplejavaapi.integration.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ConfigManager {
  private static final String ENV = System.getProperty("env", "default");
  private static final Properties PROPERTIES = new Properties();

  static {
    try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("properties/" + ENV + ".properties")) {
      if (input != null) {
        PROPERTIES.load(input);
      } else {
        log.error("Properties file not found for environment: {}", ENV);
        throw new RuntimeException("Properties file not found for environment: " + ENV);
      }
    } catch (IOException ex) {
      log.error("Error loading properties file for environment: {}", ENV, ex);
      throw new RuntimeException(ex);
    }

  }

  public static String get(String key) {
    return PROPERTIES.getProperty(key);
  }
}
