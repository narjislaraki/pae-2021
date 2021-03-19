package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO {


  @Inject
  private DalServices dalService;

  PreparedStatement ps;

  @Override
  public void setCondition(Furniture furniture, String condition) {
    try {
      String sql = "UPDATE pae.furnitures f SET f.condition = ? WHERE f.id_furniture = ? ";
      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, condition);
      ps.setInt(2, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }



}
