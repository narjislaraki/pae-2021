package be.vinci.pae.services.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.utils.Config;

public class DalServicesImpl implements DalServices, DalBackendServices {

  private ThreadLocal<Connection> conn;
  String url;
  String user;
  String password;

  /**
   * Constructor. Make the connection with the DB using the keys "url", "user", and "password" in a
   * properties file.
   */
  public DalServicesImpl() {
    url = Config.getStringProperty("url");
    user = Config.getStringProperty("user");
    password = Config.getStringProperty("password");
    conn = new ThreadLocal<Connection>();
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Missing postgresql driver!");
      System.exit(1);
    }
    /*
     * try { conn = DriverManager.getConnection(Config.getStringProperty("url"),
     * Config.getStringProperty("user"), Config.getStringProperty("password")); } catch
     * (SQLException e) { throw new FatalException("Unable to reach the SQL server!", e); }
     */

  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.get().prepareStatement(sql);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }

  @Override
  public PreparedStatement getPreparedStatementWithGeneratedReturn(String sql) {
    PreparedStatement ps = null;
    try {
      ps = conn.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }

  @Override
  public void startTransaction() {
    try {
      Connection connection = DriverManager.getConnection(url, user, password);
      connection.setAutoCommit(false);
      conn.set(connection);
    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  @Override
  public void commitTransaction() {
    try {
      Connection connection = conn.get();
      if (connection == null) {
        return;
      }
      connection.commit();
      conn.remove();
      connection.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void rollbackTransaction() {
    try {
      Connection connection = conn.get();
      connection.rollback();
      conn.remove();
      connection.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
