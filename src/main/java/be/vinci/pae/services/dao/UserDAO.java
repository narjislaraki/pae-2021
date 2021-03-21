package be.vinci.pae.services.dao;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;

public interface UserDAO {

  UserDTO getUserFromEmail(String email);

  UserDTO getUserFromUsername(String username);

  UserDTO getUserFromId(int id);

  void addUser(User user);

  void accept(User user);

  void refuse(User user);

  void setRole(User user, String role);
}
