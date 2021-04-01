package be.vinci.pae.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
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
    dalServices.getBizzTransaction(false);
    User user = (User) userDAO.getUserFromEmail(email);
    dalServices.commitTransaction();
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
    dalServices.getBizzTransaction(false);
    boolean alreadyPresent =
        userDAO.existsUserFromEmailOrUsername(user.getEmail(), user.getUsername());
    if (alreadyPresent) {
      dalServices.rollbackTransaction();
      throw new BusinessException("This email or username is already in use");
    }

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
    dalServices.getBizzTransaction(true);
    List<UserDTO> list = userDAO.getUnvalidatedUsers();
    return list;
  }

  @Override
  public void acceptUser(int id, String role) {
    if (role == null) {
      throw new BusinessException("Role is needed");
    }
    if (!role.equals("admin") && !role.equals("client") && !role.equals("antiquaire")) {
      throw new BusinessException("Invalid role");
    }
    if (id < 0) {
      throw new BusinessException("Invalid id");
    }
    dalServices.getBizzTransaction(true);
    userDAO.accept(id, role);
    dalServices.stopBizzTransaction();
  }

  @Override
  public boolean deleteUser(int id) {
    dalServices.getBizzTransaction(true);
    if (!userDAO.deleteUser(id)) {
      dalServices.stopBizzTransaction();
      throw new BusinessException("Invalid id");
    }
    dalServices.stopBizzTransaction();
    return true;
  }

  @Override
  public UserDTO getUserFromId(int id) {
    dalServices.getBizzTransaction(true);
    UserDTO user = userDAO.getUserFromId(id);
    dalServices.stopBizzTransaction();
    return user;
  }

}
