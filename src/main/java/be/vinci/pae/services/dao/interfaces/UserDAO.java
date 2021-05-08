package be.vinci.pae.services.dao.interfaces;

import java.util.List;
import be.vinci.pae.domain.interfaces.UserDTO;

public interface UserDAO {

  /**
   * Searching through the database for the user, using his email.
   * 
   * @param email the email of the user
   * @return the user found or null if none were found
   */
  UserDTO getUserFromEmail(String email);

  /**
   * Searching through the database for the user, using his username.
   * 
   * @param username the username of the user
   * @return the user found or null if none were found
   */
  UserDTO getUserFromUsername(String username);

  /**
   * Searching through the database for the user, using his id.
   * 
   * @param id the id of the user
   * @return the user found or null if none were found
   */
  UserDTO getUserFromId(int id);

  /**
   * Add the user passed as a parameter in the database after having set its "is_validated" to
   * false.
   * 
   * @param user the user to add
   */
  void addUser(UserDTO user);

  /**
   * Update the role of the user whose id is passed in parameter with the role passed in parameter.
   * 
   * @param id the id of the user
   * @param role the new role
   */
  void setRole(int id, String role);

  /**
   * Update the user whose id is passed as a parameter by setting its "is_validated" field to true
   * and its role with the role passed as a parameter.
   * 
   * @param id the id of the user to accept
   * @param role the new role
   * @return true if the executeUpdate method returns 1, false otherwise
   */
  boolean acceptUser(int id, String role);

  /**
   * Delete the user whose id is the one passed in parameter.
   * 
   * @param id the user to delete
   * @return true if the executeUpdate method returns 1, false otherwise
   */
  boolean deleteUser(int id);

  /**
   * Get the list of users whose "is_validated" field is false.
   * 
   * @return the list of unvalidated users
   */
  List<UserDTO> getUnvalidatedUsers();

  /**
   * Get the list of users whose "is_validated" field is true.
   * 
   * @returnthe list of validated users
   */
  List<UserDTO> getValidatedUsers();

}
