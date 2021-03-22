package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class AdminDAOImpl implements AdminDAO {

  @Inject
  private DalServices dalService;

  @Inject
  private UtilsDAO utilsDAO;

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
        UserDTO userDTO = utilsDAO.setUser(rs, user);
        list.add(userDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    System.out.println(list);
    return list;
  }


  @Override
  public void accept(int id) {
    try {
      String sql = "UPDATE pae.users SET is_validated = TRUE WHERE id_user = ?;";
      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void refuse(int id) {
    try {
      String sql = "DELETE FROM pae.users WHERE id_user = ?;";
      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void setRole(int id, String role) {

    try {
      String sql = "UPDATE pae.users u SET u.role = ? WHERE u.id_user = ?; ";

      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, role);
      ps.setInt(2, id);

      ps.execute();

    } catch (SQLException e) {
      throw new FatalException(e);
    }

  }
}
