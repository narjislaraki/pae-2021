package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.services.dal.DalServices;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO {


  @Inject
  private DalServices dalService;
  @Inject
  private FurnitureFactory furnitureFactory;

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
      throw new FatalException(e);
    }
    return furniture;
  }

  // A REVOIR (renvoyer un int ou une option
  public int getNumberOfReservation(int idFurniture, int idUser) {
    int number = -1;
    try {
      String sql =
          ("SELECT SUM(o.options_term) FROM pae.options o WHERE f.id_furniture = ? AND u.id_user = ?");
      ps = dalService.getPreparedStatement(sql);
      ps.setInt(1, idFurniture);
      ps.setInt(2, idUser);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        number = rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return number;
  }

  private FurnitureDTO setFurniture(ResultSet rs, FurnitureDTO furniture) {
    try {
      furniture = furnitureFactory.getFurnitureDTO();
      furniture.setId(rs.getInt(1));
      furniture.setCondition(rs.getString(2));
      furniture.setDescription(rs.getString(3));
      furniture.setType(rs.getInt(9));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return furniture;
  }


  @Override
  public void setCondition(Furniture furniture, Condition condition) {
    try {
      String sql = "UPDATE pae.furnitures f SET f.condition = ? WHERE f.id_furniture = ? ;";
      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, condition.toString());
      ps.setInt(2, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  /*
   * public void getCondition(Furniture furniture) { try { String sql = "SELECT f.condition FROM pae.furnitures f WHERE f.id_furniture = ?;"; ps =
   * dalService.getPreparedStatement(sql); } }
   */



  @Override
  public void introduceOption(int optionTerm, int idUser, int idFurniture) {
    try {
      String sql = "INSERT INTO pae.options VALUES(DEFAULT, ?, ?, null, ?, ?, ?;";
      ps = dalService.getPreparedStatement(sql);
      Timestamp date = Timestamp.valueOf(LocalDateTime.now().toString());
      ps.setTimestamp(1, date);
      ps.setInt(2, optionTerm);
      ps.setString(3, "en cours");
      ps.setInt(4, idUser);
      ps.setInt(5, idFurniture);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void cancelOption(String cancellationReason, int idOption) {
    try {
      String sql =
          "UPDATE pae.options SET state = ?,cancellation_reason = ? " + "WHERE id_option = ?;";
      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, "annulée");
      ps.setString(2, cancellationReason);
      ps.setInt(3, idOption);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void indicateSentToWorkshop(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    setCondition(furniture, Condition.EN_RESTAURATION);

  }

  @Override
  public void indicateDropOfStore(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    setCondition(furniture, Condition.DEPOSE_EN_MAGASIN);
  }

  @Override
  public void indicateOfferedForSale(Furniture furniture, double price) {
    try {
      String sql =
          "UPDATE pae.furnitures f SET f.condition = ?, f.offered_selling_price = ? WHERE f.id_furniture = ? ;";
      ps = dalService.getPreparedStatement(sql);
      ps.setString(1, Condition.EN_VENTE.toString());
      ps.setDouble(2, price);
      ps.setInt(3, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      // TODO
      e.printStackTrace();
    }
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
