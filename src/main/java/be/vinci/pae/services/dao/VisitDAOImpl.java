package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitDTO.VisitCondition;
import be.vinci.pae.domain.visit.VisitFactory;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.services.dal.DalBackendServices;
import jakarta.inject.Inject;

public class VisitDAOImpl implements VisitDAO {

  PreparedStatement ps;

  @Inject
  private DalBackendServices dalBackendServices;

  @Inject
  private VisitFactory visitFactory;

  @Inject
  private FurnitureFactory furnitureFactory;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private UserDAO userDAO;

  /**
   * Method to set a visit from a resultset.
   * 
   * @param rs the resultset
   * @param visit a null visit
   * @return a visitDTO
   */
  public VisitDTO setVisit(ResultSet rs, VisitDTO visit) {
    try {
      visit = visitFactory.getVisitDTO();
      visit.setIdRequest(rs.getInt(1));
      visit.setTimeSlot(rs.getString(2));
      visit.setVisitCondition(rs.getString(3));
      if (rs.getString(4) != null) {
        visit.setExplanatoryNote(rs.getString(4));
      }
      if (rs.getTimestamp(5) != null) {
        visit.setScheduledDateTime(rs.getTimestamp(5).toLocalDateTime());
      }
      User client = (User) userDAO.getUserFromId(rs.getInt(7));
      visit.setClient(client);
      Address address = addressDAO.getAddress(rs.getInt(6));
      visit.setWarehouseAddress(address);
      visit.setIdClient(rs.getInt(7));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return visit;
  }

  @Override
  public List<VisitDTO> getNotConfirmedVisits() {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    try {
      String sql = "SELECT r.id_request, r.time_slot, r.condition, "
          + "r.explanatory_note, r.scheduled_date_time, r.warehouse_address, "
          + "r.client, COUNT(f.id_furniture) "
          + "FROM pae.requests_for_visits r, pae.furnitures f WHERE r.condition = ? AND"
          + " r.id_request = f.request_visit GROUP BY r.id_request;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, VisitCondition.EN_ATTENTE.toString());
      ResultSet rs = ps.executeQuery();
      VisitDTO visit = null;
      while (rs.next()) {
        VisitDTO visitDTO = setVisit(rs, visit);
        visitDTO.setAmountOfFurnitures(rs.getInt(8));
        list.add(visitDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public List<VisitDTO> getVisitsToBeProcessed() {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    try {
      String sql = "SELECT DISTINCT r.id_request, r.time_slot, r.condition, "
          + "r.explanatory_note, r.scheduled_date_time, r.warehouse_address, "
          + "r.client, COUNT(f.id_furniture) FROM pae.requests_for_visits r, pae.furnitures f "
          + "WHERE r.id_request = f.request_visit AND r.condition=?" + "AND f.condition = ? "
          + "GROUP BY r.id_request;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, VisitCondition.ACCEPTEE.toString());
      ps.setString(2, Condition.EN_ATTENTE.toString());
      ResultSet rs = ps.executeQuery();
      VisitDTO visit = null;
      while (rs.next()) {
        VisitDTO visitDTO = setVisit(rs, visit);
        visitDTO.setAmountOfFurnitures(rs.getInt(8));
        list.add(visitDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public List<VisitDTO> getVisitsListForAClient(int idClient) {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    try {
      String sql = "SELECT DISTINCT r.id_request, r.time_slot, r.condition, "
          + "r.explanatory_note, r.scheduled_date_time, r.warehouse_address, "
          + "r.client, COUNT(f.id_furniture) FROM pae.requests_for_visits r, pae.furnitures f "
          + "WHERE r.id_request = f.request_visit AND r.client=? GROUP BY r.id_request;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setInt(1, idClient);
      ResultSet rs = ps.executeQuery();
      VisitDTO visit = null;
      while (rs.next()) {
        VisitDTO visitDTO = setVisit(rs, visit);
        visitDTO.setAmountOfFurnitures(rs.getInt(8));
        list.add(visitDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public int submitRequestOfVisit(VisitDTO visit) {
    int key = 0;
    try {
      String sql = "INSERT INTO pae.requests_for_visits VALUES (default, ?, ?, null, null, ?, ?);";
      ps = dalBackendServices.getPreparedStatementWithGeneratedReturn(sql);
      ps.setString(1, visit.getTimeSlot());
      ps.setString(2, VisitCondition.EN_ATTENTE.toString());
      ps.setInt(3, visit.getWarehouseAddressId());
      ps.setInt(4, visit.getIdClient());
      ps.execute();
      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        key = rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return key;
  }

  @Override
  public boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime) {
    try {
      String sql = "UPDATE pae.requests_for_visits SET condition = ?, scheduled_date_time = ? "
          + "WHERE id_request = ?;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, VisitCondition.ACCEPTEE.toString());
      Timestamp timeStampScheduledDateTime = Timestamp.valueOf(scheduledDateTime);
      ps.setTimestamp(2, timeStampScheduledDateTime);
      ps.setInt(3, idVisit);
      return ps.executeUpdate() == 1 ? true : false;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean cancelVisit(int idVisit, String explanatoryNote) {
    try {
      String sql = "UPDATE pae.requests_for_visits SET condition = ?, explanatory_note = ? "
          + "WHERE id_request = ?;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, VisitCondition.ANNULEE.toString());
      ps.setString(2, explanatoryNote);
      ps.setInt(3, idVisit);
      return ps.executeUpdate() == 1 ? true : false;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public VisitDTO getVisitById(int idVisit) {
    VisitDTO visit = null;
    try {
      ps = dalBackendServices.getPreparedStatement(
          "SELECT id_request, time_slot, condition, explanatory_note, scheduled_date_time,"
              + " warehouse_address, client "
              + "FROM pae.requests_for_visits WHERE id_request= ?;");
      ps.setInt(1, idVisit);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        visit = setVisit(rs, visit);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return visit;
  }

  @Override
  public List<FurnitureDTO> getListFurnituresForOnVisit(int idVisit) {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    try {
      String sql = "SELECT id_furniture, description, id_type, request_visit, condition "
          + "FROM pae.furnitures WHERE request_visit = ?";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setInt(1, idVisit);
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

  private FurnitureDTO setFurniture(ResultSet rs, FurnitureDTO furniture) {
    try {
      furniture = furnitureFactory.getFurnitureDTO();
      furniture.setId(rs.getInt(1));
      furniture.setDescription(rs.getString(2));
      furniture.setTypeId(rs.getInt(3));
      furniture.setRequestForVisitId(rs.getInt(4));
      furniture.setCondition(rs.getString(5));
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return furniture;
  }
  // private int addPhoto(PhotoDTO photo) {
  // int key = 0;
  // try {
  // String sql = "INSERT INTO pae.photos VALUES (default, ?, ?, ?, ?);";
  // ps = dalBackendServices.getPreparedStatement(sql);
  // ps.setString(1, photo.getPhoto());
  // ps.setBoolean(2, photo.isVisible());
  // ps.setBoolean(3, photo.isAClientPhoto());
  // ps.setInt(4, photo.getIdFurniture());
  // ps.execute();
  // ResultSet rs = ps.getGeneratedKeys();
  // if (rs.next()) {
  // key = rs.getInt(1);
  // }
  // } catch (SQLException e) {
  // throw new FatalException(e);
  // }
  // return key;
  // }



}
