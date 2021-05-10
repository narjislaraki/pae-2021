package be.vinci.pae.ucc;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO.Condition;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.domain.interfaces.UserDTO.Role;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.services.dao.interfaces.UserDAO;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import jakarta.inject.Inject;

public class SaleUCCImpl implements SaleUCC {
  @Inject
  private SaleDAO saleDao;

  @Inject
  private FurnitureDAO furnitureDao;

  @Inject
  private UserDAO userDao;

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
    if (sale == null) {
      throw new BusinessException("The sale is invalid");
    }
    dalServices.getBizzTransaction(false);
    UserDTO user = userDao.getUserFromId(sale.getIdBuyer());
    FurnitureDTO furniture = furnitureDao.getFurnitureById(sale.getIdFurniture());
    if (furniture == null) {
      throw new BusinessException("The given furniture's id is invalid");
    }
    if (user != null && !furniture.getCondition().equals(Condition.EN_VENTE)
        && !user.getRole().equals(Role.ANTIQUAIRE)
        || furniture.getCondition().equals(Condition.EN_ATTENTE)
        || furniture.getCondition().equals(Condition.SOUS_OPTION)
        || furniture.getCondition().equals(Condition.VENDU)
        || furniture.getCondition().equals(Condition.REFUSE)
        || furniture.getCondition().equals(Condition.RETIRE)) {
      throw new BusinessException("The furniture is not in a state to be sold");
    }
    furnitureDao.setFurnitureCondition(furniture, Condition.VENDU);
    sale.setDateOfSale(LocalDateTime.now());
    saleDao.addSale(sale);
    dalServices.commitBizzTransaction();
    return true;
  }
}
