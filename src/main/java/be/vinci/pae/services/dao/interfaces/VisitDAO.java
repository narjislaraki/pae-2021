package be.vinci.pae.services.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.VisitDTO;

public interface VisitDAO {

  List<VisitDTO> getNotConfirmedVisits();

  List<VisitDTO> getVisitsToBeProcessed();

  List<VisitDTO> getVisitsListForAClient(int idClient);

  int submitRequestOfVisit(VisitDTO visit);

  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  boolean cancelVisit(int idVisit, String explanatoryNote);

  VisitDTO getVisitById(int idVisit);

  List<FurnitureDTO> getListFurnituresForOnVisit(int idVisit);


}
