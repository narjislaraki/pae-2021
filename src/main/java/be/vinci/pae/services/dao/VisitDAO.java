package be.vinci.pae.services.dao;

import java.util.List;
import be.vinci.pae.domain.visit.VisitDTO;

public interface VisitDAO {

  List<VisitDTO> getNotConfirmedVisits();


}
