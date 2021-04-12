package be.vinci.pae.domain.visit;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitUCC {

  List<VisitDTO> getNotConfirmedVisits();

  boolean submitRequestOfVisit(VisitDTO visit, int idClient, int idWarehouseAddress);

  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  boolean cancelVisit(int idVisit, String explanatoryNote);

  VisitDTO getVisitById(int id);


}
