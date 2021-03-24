package be.vinci.pae.services.dao;

import java.sql.ResultSet;
import java.util.List;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;

public interface UserDAO {

  UserDTO getUserFromEmail(String email);

  UserDTO getUserFromUsername(String username);

  UserDTO getUserFromId(int id);

  void addUser(User user);

  void setRole(int id, String role);

  void deleteUser(int id);

  void accept(int id, String role);

  List<UserDTO> getUnvalidatedUsers();

  /**
   * Method to set a user from a resultset.
   * 
   * @param rs the resultset
   * @param user a null user
   * @return a userDTO
   */
  UserDTO setUser(ResultSet rs, UserDTO user);
}
