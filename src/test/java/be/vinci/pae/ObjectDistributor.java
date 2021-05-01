
package be.vinci.pae;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.address.AddressFactoryImpl;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureFactory;
import be.vinci.pae.domain.furniture.FurnitureFactoryImpl;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserFactoryImpl;
import be.vinci.pae.domain.visit.PhotoDTO;
import be.vinci.pae.domain.visit.PhotoFactory;
import be.vinci.pae.domain.visit.PhotoFactoryImpl;
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitDTO.VisitCondition;
import be.vinci.pae.domain.visit.VisitFactory;
import be.vinci.pae.domain.visit.VisitFactoryImpl;

public class ObjectDistributor {

  private static UserFactory userFactory = new UserFactoryImpl();
  private static AddressFactory addressFactory = new AddressFactoryImpl();
  private static VisitFactory visitFactory = new VisitFactoryImpl();
  private static FurnitureFactory furnitureFactory = new FurnitureFactoryImpl();
  private static PhotoFactory photoFactory = new PhotoFactoryImpl();

  private static String goodPassword = "1234";
  private static String goodEmail = "test@test.com";
  private static String badEmail = "test.test@test.com";
  private static String badPassword = "5678";
  private static String goodEmailNotValidated = "test3@test.com";
  private static LocalDateTime goodScheduledDateTime = LocalDateTime.now();
  private static LocalDateTime badScheduledDateTime = null;
  private static String goodExplanatoryNote = "good explanatory";
  private static String badExplanatoryNote = null;
  private static String emptyExplanatoryNote = "";

  /**
   * Construct a user considered as "a good user model that is validated", and return it.
   * 
   * @return the good user
   */
  public static User getGoodValidatedUser() {
    User goodUser = (User) userFactory.getUserDTO();
    goodUser.setId(1);
    goodUser.setUsername("test");
    goodUser.setLastName("Heuzer");
    goodUser.setFirstName("Nina");
    goodUser.setEmail(goodEmail);
    goodUser.setRole("admin");
    goodUser.setAddress(getAddress());
    LocalDateTime dateTime = getLocalDateTime("2021-01-05 00:00");
    goodUser.setRegistrationDate(dateTime);
    goodUser.setValidated(true);
    goodUser.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234


    goodUser.setAddress(getAddress());

    return goodUser;
  }


  /**
   * Construct a user considered as "a good user model but not validated yet", and return it.
   * 
   * @return the not-validated good user
   */
  public static User getGoodNotValidatedUser() {
    User goodUserNotValidated = (User) userFactory.getUserDTO();
    goodUserNotValidated.setId(3);
    goodUserNotValidated.setUsername("test3");
    goodUserNotValidated.setLastName("de Theux");
    goodUserNotValidated.setFirstName("Boris");
    goodUserNotValidated.setEmail(goodEmailNotValidated);
    goodUserNotValidated.setRole("client");
    LocalDateTime dateTime = getLocalDateTime("2021-02-07 00:00");
    goodUserNotValidated.setRegistrationDate(dateTime);
    goodUserNotValidated.setValidated(false);
    goodUserNotValidated
        .setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234
    goodUserNotValidated.setAddress(getAddress());

    return goodUserNotValidated;
  }

  public static String getGoodPassword() {
    return goodPassword;
  }

  public static String getGoodEmail() {
    return goodEmail;
  }

  public static String getBadEmail() {
    return badEmail;
  }

  public static String getBadPassword() {
    return badPassword;
  }

  public static String getGoodEmailNotValidated() {
    return goodEmailNotValidated;
  }

  public static LocalDateTime getGoodScheduledDateTime() {
    return goodScheduledDateTime;
  }

  public static LocalDateTime getBadScheduledDateTime() {
    return badScheduledDateTime;
  }

  public static String getGoodExplanatoryNote() {
    return goodExplanatoryNote;
  }

  public static String getBadExplanatoryNote() {
    return badExplanatoryNote;
  }

  public static String getEmptyExplanatoryNote() {
    return emptyExplanatoryNote;
  }

  private static Address getAddress() {
    Address address = addressFactory.getAddress();
    address.setBuildingNumber("10");
    address.setCity("Bruxelles");
    address.setCountry("Belgique");
    address.setPostCode("1000");
    address.setStreet("Rue du four");
    address.setUnitNumber("6");
    return address;
  }

  /**
   * Returning a localDateTime based on a string.
   * 
   * @param str the dateTime format as "yyyy-MM-dd HH:mm"
   * @return the LocalDateTime
   */
  private static LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(str, formatter);
  }

  /**
   * Construct a Visit not yet confirmed.
   * 
   * @return the visit
   */
  public static VisitDTO getNotConfirmedVisitDTO() {
    VisitDTO visit = visitFactory.getVisitDTO();
    visit.setClient(getGoodNotValidatedUser());
    visit.setIdClient(getGoodValidatedUser().getId());
    visit.setIdRequest((int) (Math.random() * 100));
    visit.setTimeSlot("Du 10 au 25 avril de 17h à 21h");
    visit.setWarehouseAddress(getAddress());
    visit.setWarehouseAddressId(getAddress().getId());
    visit.setVisitCondition(VisitCondition.EN_ATTENTE.toString());

    return visit;
  }

  /**
   * Construct a visit who has furniture not yet treated.
   * 
   * @return the visit
   */
  public static VisitDTO getVisitsToBeProcessedDTO() {
    VisitDTO visit = visitFactory.getVisitDTO();
    visit.setIdRequest(1);
    visit.setClient(getGoodNotValidatedUser());
    visit.setIdClient(getGoodValidatedUser().getId());
    visit.setIdRequest((int) (Math.random() * 100));
    visit.setTimeSlot("Du 10 au 25 avril de 17h à 21h");
    visit.setWarehouseAddress(getAddress());
    visit.setWarehouseAddressId(getAddress().getId());
    visit.setVisitCondition(VisitCondition.ACCEPTEE.toString());
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    furniture.setId(1);
    furniture.setCondition(Condition.EN_ATTENTE.toString());
    furniture.setRequestForVisitId(1);
    ArrayList<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(furniture);
    visit.setFurnitureList(list);
    return visit;
  }


  /**
   * Construct a visit with no furniture
   * 
   * @return the visit
   */
  public static VisitDTO getVisitWithNoFurniture() {
    VisitDTO visit = visitFactory.getVisitDTO();
    visit.setIdClient(1);
    return visit;
  }

  /**
   * Construct a furniture
   * 
   * @return the furniture
   */
  public static FurnitureDTO getFurniture() {
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    return furniture;
  }

  /**
   * Construct a photo
   * 
   * @return the photo
   */
  public static PhotoDTO getPhoto() {
    PhotoDTO photo = photoFactory.getPhotoDTO();
    return photo;
  }

  /**
   * Construct a furniture in sale.
   * 
   * @return the furniture
   */
  public static FurnitureDTO getFurnitureInSale() {
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    furniture.setCondition(Condition.EN_VENTE.toString());
    furniture.setDepositDate(LocalDateTime.now());
    furniture.setDescription("meuble");
    furniture.setId(1);
    // TODO to complete with the tests
    return furniture;
  }


  /**
   * Construct a visit considered as"a good visit",and return it.**@return the good visit
   * 
   * @return the visit
   */
  public static VisitDTO getGoodVisit() {
    VisitDTO goodVisit = visitFactory.getVisitDTO();
    goodVisit.setIdRequest(1);
    goodVisit.setTimeSlot("maintenant");
    goodVisit.setWarehouseAddressId(1);
    goodVisit.setWarehouseAddress(getAddress());
    goodVisit.setVisitCondition(VisitCondition.ACCEPTEE.toString());
    goodVisit.setClient(getGoodValidatedUser());
    goodVisit.setIdClient(getGoodValidatedUser().getId());
    goodVisit.setExplanatoryNote(null);
    goodVisit.setScheduledDateTime(LocalDateTime.now());
    goodVisit.setFurnitureList(new ArrayList<FurnitureDTO>());
    goodVisit.getFurnitureList().add(getFurniture());
    goodVisit.setAmountOfFurnitures(0);
    List<PhotoDTO> list = new ArrayList<PhotoDTO>();
    goodVisit.getFurnitureList().get(0).setListPhotos(list);
    goodVisit.getFurnitureList().get(0).getListPhotos().add(getPhoto());
    return goodVisit;
  }

  /**
   * Construct a visit considered as"a good visit",and return it.**@return the good visit
   * 
   * @return the visit
   */
  public static VisitDTO getGoodVisitWithoutWarehouseAddress() {
    VisitDTO goodVisit = visitFactory.getVisitDTO();
    goodVisit.setIdRequest(1);
    goodVisit.setTimeSlot("maintenant");
    goodVisit.setVisitCondition(VisitCondition.ACCEPTEE.toString());
    goodVisit.setIdClient(getGoodValidatedUser().getId());
    goodVisit.setExplanatoryNote(null);
    goodVisit.setScheduledDateTime(LocalDateTime.now());
    goodVisit.setFurnitureList(new ArrayList<FurnitureDTO>());
    goodVisit.getFurnitureList().add(getFurniture());
    goodVisit.setAmountOfFurnitures(0);
    List<PhotoDTO> list = new ArrayList<PhotoDTO>();
    goodVisit.getFurnitureList().get(0).setListPhotos(list);
    goodVisit.getFurnitureList().get(0).getListPhotos().add(getPhoto());
    return goodVisit;
  }



}
