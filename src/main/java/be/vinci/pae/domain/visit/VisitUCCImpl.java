package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.VisitDAO;
import jakarta.inject.Inject;

public class VisitUCCImpl implements VisitUCC {

  @Inject
  private DalServices dalServices;

  @Inject
  private VisitDAO visitDAO;

  @Inject
  private AddressDAO addressDAO;

  @Override
  public List<VisitDTO> getNotConfirmedVisits() {
    dalServices.getBizzTransaction(true);
    List<VisitDTO> list = visitDAO.getNotConfirmedVisits();
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public boolean submitRequestOfVisit(VisitDTO visit, int idClient, int idWarehouseAddress) {
    dalServices.getBizzTransaction(false);
    visit.getWarehouseAddress().setId(addressDAO.addAddress(visit.getWarehouseAddress()));
    visitDAO.submitRequestOfVisit(visit, idClient, idWarehouseAddress);
    dalServices.commitBizzTransaction();
    return true;
  }

  @Override
  public boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime) {
    if (scheduledDateTime == null) {
      throw new BusinessException("Scheduled date time is needed");
    }
    dalServices.getBizzTransaction(true);
    boolean result = visitDAO.acceptVisit(idVisit, scheduledDateTime);
    dalServices.stopBizzTransaction();
    if (!result) {
      throw new BusinessException("The id is invalid");
    }
    return result;
  }

  @Override
  public boolean cancelVisit(int idVisit, String explanatoryNote) {
    if (explanatoryNote == null || explanatoryNote.equals("")) {
      throw new BusinessException("Explanatory note is needed");
    }
    dalServices.getBizzTransaction(true);
    boolean result = visitDAO.cancelVisit(idVisit, explanatoryNote);
    dalServices.stopBizzTransaction();
    if (!result) {
      throw new BusinessException("The id is invalid");
    }
    return result;
  }



}
