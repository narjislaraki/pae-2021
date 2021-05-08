package be.vinci.pae.ucc;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.VisitDTO;

public interface VisitUCC {

  List<VisitDTO> getNotConfirmedVisits();

  List<VisitDTO> getVisitsToBeProcessed();

  List<VisitDTO> getVisitsListForAClient(int idClient);

  boolean submitRequestOfVisit(VisitDTO visit);

  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  boolean cancelVisit(int idVisit, String explanatoryNote);

  VisitDTO getVisitById(int id);

  List<FurnitureDTO> getListFurnituresForOneVisit(int idVisit);


}
