package be.vinci.pae.services.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import be.vinci.pae.exception.FatalException;
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
      conn = DriverManager.getConnection(Config.getStringProperty("url"), Config.getStringProperty("user"),
          Config.getStringProperty("password"));
    } catch (SQLException e) {
      throw new FatalException("Unable to reach the SQL server!", e);
    }

  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }

  @Override
  public PreparedStatement getPreparedStatementWithGeneratedReturn(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }
}
