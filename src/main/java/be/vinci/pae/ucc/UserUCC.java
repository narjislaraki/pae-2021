package be.vinci.pae.ucc;

import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.UserDTO;

public interface UserUCC {

  UserDTO connection(String email, String password);

  boolean registration(UserDTO user);

  boolean acceptUser(int id, String role);

  boolean deleteUser(int id);

  List<UserDTO> getUnvalidatedUsers();

  UserDTO getUserFromId(int id);

  List<UserDTO> getValidatedUsers();

  List<SaleDTO> getTransactionsBuyer(int id);

  List<FurnitureDTO> getTransactionsSeller(int id);
}
