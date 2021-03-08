package be.vinci.pae.services;

import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserDTO;

public interface UserDAO {

  UserDTO getUser(String email);

  UserDTO getUser(int id);

  void addUser(User user);

}
