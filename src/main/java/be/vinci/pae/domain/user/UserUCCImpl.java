package be.vinci.pae.domain.user;

import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;


  @Override
  public UserDTO connection(String email, String password) {
    User user = (User) userDAO.getUserFromEmail(email);

    if (user == null) {
      throw new UnauthorizedException("Wrong credentials");
    } else if (!user.isValidated()) {
      throw new UnauthorizedException("User is not validated");
      // The password is checked after the validation to limit processor usage
    } else if (!user.checkPassword(password)) {
      throw new UnauthorizedException("Wrong credentials");
    }

    return user;

  }

  @Override
  public void confirmRegistration(User user, String role) {

    userDAO.accept(user);
    if (!role.equals("")) {
      userDAO.setRole(user, role);
    }

  }

  @Override
  public void refuseRegistration(User user) {
    userDAO.refuse(user);
  }



}
