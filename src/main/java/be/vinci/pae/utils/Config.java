package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  public static final String PROD = "prod.properties";
  private static Properties props = new Properties();

  /**
   * Load a properties file.
   * 
   * @param file the path of the file
   */
  public static void load(String file) {
    try (FileInputStream in = new FileInputStream(file)) {
      props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load a test properties file.
   */
  public static void load() {
    try (FileInputStream in = new FileInputStream(PROD)) {
      props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter for a key's value.
   * 
   * @param key the key
   * @return the value of the key as a String
   */
  public static String getStringProperty(String key) {
    return props.getProperty(key);
  }

  public static int getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }

}
