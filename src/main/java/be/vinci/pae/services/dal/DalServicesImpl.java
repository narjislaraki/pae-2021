package be.vinci.pae.services.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.utils.Config;

public class DalServicesImpl implements DalServices, DalBackendServices {


  private static ThreadLocal<Connection> td;
  private static Properties properties = new Properties();
  private static BasicDataSource ds;

  String url;
  String user;
  String password;
  int connectionQuantity;

  /**
   * Constructor. Make the connection with the DB using the keys "url", "user", and "password" in a
   * properties file.
   */
  public DalServicesImpl() {
    url = (Config.getStringProperty("url"));
    user = Config.getStringProperty("user");
    password = Config.getStringProperty("password");
    connectionQuantity = Config.getIntProperty("connectionQuantity");

    // // ds = new BasicDataSource();
    // ds.setUrl(url);
    // ds.setUsername(user);
    // ds.setPassword(password);
    //
    // ds.setMinIdle(5);
    // ds.setMaxIdle(10);
    properties.setProperty("driverClassName", "org.postgresql.Driver");
    properties.setProperty("url", url);
    properties.setProperty("username", user);
    properties.setProperty("password", password);
    properties.setProperty("maxActive", String.valueOf(connectionQuantity));
    properties.setProperty("maxIdle", "10");
    properties.setProperty("maxWait", "30000");

    try {
      ds = BasicDataSourceFactory.createDataSource(properties);
    } catch (Exception e1) {
      // TODO Auto-generated catch block
      throw new FatalException(e1);
    }
    td = new ThreadLocal<Connection>();
  }

  @Override
  public PreparedStatement getPreparedStatement(String sql) {
    PreparedStatement ps = null;
    try {
      ps = td.get().prepareStatement(sql);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }

  @Override
  public PreparedStatement getPreparedStatementWithGeneratedReturn(String sql) {
    PreparedStatement ps = null;
    try {
      ps = td.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return ps;
  }



  @Override
  public synchronized void getConnection(boolean autoCommit) {
    try {
      Connection connection = ds.getConnection();
      connection.setAutoCommit(autoCommit);
      // if (td.get() == null) {
      // td.remove();
      // }
      td.set(connection);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void commitTransaction() throws SQLException {
    Connection connection = commitTransac();
    td.remove();
    connection.close();
  }


  private Connection commitTransac() throws SQLException {
    Connection connection = td.get();
    connection.commit();
    return connection;
  }

  @Override
  public void commitTransactionAndContinue() throws SQLException {
    commitTransac();
  }

  @Override
  public void rollbackTransaction() {
    try {
      Connection connection = td.get();
      connection.rollback();
      td.remove();
      connection.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
