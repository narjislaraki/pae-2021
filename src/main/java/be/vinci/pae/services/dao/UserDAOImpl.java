package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class UserDAOImpl implements UserDAO {

  @Inject
  private DalServices dalService;
  @Inject
  private UserFactory userFactory;

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
      user = setUser(rs, user);

    } catch (SQLException e) {
      e.printStackTrace();
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
      user = setUser(rs, user);
    } catch (SQLException e) {
      e.printStackTrace();
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
      ps.setBoolean(7, user.isValidated());
      ps.setString(8, user.getPassword());
      ps.setInt(9, user.getAddress());
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private UserDTO setUser(ResultSet rs, UserDTO user) {
    try {
      while (rs.next()) {
        user = userFactory.getUserDTO(); // TODO bon endroit?
        user.setId(rs.getInt(1));
        user.setUsername(rs.getString(2));
        user.setLastName(rs.getString(3));
        user.setFirstName(rs.getString(4));
        user.setEmail(rs.getString(5));
        user.setRole(rs.getString(6));
        user.setRegistrationDate(rs.getTimestamp(7).toLocalDateTime());
        user.setValidated(rs.getBoolean(8));
        user.setPassword(rs.getString(9));
        user.setAddress(rs.getInt(10));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return user;
  }
}
