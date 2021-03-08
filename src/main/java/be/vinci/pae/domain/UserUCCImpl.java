package be.vinci.pae.domain;

import be.vinci.pae.services.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;


  @Override
  public UserDTO connection(String email, String password) {
    UserDTO userDTO = userDAO.getUser(email);
    User user = (User) userDTO;
    System.out.println(user.getUsername() + " " + user.getEmail());
    if (user == null || !user.checkPassword(password) || !user.isValidated())
      return null;

    return userDTO;
  }
}
