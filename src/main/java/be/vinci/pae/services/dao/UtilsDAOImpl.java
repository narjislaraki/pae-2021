package be.vinci.pae.services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import jakarta.inject.Inject;

public class UtilsDAOImpl implements UtilsDAO {

  @Inject
  private UserFactory userFactory;


  @Inject
  private AddressDAO addressDAO;

  /**
   * Method to set a user from a resultset.
   * 
   * @param rs the resultset
   * @param user a null user
   * @return a userDTO
   */
  @Override
  public UserDTO setUser(ResultSet rs, UserDTO user) {
    try {
      user = userFactory.getUserDTO();
      user.setId(rs.getInt(1));
      user.setUsername(rs.getString(2));
      user.setLastName(rs.getString(3));
      user.setFirstName(rs.getString(4));
      user.setEmail(rs.getString(5));
      user.setRole(rs.getString(6));
      user.setRegistrationDate(rs.getTimestamp(7).toLocalDateTime());
      user.setValidated(rs.getBoolean(8));
      user.setPassword(rs.getString(9));
      Address address = addressDAO.getAddress(rs.getInt(10));
      user.setAddress(address);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return user;
  }
}
