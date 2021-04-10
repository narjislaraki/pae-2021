package be.vinci.pae.services.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.visit.VisitDTO;
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

  public VisitDTO setVisit(ResultSet rs, VisitDTO visit) {
    try {
      visit = visitFactory.getVisitDTO();
      visit.setIdRequest(rs.getInt(1));
      visit.setTimeSlot(rs.getString(2));
      visit.setVisitCondition(rs.getString(3));
      visit.setExplanatoryNote(rs.getString(4));
      visit.setScheduledDateTime(rs.getTimestamp(5).toLocalDateTime());
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



}
