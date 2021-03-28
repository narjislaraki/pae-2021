package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class UserDAOImpl implements UserDAO {

  @Inject
  private DalBackendServices dalBackendService;

  @Inject
  private UserFactory userFactory;

  @Inject
  private AddressDAO addressDAO;

  PreparedStatement ps;


  /**
   * Searching through the database for the user, using his email.
   * 
   * @param email the email
   * @return the user if he exists, otherwise null
   */
  public UserDTO getUserFromEmail(String email) {
    // TODO PS -> attribut?
    // TODO fetch de l'adresse aussi
    UserDTO user = null;
    try {
      ps = dalBackendService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.email = ?;");


      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }

  /**
   * Searching through the database for a user, using his email and his username.
   * 
   * @param email the email
   * @return the user if he exists, otherwise null
   */
  @Override
  public boolean existsUserFromEmailOrUsername(String email, String username) {
    boolean res = false;
    try {
      ps = dalBackendService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.email = ? OR u.username = ?;");


      ps.setString(1, email);
      ps.setString(2, username);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        res = true;
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return res;
  }



  /**
   * Searching through the database for the user, using his username.
   * 
   * @param username the username
   * @return the user if he exists, otherwise throws error
   */
  @Override
  public UserDTO getUserFromUsername(String username) {
    // TODO PS -> attribut?
    // TODO fetch de l'adresse aussi
    UserDTO user = null;

    try {
      ps = dalBackendService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.username = ?;");


      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }

  @Override
  public void addUser(UserDTO user) {
    try {
      String sql = "INSERT INTO pae.users VALUES(default, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
      ps = dalBackendService.getPreparedStatement(sql);
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
      ps = dalBackendService.getPreparedStatement(
          "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
              + "u.registration_date, u.is_validated, u.password, u.address "
              + "FROM pae.users u WHERE u.id_user= ?;");


      ps.setInt(1, id);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        user = setUser(rs, user);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }

  @Override
  public List<UserDTO> getUnvalidatedUsers() {
    List<UserDTO> list = new ArrayList<UserDTO>();
    try {
      String sql = "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
          + "u.registration_date, u.is_validated, u.password, u.address "
          + "FROM pae.users u WHERE u.is_validated = false;";
      ps = dalBackendService.getPreparedStatement(sql);
      ResultSet rs = ps.executeQuery();
      UserDTO user = null;
      while (rs.next()) {
        UserDTO userDTO = setUser(rs, user);
        list.add(userDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    System.out.println(list);
    return list;
  }

  @Override
  public void accept(int id, String role) {
    try {
      String sql = "UPDATE pae.users SET is_validated = TRUE, role = ? WHERE id_user = ?;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, role);
      ps.setInt(2, id);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean deleteUser(int id) {
    try {
      String sql = "DELETE FROM pae.users WHERE id_user = ?;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return true;
      }

    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return false;
  }

  @Override
  public void setRole(int id, String role) {

    try {
      String sql = "UPDATE pae.users u SET u.role = ? WHERE u.id_user = ?; ";

      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, role);
      ps.setInt(2, id);

      ps.execute();

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }

  /**
   * Method to set a user from a resultset.
   * 
   * @param rs the resultset
   * @param user a null user
   * @return a userDTO
   */
  @Override
  public UserDTO setUser(ResultSet rs, UserDTO user) {
    try {
      user = userFactory.getUserDTO();
      user.setId(rs.getInt(1));
      user.setUsername(rs.getString(2));
      user.setLastName(rs.getString(3));
      user.setFirstName(rs.getString(4));
      user.setEmail(rs.getString(5));
      user.setRole(rs.getString(6));
      user.setRegistrationDate(rs.getTimestamp(7).toLocalDateTime());
      user.setValidated(rs.getBoolean(8));
      user.setPassword(rs.getString(9));
      Address address = addressDAO.getAddress(rs.getInt(10)); // TODO à revoir
      user.setAddress(address);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }


}

