package be.vinci.pae.domain.user;

import java.util.List;

import be.vinci.pae.domain.sale.SaleDTO;

public interface UserUCC {

  UserDTO connection(String email, String password);

  boolean registration(UserDTO user);

  boolean acceptUser(int id, String role);

  boolean deleteUser(int id);

  List<UserDTO> getUnvalidatedUsers();

  UserDTO getUserFromId(int id);

  List<UserDTO> getValidatedUsers();

  List<SaleDTO> getTransactionsBuyer(int id);

	List<SaleDTO> getTransactionsSeller(int id);
}
