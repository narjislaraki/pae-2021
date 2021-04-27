package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import be.vinci.pae.domain.edition.EditionDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserDTO.Role;
import be.vinci.pae.domain.visit.PhotoDTO;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.services.dal.DalServices;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.SaleDAO;
import be.vinci.pae.services.dao.UserDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDao;

  @Inject
  private UserDAO userDAO;

  @Inject
  private DalServices dalServices;

  @Inject
  private Logger logger;

  @Inject
  private SaleDAO saleDao;

  public FurnitureUCCImpl() {
    scheduledTasksInit();
  }

  /**
   * Tasks to manage Options and Reservations overtime.
   */
  private void scheduledTasks() {
    dalServices.getBizzTransaction(true);
    furnitureDao.cancelOvertimedOptions();
    logger.info("Scheduled management of overtimed Options just happend");
    dalServices.stopBizzTransaction();
  }

  /**
   * Initiation of the Options and Reservations management. The scheduledTask will be launched 1 sec
   * after the loading and then once a day at 00:05.
   */

  private void scheduledTasksInit() {
    TimerTask task = new TimerTask() {
      public void run() {
        scheduledTasks();
      }
    };
    TimerTask repeatedTask = new TimerTask() {
      public void run() {
        scheduledTasks();
      }
    };

    Timer timer1 = new Timer("Timer1");
    Timer timer2 = new Timer("Timer2");

    // time calculation
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

    String dateTime = tomorrow.getYear() + "-" + String.format("%02d", tomorrow.getMonthValue())
        + "-" + String.format("%02d", tomorrow.getDayOfMonth()) + " 00:05:00";
    LocalDateTime tomorrowRighHour = LocalDateTime.parse(dateTime, formatter);
    long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), tomorrowRighHour);

    long delay = 1000L * 60L * minutes; // tomorrow 00:05
    long period = 1000L * 60L * 60L * 24L; // 1 day
    // delay = 1 second, launched once
    timer1.schedule(task, 1000L);
    // delay = until tomorrow 00:05, launched every 24 hours
    timer2.scheduleAtFixedRate(repeatedTask, delay, period);

  }

  @Override
  public OptionDTO getOption(int id) {
    dalServices.getBizzTransaction(true);
    OptionDTO option = furnitureDao.getOption(id);
    dalServices.stopBizzTransaction();
    return option;
  }

  @Override
  public int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser) {
    dalServices.getBizzTransaction(true);
    int nbOfDay = furnitureDao.getSumOfOptionDaysForAUserAboutAFurniture(idFurniture, idUser);
    dalServices.stopBizzTransaction();
    return nbOfDay;
  }

  @Override
  public void indicateSentToWorkshop(int id) {
    dalServices.getBizzTransaction(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.ACHETE)) {
      furnitureDao.indicateSentToWorkshop(id);
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("State error");
    }
  }

  @Override
  public void indicateDropOfStore(int id) {
    dalServices.getBizzTransaction(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.EN_RESTAURATION)
        || furniture.getCondition().equals(Condition.ACHETE)) {
      furnitureDao.indicateDropInStore(id);
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("State error");
    }
  }

  @Override
  public void indicateOfferedForSale(int id, double price) {
    dalServices.getBizzTransaction(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    if (price > 0 && furniture.getCondition().equals(Condition.DEPOSE_EN_MAGASIN)) {
      furnitureDao.indicateOfferedForSale(furniture, price);
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("State error");
    }
  }

  @Override
  public void withdrawSale(int id) {
    dalServices.getBizzTransaction(false);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    if (furniture.getCondition().equals(Condition.EN_VENTE)
        || furniture.getCondition().equals(Condition.DEPOSE_EN_MAGASIN)) {
      furnitureDao.withdrawSale(id);
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("State error");
    }
  }

  @Override
  public void introduceOption(int optionTerm, int idUser, int idFurniture) {
    if (optionTerm <= 0) {
      throw new UnauthorizedException("optionTerm negative");
    }
    dalServices.getBizzTransaction(false);
    int nbrDaysActually =
        furnitureDao.getSumOfOptionDaysForAUserAboutAFurniture(idFurniture, idUser);
    if (nbrDaysActually == 5) {
      throw new UnauthorizedException("You have already reached the maximum number of days");
    } else if (nbrDaysActually + optionTerm > 5) {
      int daysLeft = 5 - nbrDaysActually;
      throw new UnauthorizedException("You can't book more than : " + daysLeft + " days");
    } else {
      furnitureDao.introduceOption(optionTerm, idUser, idFurniture);
      furnitureDao.indicateFurnitureUnderOption(idFurniture);
      dalServices.commitBizzTransaction();
    }
  }

  @Override
  public void cancelOption(String cancellationReason, int idOption, UserDTO user) {
    if (idOption < 1) {
      throw new BusinessException("Invalid id");
    }
    dalServices.getBizzTransaction(false);
    OptionDTO opt = furnitureDao.getOption(idOption);
    if (user.getId() == opt.getIdUser() || user.getRole() == Role.ADMIN) {
      int idFurniture = furnitureDao.cancelOption(cancellationReason, opt.getId());
      FurnitureDTO furniture = furnitureDao.getFurnitureById(idFurniture);
      furnitureDao.indicateOfferedForSale(furniture, furniture.getOfferedSellingPrice());
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("You have no right to delete this option");
    }
  }

  @Override
  public List<FurnitureDTO> getFurnitureList(UserDTO user) {
    dalServices.getBizzTransaction(true);
    List<FurnitureDTO> list = null;
    if (user != null && user.getRole() == Role.ADMIN) {
      list = furnitureDao.getFurnitureList();
    } else {
      list = furnitureDao.getPublicFurnitureList();
    }
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public FurnitureDTO getFurnitureById(int id) {
    dalServices.getBizzTransaction(true);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    if (furniture == null) {
      throw new BusinessException("There are no furniture for the given id");
    }
    int seller = furniture.getSellerId();
    if (seller != 0) {
      furniture.setSeller(userDAO.getUserFromId(seller));
    }
    int idPhoto = furniture.getFavouritePhotoId();
    if (idPhoto != 0) {
      furniture.setFavouritePhoto(furnitureDao.getFavouritePhotoById(idPhoto));
    }
    furniture.setType(furnitureDao.getFurnitureTypeById(furniture.getTypeId()));
    dalServices.stopBizzTransaction();
    return furniture;
  }

  @Override
  public void cancelOvertimedOptions() {
    dalServices.getBizzTransaction(true);
    furnitureDao.cancelOvertimedOptions();
    dalServices.stopBizzTransaction();
  }

  @Override
  public List<TypeOfFurnitureDTO> getTypesOfFurnitureList() {
    dalServices.getBizzTransaction(true);
    List<TypeOfFurnitureDTO> list = null;
    list = furnitureDao.getTypesOfFurnitureList();
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

  @Override
  public List<PhotoDTO> getFurniturePhotos(int idFurniture) {
    dalServices.getBizzTransaction(true);
    List<PhotoDTO> list = furnitureDao.getFurniturePhotos(idFurniture);
    dalServices.stopBizzTransaction();
    return list;
  }

  @Override
  public boolean edit(EditionDTO edition) {
    dalServices.getBizzTransaction(false);
    if (!(edition.getDescription().isEmpty() && edition.getIdType() == -1
        && edition.getOfferedSellingPrice() == -1 && edition.getFavouritePhotoId() == -1)) {

      FurnitureDTO furniture = furnitureDao.getFurnitureById(edition.getIdFurniture());
      System.out.println(furniture);
      String description = edition.getDescription().isEmpty() ? furniture.getDescription()
          : edition.getDescription();

      int idType = edition.getIdType() == -1 ? furniture.getTypeId() : edition.getIdType();

      double offeredSellingPrice =
          edition.getOfferedSellingPrice() == -1 ? furniture.getOfferedSellingPrice()
              : edition.getOfferedSellingPrice();

      int favouritePhoto = edition.getFavouritePhotoId() == -1 ? furniture.getFavouritePhotoId()
          : edition.getFavouritePhotoId();
      furnitureDao.edit(furniture.getId(), description, idType, offeredSellingPrice,
          favouritePhoto);
    }

    edition.getPhotosToAdd().forEach(e -> furnitureDao.addAdminPhoto(e, edition.getIdFurniture()));

    edition.getPhotosToDelete().forEach(e -> furnitureDao.deletePhoto(e));

    edition.getPhotosToDisplay().forEach(e -> furnitureDao.displayPhoto(e));

    edition.getPhotosToHide().forEach(e -> furnitureDao.hidePhoto(e));

    dalServices.commitBizzTransaction();
    return true;
  }

  @Override
  public FurnitureDTO getFurnitureWithPhotosById(int id) {
    dalServices.getBizzTransaction(true);
    FurnitureDTO furniture = furnitureDao.getFurnitureById(id);
    furniture.setListPhotos(furnitureDao.getFurniturePhotos(id));
    if (furniture == null) {
      throw new BusinessException("There are no furniture for the given id");
    }
    int seller = furniture.getSellerId();
    if (seller != 0) {
      furniture.setSeller(userDAO.getUserFromId(seller));
    }
    int idPhoto = furniture.getFavouritePhotoId();
    if (idPhoto != 0) {
      furniture.setFavouritePhoto(furnitureDao.getFavouritePhotoById(idPhoto));
    }
    furniture.setType(furnitureDao.getFurnitureTypeById(furniture.getTypeId()));
    dalServices.stopBizzTransaction();
    return furniture;
  }

}
