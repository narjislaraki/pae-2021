package be.vinci.pae.domain.admin;

import java.util.List;
import be.vinci.pae.domain.user.UserDTO;


public interface AdminUCC {

  List<UserDTO> getUnvalidatedUsers();

}
