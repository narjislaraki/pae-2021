package be.vinci.pae.domain;

import be.vinci.pae.services.UserDAO;

import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;


  @Override
  public UserDTO connection(String username) {
    UserDTO userDTO = userDAO.getUser(username);
    return userDTO;
  }
}
