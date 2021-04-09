package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserDTO.Role;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
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

  @Inject
  private Logger logger;

  public FurnitureUCCImpl() {
    scheduledTasksInit();
  }

  /**
   * Tasks to manage Options and Reservations overtime.
   */
  private void scheduledTasks() {
    dalServices.getBizzTransaction(true);
    furnitureDao.cancelOvertimedOptions();
    furnitureDao.cancelOvertimedReservations();
    logger.info("Scheduled management of overtimed Options and Reservations just happend");
    dalServices.stopBizzTransaction();
  }

  /**
   * Initiation of the Options and Reservations management.
   * 
   * The scheduledTask will be launched 1 sec after the loading and then once a day at 00:05.
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
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
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
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
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
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
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
    Furniture furniture = (Furniture) furnitureDao.getFurnitureById(id);
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
      Furniture furniture = (Furniture) furnitureDao.getFurnitureById(idFurniture);
      furnitureDao.indicateOfferedForSale(furniture, furniture.getOfferedSellingPrice());
      dalServices.commitBizzTransaction();
    } else {
      throw new BusinessException("You have no right to delete this option");
    }
  }

  @Override
  public List<FurnitureDTO> getFurnitureList(UserDTO user) {
    dalServices.getBizzTransaction(true);
    List<FurnitureDTO> list;
    if (user != null && user.getRole() == Role.ADMIN) {
      list = furnitureDao.getFurnitureList();
    } else {
      list = furnitureDao.getPublicFurnitureList();
    }
    dalServices.stopBizzTransaction();
    return list;
  }


  @Override
  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {
    // TODO Auto-generated method stub
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



}
