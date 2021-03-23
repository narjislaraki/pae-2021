package be.vinci.pae.domain.user;

import java.util.List;

import be.vinci.pae.api.exceptions.BusinessException;
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
  public List<UserDTO> getUnvalidatedUsers() {
    return userDAO.getUnvalidatedUsers();
  }

  @Override
  public void acceptUser(int id, String role) {
    if (!role.equals("admin") && !role.equals("client") && !role.equals("antiquaire")) {
      throw new BusinessException("Invalid role");
    }
    if (id < 0) {
      throw new BusinessException("Invalid id");
    }
    userDAO.accept(id, role);
  }

  @Override
  public void refuseUser(int id) {
    userDAO.refuse(id);
  }

  public UserDTO getUserFromId(int id) {
    return userDAO.getUserFromId(id);
  }

}
