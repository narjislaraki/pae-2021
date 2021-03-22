package be.vinci.pae.services.dao;

import java.util.List;
import be.vinci.pae.domain.user.UserDTO;


public interface AdminDAO {

  List<UserDTO> getUnvalidatedUsers();

  void accept(int id);

  void refuse(int id);

  void setRole(int id, String role);

}
