package be.vinci.pae.main;

import be.vinci.pae.services.UtilisateurDAO;
import be.vinci.pae.services.UtilisateurDAOImpl;

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

  public static void main(String[] args) {
    System.out.println("Hello world!");
  }

}
