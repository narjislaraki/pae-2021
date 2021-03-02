package be.vinci.pae.main;

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
  static UtilisateurDAOImpl ds = new UtilisateurDAOImpl();

  public static void main(String[] args) {
    System.out.println("Hello world!");
  }

}
