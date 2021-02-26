package be.vinci.pae.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataServiceUserCollection {
  private Connection conn = null;

  public DataServiceUserCollection() {
    Context.load("prod.properties");
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver postgresql manquant !");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(Context.getProperty("url"), Context.getProperty("user"),
          Context.getProperty("password"));
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



}
