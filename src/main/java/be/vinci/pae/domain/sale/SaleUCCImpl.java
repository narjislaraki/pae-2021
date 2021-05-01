package be.vinci.pae.domain.sale;

import java.util.List;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.SaleDAO;
import jakarta.inject.Inject;

public class SaleUCCImpl implements SaleUCC {
  @Inject
  private SaleDAO saleDao;

  @Inject
  private DalServices dalServices;

  public List<SaleDTO> getSalesList() {
    dalServices.getBizzTransaction(true);
    List<SaleDTO> list = saleDao.getSalesList();
    dalServices.stopBizzTransaction();
    return list;
  }
}
