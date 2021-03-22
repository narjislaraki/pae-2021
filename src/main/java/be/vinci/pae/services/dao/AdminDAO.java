package be.vinci.pae.services.dao;

import java.util.List;
import be.vinci.pae.domain.user.UserDTO;


public interface AdminDAO {

  List<UserDTO> getUnvalidatedUsers();

}
