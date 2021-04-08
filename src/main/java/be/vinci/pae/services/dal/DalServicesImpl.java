package be.vinci.pae.services.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.Config;

public class DalServicesImpl implements DalServices, DalBackendServices {

  private ThreadLocal<Connection> td;

  private Properties properties = new Properties();

  private BasicDataSource ds;

  String url;
  String user;
  String password;
  int connectionQuantity;

  /**
   * Constructor. Make the connection with the DB using the keys "url", "user", and "password" in a
   * properties file.
   */
  public DalServicesImpl() {
    url = Config.getStringProperty("url");
    user = Config.getStringProperty("user");
    password = Config.getStringProperty("password");
    connectionQuantity = Config.getIntProperty("connectionQuantity");

    properties.setProperty("driverClassName", "org.postgresql.Driver");
    properties.setProperty("url", url);
    properties.setProperty("username", user);
    properties.setProperty("password", password);

    properties.setProperty("maxTotal", String.valueOf(connectionQuantity));

    try {
      ds = BasicDataSourceFactory.createDataSource(properties);
    } catch (Exception e) {
      throw new FatalException(e);
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
  public void getBizzTransaction(boolean autoCommit) {
    try {
      if (td.get() != null) {
        throw new FatalException("Connection already given");
      }
      Connection connection = ds.getConnection();
      connection.setAutoCommit(autoCommit);
      td.set(connection);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void stopBizzTransaction() {
    Connection connection = td.get();
    td.remove();
    try {
      connection.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  @Override
  public void commitBizzTransaction() {
    Connection connection;
    try {
      connection = commitTransac();
      td.remove();
      connection.close();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  private Connection commitTransac() {
    Connection connection = td.get();
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return connection;
  }

  @Override
  public void commitTransactionAndContinue() {
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

  @Override
  public boolean hasTransaction() {
    Connection connection = td.get();
    return connection == null ? false : true;
  }


}
