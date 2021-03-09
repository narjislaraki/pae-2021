package be.vinci.pae.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  public static final String TEST = "test.properties";
  public static final String PROD = "prod.properties";
  private static Properties props = new Properties();

  public static void load(String file) {
    try (FileInputStream in = new FileInputStream(file)) {
      props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void load() {
    try (FileInputStream in = new FileInputStream(PROD)) {
      props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getProperty(String key) {
    return props.getProperty(key);
  }

}
