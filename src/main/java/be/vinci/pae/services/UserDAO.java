package be.vinci.pae.services;

import be.vinci.pae.domain.UserDTO;

public interface UserDAO {

  UserDTO getUser(String email);

}
