package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.address.Address;
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
  private AddressDAO addressDAO;

  @Inject
  private UserDAO userDAO;

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
      String sql =
          "SELECT id_request, time_slot, condition, explanatory_note, scheduled_date_time, warehouse_address, client FROM pae.requests_for_visits WHERE condition = ?;";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, VisitCondition.EN_ATTENTE.toString());
      ResultSet rs = ps.executeQuery();
      VisitDTO visit = null;
      while (rs.next()) {
        VisitDTO visitDTO = setVisit(rs, visit);
        list.add(visitDTO);
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
    return list;
  }

  @Override
  public void submitRequestOfVisit(VisitDTO visit, int idClient, int idWarehouseAddress) {
    try {
      String sql = "INSERT INTO pae.requests_for_visits VALUES (default, ?, ?, null, null, ?, ?);";
      ps = dalBackendServices.getPreparedStatement(sql);
      ps.setString(1, visit.getTimeSlot());
      ps.setString(2, VisitCondition.EN_ATTENTE.toString());
      ps.setInt(3, idWarehouseAddress);
      ps.setInt(4, idClient);
      ps.execute();
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime) {
    try {
      String sql =
          "UPDATE pae.requests_for_visits SET condition = ?, scheduled_date_time = ? WHERE id_request = ?;";
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
      String sql =
          "UPDATE pae.requests_for_visits SET condition = ?, explanatoryNote = ? WHERE id_request = ?;";
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



}
