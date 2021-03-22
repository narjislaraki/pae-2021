package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.exception.FatalException;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class UserDAOImpl implements UserDAO {

  @Inject
  private DalServices dalService;
  @Inject
  private UserFactory userFactory;
  @Inject
  private AddressFactory addressFactory;

  PreparedStatement ps;


  /**
   * Searching through the database for the user, using his email.
   * 
   * @param email the email
   * @return the user if he exists, otherwise null
   */
  @Override
  public UserDTO getUserFromEmail(String email) {
    // TODO PS -> attribut?
    // TODO fetch de l'adresse aussi
    UserDTO user = null;

    try {
      ps = dalService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.email = ?;");


      ps.setString(1, email);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = UtilsDAO.setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }



  /**
   * Searching through the database for the user, using his username.
   * 
   * @param username the username
   * @return the user if he exists, otherwise null
   */
  @Override
  public UserDTO getUserFromUsername(String username) {
    // TODO PS -> attribut?
    // TODO fetch de l'adresse aussi
    UserDTO user = null;

    try {
      ps = dalService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.username = ?;");


      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = UtilsDAO.setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }

  @Override
  public void addUser(User user) {
    try {
      String sql = "INSERT INTO pae.users VALUES(default, ?, ?, ?, ?, ?::pae.roles, ?, ?, ?, ?);";
      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getLastName());
      ps.setString(3, user.getFirstName());
      ps.setString(4, user.getEmail());
      ps.setString(5, user.getRole().toString().toLowerCase());
      Timestamp registrationDate = Timestamp.valueOf(user.getRegistrationDate());
      ps.setTimestamp(6, registrationDate);
      ps.setBoolean(7, false);
      ps.setString(8, user.getPassword());
      ps.setInt(9, user.getAddress().getId());
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  /**
   * Searching through the database for the user, using his id.
   * 
   * @param id the id
   * @return the user if he exists, otherwise null
   */
  @Override
  public UserDTO getUserFromId(int id) {
    // TODO PS -> attribut?
    // TODO fetch de l'adresse aussi
    UserDTO user = null;

    try {
      ps = dalService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.id= ?;");


      ps.setInt(1, id);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = UtilsDAO.setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }



  @Override
  public void accept(User user) {

    try {
      String sql = "UPDATE pae.users u SET u.is_validated = TRUE WHERE u.id_user = ?;";
      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, user.getId());
      ps.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void refuse(User user) {

    try {
      String sql = "DELETE FROM pae.users u WHERE u.id_user = ?;";

      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, user.getId());
      ps.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void setRole(User user, String role) {

    try {
      String sql = "UPDATE pae.users u SET u.role = ? WHERE u.id_user = ?; ";

      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, role);
      ps.setInt(2, user.getId());

      ps.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}

