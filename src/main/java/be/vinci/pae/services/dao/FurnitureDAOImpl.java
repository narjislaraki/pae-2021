package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.vinci.pae.api.exceptions.FatalException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.furniture.OptionDTO.State;
import be.vinci.pae.domain.furniture.OptionFactory;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class FurnitureDAOImpl implements FurnitureDAO {


  @Inject
  private DalBackendServices dalBackendService;
  @Inject
  private FurnitureFactory furnitureFactory;
  @Inject
  private OptionFactory optionFactory;

  PreparedStatement ps;

  /**
   * Searching through the database for the furniture, using his id.
   * 
   * @param id the id
   * @return the furniture if he exists, otherwise null
   */
  @Override
  public FurnitureDTO getFurnitureById(int id) {
    FurnitureDTO furniture = null;
    try {
      String sql = "SELECT id_furniture, condition, description, purchase_price, pick_up_date, "
          + "store_deposit, deposit_date,"
          + " offered_selling_price, id_type, request_visit, seller, favorite_photo"
          + " FROM pae.furnitures WHERE id_furniture = ?;";
      ps = dalBackendService.getPreparedStatement(sql);
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

  /**
   * Calculate and return the number of days booked by the user for a furniture.
   * 
   * @param idFurniture the id of the furniture
   * @param idUser the id of the user
   */
  @Override
  public int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser) {
    int number = -1;
    try {
      String sql =
          "SELECT SUM(option_term) FROM pae.options WHERE id_furniture = ? AND id_user = ?";
      ps = dalBackendService.getPreparedStatement(sql);
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
      furniture.setPurchasePrice(rs.getDouble(4));
      furniture
          .setPickUpDate(rs.getTimestamp(5) == null ? null : rs.getTimestamp(5).toLocalDateTime());
      furniture.setStoreDeposit(rs.getBoolean(6));
      furniture
          .setDepositDate(rs.getTimestamp(7) == null ? null : rs.getTimestamp(7).toLocalDateTime());
      furniture.setOfferedSellingPrice(rs.getDouble(8));
      furniture.setTypeId(rs.getInt(9));
      furniture.setRequestForVisitId(rs.getInt(10));
      furniture.setSellerId(rs.getInt(11));
      furniture.setFavouritePhotoId(rs.getInt(12));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return furniture;
  }

  private OptionDTO setOption(ResultSet rs, OptionDTO option) {
    try {
      option = optionFactory.getOptionDTO();
      option.setId(rs.getInt(1));
      option.setDate(rs.getTimestamp(2) == null ? null : rs.getTimestamp(2).toLocalDateTime());
      option.setOptionTerm(rs.getInt(3));
      option.setCancellationReason(rs.getString(4));
      option.setCondition(rs.getString(5));
      option.setIdUser(rs.getInt(6));
      option.setIdFurniture(rs.getInt(7));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return option;
  }


  @Override
  public void setFurnitureCondition(Furniture furniture, Condition condition) {
    try {
      String sql = "UPDATE pae.furnitures SET condition = ? WHERE id_furniture = ? ;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, condition.toString());
      ps.setInt(2, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }


  @Override
  public void introduceOption(int optionTerm, int idUser, int idFurniture) {
    try {
      String sql = "INSERT INTO pae.options VALUES(DEFAULT, ?, ?, null, ?, ?, ?);";
      ps = dalBackendService.getPreparedStatement(sql);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      Timestamp date = Timestamp.valueOf(LocalDateTime.now().format(formatter));
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
  public void indicateFurnitureUnderOption(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    setFurnitureCondition(furniture, Condition.SOUS_OPTION);
  }

  @Override
  public int cancelOption(String cancellationReason, int idOption) {
    try {

      String sql = "UPDATE pae.options SET condition = ?, cancellation_reason = ? "
          + "WHERE id_option = ? RETURNING id_furniture;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, "annulée");
      ps.setString(2, cancellationReason);
      ps.setInt(3, idOption);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return -1;
  }

  @Override
  public OptionDTO getOption(int idFurniture) {
    OptionDTO option = null;
    try {
      String sql = "SELECT id_option, date, option_term, cancellation_reason, "
          + "condition, id_user, id_furniture"
          + " FROM pae.options WHERE id_furniture = ? AND condition = ?;";

      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, idFurniture);
      ps.setString(2, State.EN_COURS.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        option = setOption(rs, option);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return option;
  }


  @Override
  public void indicateSentToWorkshop(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    setFurnitureCondition(furniture, Condition.EN_RESTAURATION);

  }

  @Override
  public void indicateDropInStore(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    try {
      String sql = "UPDATE pae.furnitures SET condition = ?, store_deposit = ?, "
          + "deposit_date = ? WHERE id_furniture = ? ;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, Condition.DEPOSE_EN_MAGASIN.toString());
      ps.setBoolean(2, true);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      Timestamp date = Timestamp.valueOf(LocalDateTime.now().format(formatter));
      ps.setTimestamp(3, date);
      ps.setInt(4, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    setFurnitureCondition(furniture, Condition.DEPOSE_EN_MAGASIN);
  }

  @Override
  public void indicateOfferedForSale(Furniture furniture, double price) {
    try {
      String sql = "UPDATE pae.furnitures SET condition = ?, offered_selling_price = ? "
          + "WHERE id_furniture = ? ;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, Condition.EN_VENTE.toString());
      ps.setDouble(2, price);
      ps.setInt(3, furniture.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void withdrawSale(int id) {
    Furniture furniture = (Furniture) getFurnitureById(id);
    setFurnitureCondition(furniture, Condition.RETIRE);

  }

  @Override
  public List<FurnitureDTO> getFurnitureList() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    try {
      String sql = "SELECT id_furniture, condition, description, purchase_price, "
          + "pick_up_date, store_deposit, deposit_date, "
          + "offered_selling_price, id_type, request_visit, seller, favorite_photo "
          + "FROM pae.furnitures;";
      ps = dalBackendService.getPreparedStatement(sql);
      ResultSet rs = ps.executeQuery();
      FurnitureDTO furniture = null;
      while (rs.next()) {
        FurnitureDTO furnitureDTO = setFurniture(rs, furniture);
        list.add(furnitureDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public List<FurnitureDTO> getPublicFurnitureList() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    try {
      String sql = "SELECT id_furniture, condition, description, purchase_price, "
          + "pick_up_date, store_deposit, deposit_date, "
          + "offered_selling_price, id_type, request_visit, seller, favorite_photo "
          + "FROM pae.furnitures WHERE condition = ? OR condition = ?;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setString(1, Condition.EN_VENTE.toString());
      ps.setString(2, Condition.SOUS_OPTION.toString());
      ResultSet rs = ps.executeQuery();
      FurnitureDTO furniture = null;
      while (rs.next()) {
        FurnitureDTO furnitureDTO = setFurniture(rs, furniture);
        list.add(furnitureDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }


  @Override
  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {
    // TODO Auto-generated method stub

  }

  /**
   * Returns the type of the furniture based on its id.
   * 
   * @param id the id of the type
   */
  @Override
  public String getFurnitureTypeById(int id) {
    String label = "";
    try {
      String sql = "SELECT label FROM pae.types_of_furnitures WHERE id_type = ?;";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        label = rs.getString(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return label;
  }

  /**
   * Returns the favourite photo based on its id.
   * 
   * @param id the id of the photo
   */
  public String getFavouritePhotoById(int id) {
    String favouritePhoto = "";
    try {
      String sql = "SELECT photo FROM pae.photos WHERE id_photo = ?";
      ps = dalBackendService.getPreparedStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        favouritePhoto = rs.getString(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return favouritePhoto;
  }
}
