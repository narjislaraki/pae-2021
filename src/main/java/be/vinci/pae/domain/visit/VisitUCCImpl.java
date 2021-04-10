package be.vinci.pae.domain.visit;

import java.util.List;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.VisitDAO;
import jakarta.inject.Inject;

public class VisitUCCImpl implements VisitUCC {

  @Inject
  private DalServices dalServices;

  @Inject
  private VisitDAO visiteDAO;

  @Override
  public List<VisitDTO> getNotConfirmedVisits() {
    dalServices.getBizzTransaction(true);
    List<VisitDTO> list = visiteDAO.getNotConfirmedVisits();
    dalServices.stopBizzTransaction();
    return list;
  }

}
