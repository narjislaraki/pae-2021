package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class AdminDAOImpl implements AdminDAO {

  @Inject
  private DalServices dalService;

  @Inject
  private UserFactory userFactory;

  PreparedStatement ps;


  @Override
  public List<UserDTO> getUnvalidatedUsers() {
    List<UserDTO> list = new ArrayList<UserDTO>();
    try {
      String sql = "SELECT u.id_user, u.username, u.last_name, u.first_name, u.email, u.role, "
          + "u.registration_date, u.is_validated, u.password, u.address "
          + "FROM pae.users u WHERE u.is_validated = false;";
      ps = dalService.getPreparedStatement(sql);
      ResultSet rs = ps.executeQuery();
      UserDTO user = null;
      while (rs.next()) {
        UtilsDAO.setUser(rs, user);
        list.add(user);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println(list);
    return list;
  }
}
