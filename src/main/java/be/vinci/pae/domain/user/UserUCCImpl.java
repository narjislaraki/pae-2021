package be.vinci.pae.domain.user;

import be.vinci.pae.api.exceptions.BusinessError;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;


  @Override
  public UserDTO connection(String email, String password) {
    User user = (User) userDAO.getUserFromEmail(email);

    if (user == null) {
      throw new BusinessError("Wrong credentials");
    } else if (!user.isValidated()) {
      throw new BusinessError("User is not validated");
      // The password is checked after the validation to limit processor usage
    } else if (!user.checkPassword(password)) {
      throw new BusinessError("Wrong credentials");
    }

    return user;

  }

}
