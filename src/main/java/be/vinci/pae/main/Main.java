package be.vinci.pae.main;

import be.vinci.pae.services.UtilisateurDAO;
import be.vinci.pae.services.UtilisateurDAOImpl;
import be.vinci.pae.utils.Config;

/**
 * Main class.
 *
 */
public class Main {

  /**
   * Main method.
   * 
   * @param args command line arguments
   */
  static UtilisateurDAO ds = new UtilisateurDAOImpl();
  static String env;

  public static void main(String[] args) {
    try {
      env = args[0];
    } catch (Exception e) {
      env = "prod";
    }

    switch (env) {
      case "dev": // TODO Utile?
        Config.load("dev.properties");
        break;

      case "test":
        Config.load("test.properties");
        break;

      default:
        Config.load("prod.properties");
        break;
    }


    System.out.println("Hello world!");
  }

}
