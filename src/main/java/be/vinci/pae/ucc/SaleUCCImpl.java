package be.vinci.pae.ucc;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO.Condition;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import jakarta.inject.Inject;

public class SaleUCCImpl implements SaleUCC {
  @Inject
  private SaleDAO saleDao;

  @Inject
  private FurnitureDAO furnitureDao;

  @Inject
  private DalServices dalServices;


  @Override
  public List<SaleDTO> getSalesList() {
    dalServices.getBizzTransaction(true);
    List<SaleDTO> list = saleDao.getSalesList();
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public boolean addSale(SaleDTO sale) {
    dalServices.getBizzTransaction(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(sale.getIdFurniture());
    if (furniture.getCondition().equals(Condition.VENDU)) {
      return false;
    }
    furnitureDao.setFurnitureCondition(furniture, Condition.VENDU);
    sale.setDateOfSale(LocalDateTime.now());
    saleDao.addSale(sale);
    dalServices.commitBizzTransaction();
    return true;
  }
}
