package be.vinci.pae.services.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import be.vinci.pae.utils.Config;

public class DalServicesImpl implements DalServices {

  private Connection conn;

  /**
   * Constructor. Make the connection with the DB using the keys "url", "user", and "password" in a
   * properties file.
   */
  public DalServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Missing postgresql driver!");
      System.exit(1);
    }
    try {
      conn = DriverManager.getConnection(Config.getProperty("url"), Config.getProperty("user"),
          Config.getProperty("password"));
    } catch (SQLException e) {
      System.out.println("Unable to reach the server!");
      System.exit(1);
    }

  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ps;
  }
}
