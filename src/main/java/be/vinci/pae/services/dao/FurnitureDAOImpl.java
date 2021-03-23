package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.addresses.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO {


  @Inject
  private DalServices dalService;

  PreparedStatement ps;

  public FurnitureDTO getFurnitureById(int id) {
    FurnitureDTO furniture = null;
    try {
      String sql = ("SELECT id_furniture, condition, description, purchase_price, pick_up_date,"
          + " store_deposit, deposit_date, offered_selling_price, id_type, request_visit, seller"
          + " FROM pae.furnitures WHERE id_furniture = ?;");
      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        furniture = setFurniture(rs, furniture);
      }
    } catch (SQLException e) {
      // TODO: handle exception
    }
    return null;
  }

  private FurnitureDTO setFurniture(ResultSet rs, FurnitureDTO furniture) {
    // TODO Auto-generated method stub
    return null;
  }

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

  @Override
  public void introduceOption(int numberOfDay) {
    // TODO Auto-generated method stub

  }

  @Override
  public void cancelOption(String cancellationReason) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateSentToWorkshop(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateDropOfStore(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateOfferedForSale(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void withdrawSale(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<FurnitureDTO> SeeFurnitureList() {
    // TODO Auto-generated method stub

    return null;
  }

  @Override
  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {
    // TODO Auto-generated method stub

  }



}
