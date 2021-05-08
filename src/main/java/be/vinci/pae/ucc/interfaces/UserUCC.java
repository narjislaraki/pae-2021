package be.vinci.pae.ucc.interfaces;

import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.UserDTO;

public interface UserUCC {

  /**
   * Connect a user if the credential are correct and if the user is validated.
   * 
   * @param email the email introduce
   * @param password the password introduce
   * @return the user to be connected
   */
  UserDTO connection(String email, String password);

  /**
   * Register a user with the information entered.
   * 
   * @param user the user to be create
   * @return true if the user is create, otherwise false
   */
  boolean registration(UserDTO user);

  /**
   * Validated a user and set his role.
   * 
   * @param id the id of the user
   * @param role the role to be assign
   * @return true if the user has been accepted, otherwise false
   */
  boolean acceptUser(int id, String role);

  /**
   * Delete the user associate to the id.
   * 
   * @param id of the user
   * @return true if the user has been deleted, otherwise false
   */
  boolean deleteUser(int id);

  /**
   * Get the list of the unvalidated users.
   * 
   * @return the list of the unvalitated users
   */
  List<UserDTO> getUnvalidatedUsers();

  /**
   * Get a user by his id.
   * 
   * @param id the id of the user
   * @return the user associate to the id, otherwise throw an exeception
   */
  UserDTO getUserFromId(int id);

  /**
   * Get the list of validated users.
   * 
   * @return the list of validated users
   */
  List<UserDTO> getValidatedUsers();

  /**
   * Get a list of sale by his buyer id.
   * 
   * @param id the id of the user
   * @return the list of sale
   */
  List<SaleDTO> getTransactionsBuyer(int id);

  /**
   * Get by and id user, the list of furniture sold by this user.
   * 
   * @param id the id of the user.
   * @return the list of furniture sold
   */
  List<FurnitureDTO> getTransactionsSeller(int id);
}
