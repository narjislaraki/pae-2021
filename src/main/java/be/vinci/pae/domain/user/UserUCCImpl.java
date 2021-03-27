package be.vinci.pae.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.api.exceptions.BusinessException;
import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

public class UserUCCImpl implements UserUCC {


  @Inject
  private UserDAO userDAO;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private DalServices dalServices;


  @Override
  public UserDTO connection(String email, String password) {
    dalServices.getConnection(true);
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
  public void registration(UserDTO user) {
    dalServices.getConnection(false);
    boolean alreadyPresent =
        userDAO.existsUserFromEmailOrUsername(user.getEmail(), user.getUsername());
    if (alreadyPresent) {
      throw new BusinessException("This email or username is already in use");
    }
    dalServices.commitTransactionAndContinue();

    user.getAddress().setId(addressDAO.addAddress(user.getAddress()));


    user.setPassword(((User) user).hashPassword(user.getPassword()));
    user.setValidated(false);
    user.setRegistrationDate(LocalDateTime.now());
    user.setRole("client");

    userDAO.addUser(user);

    dalServices.commitTransaction();

  }


  @Override
  public List<UserDTO> getUnvalidatedUsers() {
    dalServices.getConnection(true);
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
    dalServices.getConnection(true);
    userDAO.accept(id, role);
  }

  @Override
  public boolean deleteUser(int id) {
    if (!userDAO.deleteUser(id)) {
      throw new BusinessException("Invalid id");
    }
    return true;
  }

  public UserDTO getUserFromId(int id) {
    return userDAO.getUserFromId(id);
  }

}
