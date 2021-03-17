package be.vinci.pae.domain.user;

import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;


  @Override
  public UserDTO connection(String email, String password) {
    User user = (User) userDAO.getUserFromEmail(email);

    String errorMessage = null;

    if (user == null) {
      errorMessage = "Wrong credentials";
    } else if (!user.isValidated()) {
      errorMessage = "User is not validated";
      // The password is checked after the validation to limit processor usage
    } else if (!user.checkPassword(password)) {
      errorMessage = "Wrong credentials";
    }

    if (errorMessage != null) {
      throw new FatalException(errorMessage, Status.UNAUTHORIZED);
    }

    return user;

  }

}
