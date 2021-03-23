package be.vinci.pae.services.dao;

import java.sql.ResultSet;
import be.vinci.pae.domain.user.UserDTO;

public interface UtilsDAO {

  UserDTO setUser(ResultSet rs, UserDTO user);
}
