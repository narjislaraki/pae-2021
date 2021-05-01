package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.VisitDAO;
import jakarta.inject.Inject;

public class VisitUCCImpl implements VisitUCC {

  @Inject
  private DalServices dalServices;

  @Inject
  private VisitDAO visitDAO;

  @Inject
  private AddressDAO addressDAO;

  @Inject
  private FurnitureDAO furnitureDAO;

  @Inject
  private UserDAO userDAO;

  @Override
  public List<VisitDTO> getNotConfirmedVisits() {
    dalServices.getBizzTransaction(true);
    List<VisitDTO> list = visitDAO.getNotConfirmedVisits();
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public List<VisitDTO> getVisitsToBeProcessed() {
    dalServices.getBizzTransaction(true);
    List<VisitDTO> list = visitDAO.getVisitsToBeProcessed();
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public boolean submitRequestOfVisit(VisitDTO visit) {
    dalServices.getBizzTransaction(false);
    // TODO vérifier autrement que l'adresse est différente de celle du client
    visit.setClient(userDAO.getUserFromId(visit.getIdClient()));
    if (visit.getWarehouseAddress() == null || visit.getWarehouseAddress().getStreet() == null
        || visit.getWarehouseAddress().getStreet().isEmpty()) {
      visit.setWarehouseAddress(visit.getClient().getAddress());
      visit.setWarehouseAddressId(visit.getClient().getAddress().getId());
    } else {
      visit.getWarehouseAddress().setId(addressDAO.addAddress(visit.getWarehouseAddress()));
      visit.setWarehouseAddressId(visit.getWarehouseAddress().getId());
    }
    visit.setVisitCondition("en attente");
    int idRequestForVisit = visitDAO.submitRequestOfVisit(visit);
    for (FurnitureDTO furniture : visit.getFurnitureList()) {
      int idFurniture =
          furnitureDAO.addFurniture(furniture, idRequestForVisit, visit.getIdClient());
      for (PhotoDTO photo : furniture.getListPhotos()) {
        furnitureDAO.addClientPhoto(photo, idFurniture);
      }
    }
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

  @Override
  public VisitDTO getVisitById(int id) {
    dalServices.getBizzTransaction(true);
    VisitDTO visit = visitDAO.getVisitById(id);
    if (visit == null) {
      throw new BusinessException("There are no request for visit for the given id");
    }
    int idAddress = visit.getWarehouseAddressId();
    if (idAddress != 0) {
      visit.setWarehouseAddress(addressDAO.getAddress(idAddress));
    }
    int idClient = visit.getIdClient();
    if (idClient != 0) {
      visit.setClient((User) userDAO.getUserFromId(idClient));
    }
    dalServices.stopBizzTransaction();
    return visit;
  }

  @Override
  public List<FurnitureDTO> getListFurnituresForOneVisit(int idVisit) {
    dalServices.getBizzTransaction(true);
    List<FurnitureDTO> list = visitDAO.getListFurnituresForOnVisit(idVisit);
    for (FurnitureDTO furniture : list) {
      furniture.setListPhotos(furnitureDAO.getFurniturePhotos(furniture.getId()));
    }
    dalServices.stopBizzTransaction();
    return list;
  }

}
