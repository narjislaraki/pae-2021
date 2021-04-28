package be.vinci.pae.services.dao;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.visit.VisitDTO;

public interface VisitDAO {

  List<VisitDTO> getNotConfirmedVisits();

  int submitRequestOfVisit(VisitDTO visit);

  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  boolean cancelVisit(int idVisit, String explanatoryNote);

  VisitDTO getVisitById(int idVisit);

  List<FurnitureDTO> getListFurnituresForOnVisit(int idVisit);


}
