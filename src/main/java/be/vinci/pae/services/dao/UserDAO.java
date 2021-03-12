package be.vinci.pae.services.dao;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;

public interface UserDAO {

  UserDTO getUserFromEmail(String email);

  UserDTO getUserFromUsername(String username);

  void addUser(User user);
}
