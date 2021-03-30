package be.vinci.pae.domain.furniture;

import java.util.List;
import java.util.Map;
import be.vinci.pae.api.exceptions.BusinessException;
import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDao;

  @Inject
  private UserDAO userDAO;

  @Inject
  private DalServices dalServices;


  @Override
  public OptionDTO getOption(int id) {
    dalServices.getConnection(false);
    OptionDTO option = furnitureDao.getOption(id);
    dalServices.commitTransaction();
    return option;
  }

  @Override
  public int getNbOfDay(int idFurniture, int idUser) {
    dalServices.getConnection(false);
    int nbOfDay = furnitureDao.getNumberOfOptions(idFurniture, idUser);
    dalServices.commitTransaction();
    return nbOfDay;
  }



  @Override
  public void indicateSentToWorkshop(int id) {
    dalServices.getConnection(false);
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.ACHETE)) {
      furnitureDao.indicateSentToWorkshop(id);
      dalServices.commitTransaction();
    } else {
      dalServices.rollbackTransaction();
      throw new BusinessException("State error");
    }
  }

  @Override
  public void indicateDropOfStore(int id) {
    dalServices.getConnection(false);
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.EN_RESTAURATION)
        || furniture.getCondition().equals(Condition.ACHETE)) {
      furnitureDao.indicateDropInStore(id);
      dalServices.commitTransaction();
    } else {
      dalServices.rollbackTransaction();
      throw new BusinessException("State error");
    }
  }

  @Override
  public void indicateOfferedForSale(int id, double price) {
    dalServices.getConnection(false);
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
    if (price > 0 && furniture.getCondition().equals(Condition.DEPOSE_EN_MAGASIN)) {
      furnitureDao.indicateOfferedForSale(furniture, price);
      dalServices.commitTransaction();
    } else {
      dalServices.rollbackTransaction();
      throw new BusinessException("State error");
    }
  }

  @Override
  public void withdrawSale(int id) {
    dalServices.getConnection(false);
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.EN_VENTE)
        || furniture.getCondition().equals(Condition.DEPOSE_EN_MAGASIN)) {
      furnitureDao.withdrawSale(id);
      dalServices.commitTransaction();
    } else {
      dalServices.rollbackTransaction();
      throw new BusinessException("State error");
    }
  }

  @Override
  public void introduceOption(int optionTerm, int idUser, int idFurniture) {
    if (optionTerm <= 0) {
      throw new UnauthorizedException("optionTerm negative");
    }
    dalServices.getConnection(false);
    int nbrDaysActually = furnitureDao.getNumberOfOptions(idFurniture, idUser);
    if (nbrDaysActually == 5) {
      dalServices.rollbackTransaction();
      throw new UnauthorizedException("You have already reached the maximum number of days");
    } else if (nbrDaysActually + optionTerm > 5) {
      dalServices.rollbackTransaction();
      int daysLeft = 5 - nbrDaysActually;
      throw new UnauthorizedException("You can't book more than : " + daysLeft + " days");
    } else {
      furnitureDao.introduceOption(optionTerm, idUser, idFurniture);
      furnitureDao.indicateUnderOption(idFurniture);
      dalServices.commitTransaction();
    }
  }

  @Override
  public void cancelOption(String cancellationReason, int idOption) {
    if (idOption < 1) {
      throw new BusinessException("Invalid id");
    }
    dalServices.getConnection(false);
    int idFurniture = furnitureDao.cancelOption(cancellationReason, idOption);
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(idFurniture);
    furnitureDao.indicateOfferedForSale(furniture, furniture.getOfferedSellingPrice());
    dalServices.commitTransaction();
  }

  @Override
  public List<FurnitureDTO> getFurnitureList() {
    dalServices.getConnection(false);
    List<FurnitureDTO> furnitureList = furnitureDao.getFurnitureList();
    dalServices.commitTransaction();
    System.out.println(furnitureList);
    return furnitureList;
  }


  @Override
  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {
    // TODO Auto-generated method stub
  }

  @Override
  public FurnitureDTO getFurnitureById(int id) {
    dalServices.getConnection(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    // dalServices.commitTransactionAndContinue();
    furniture.setSeller(userDAO.getUserFromId(furniture.getSellerId()));
    // TODO tests si présent?
    furniture.setType(furnitureDao.getTypeById(furniture.getTypeId()));
    furniture
        .setFavouritePhoto(furnitureDao.getFavouritePhotoById(furniture.getFavouritePhotoId()));
    // dalServices.commitTransaction();
    dalServices.commitTransaction();
    return furniture;
  }



}
