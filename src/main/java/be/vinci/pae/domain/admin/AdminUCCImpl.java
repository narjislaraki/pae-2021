package be.vinci.pae.domain.admin;

import java.util.List;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dao.AdminDAO;
import jakarta.inject.Inject;

public class AdminUCCImpl implements AdminUCC {

  @Inject
  private AdminDAO adminDAO;

  @Override
  public List<UserDTO> getUnvalidatedUsers() {
    return adminDAO.getUnvalidatedUsers();
  }

  @Override
  public void acceptUser(int id) {
    adminDAO.accept(id);
  }

  @Override
  public void refuseUser(int id) {
    adminDAO.refuse(id);
  }
}
