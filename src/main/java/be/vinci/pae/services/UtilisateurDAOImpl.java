package be.vinci.pae.services;

import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UtilisateurDAOImpl implements UtilisateurDAO {
  private Connection conn = null;

  public UtilisateurDAOImpl() {
    // TODO Attention, la connexion devra se faire dans une classe externe
    Config.load("prod.properties");
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver postgresql manquant !");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(Config.getProperty("url"), Config.getProperty("user"),
          Config.getProperty("password"));
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur !");
      System.exit(1);
    }
    try {
      conn.close();
      System.out.println("La connexion vient de se fermer");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Recherche de l'utilisateur dans la DB via son pseudo
   * 
   * @param String pseudo le pseudo
   * @return l'utilisateur complet s'il existe, sinon null
   */
  @Override
  public Utilisateur getUtilisateur(String pseudo) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Recherche de l'utilisateur dans la DB via son id
   * 
   * @param int id l'id
   * @return l'utilisateur complet s'il existe, sinon null
   */
  @Override
  public Utilisateur getUtilisateur(int id) {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * Ajoute un utilisateur
   * 
   * @param Utilisateur utilisateur l'utilisateur
   */
  @Override
  public void addUtilisateur(Utilisateur utilisateur) {
    // TODO Auto-generated method stub --> return quelque chose type boolean ou plus complexe?

  }



}
