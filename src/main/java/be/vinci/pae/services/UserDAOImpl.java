package be.vinci.pae.services;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.inject.Inject;

public class UserDAOImpl implements UserDAO {

  @Inject
  private DalServices dalService;

  @Inject
  private UserFactory userFactory;

  public UserDAOImpl() {
    // TODO Beware, the connection has to be done in an extern class
  }

  /**
   * Searching through thed database for the user, using his username
   * 
   * @param username the username
   * @return the user if he exists, otherwise null
   */
  @Override
  public UserDTO getUser(String username) {
    // TODO PS -> attribut?
    // TODO retrait astérisque
    // TODO fetch de l'adresse aussi
    UserDTO user = null;

    try {
      PreparedStatement ps = dalService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.username = ?;");


      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        user = userFactory.getUserDTO(); // TODO bon endroit?
        user.setId(rs.getInt(1));
        user.setUsername(rs.getString(2));
        user.setLastName(rs.getString(3));
        user.setFirstName(rs.getString(4));
        user.setEmail(rs.getString(5));
        user.setRole(rs.getString(6));
        user.setRegistrationDate(rs.getTimestamp(7).toLocalDateTime());
        user.setIsValidated(rs.getBoolean(8));
        user.setPassword(rs.getString(9));
        user.setAddress(rs.getInt(10));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * Searching through the database for the user, using his id
   * 
   * @param id the user's id
   * @return the user if he exists, otherwise null
   */
  @Override
  public User getUser(int id) {
    // TODO Auto-generated method stub
    return null;
  }


  /**
   * Adds a user in the databas.
   * 
   * @param user the user
   */
  @Override
  public void addUser(User user) {
    // TODO Auto-generated method stub --> return quelque chose type boolean ou plus complexe?
  }



}
