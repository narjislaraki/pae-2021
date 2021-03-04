package be.vinci.pae.services;

import be.vinci.pae.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DalServicesImpl implements DalServices {

  private Connection conn;

  public DalServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver postgresql manquant !");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(Config.getProperty("url"),
          Config.getProperty("utilisateur"), Config.getProperty("motDePasse"));
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

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return ps;
  }
}
