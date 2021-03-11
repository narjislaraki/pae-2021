package be.vinci.pae.services.DAO;

import be.vinci.pae.domain.user.UserDTO;

public interface UserDAO {

  UserDTO getUser(String email);

}
