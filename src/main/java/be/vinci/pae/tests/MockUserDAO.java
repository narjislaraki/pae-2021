package be.vinci.pae.tests;

import java.sql.ResultSet;
import java.util.List;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dao.UserDAO;

public class MockUserDAO implements UserDAO {

  @Override
  public UserDTO getUserFromEmail(String email) {
    UserDTO user = null;
    if (email.equals("test@test.com")) {
      user = UserDistributor.getGoodValidatedUser();
    }
    return user;
  }

  @Override
  public UserDTO getUserFromUsername(String username) {
    UserDTO user = null;
    if (username.equals("test")) {
      user = UserDistributor.getGoodValidatedUser();
    }
    return user;
  }

  @Override
  public UserDTO getUserFromId(int id) {
    UserDTO user = null;
    if (id == 1) {
      user = UserDistributor.getGoodValidatedUser();
    }
    return user;
  }

  @Override
  public void addUser(User user) {

  }

  @Override
  public void setRole(int id, String role) {
    // TODO Auto-generated method stub

  }

  @Override
  public void refuse(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void accept(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<UserDTO> getUnvalidatedUsers() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserDTO setUser(ResultSet rs, UserDTO user) {
    // TODO Auto-generated method stub
    return null;
  }



}
