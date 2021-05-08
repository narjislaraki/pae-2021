
package be.vinci.pae;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import be.vinci.pae.domain.interfaces.AddressDTO;
import be.vinci.pae.domain.interfaces.EditionDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO.Condition;
import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.domain.interfaces.PhotoDTO;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.TypeOfFurnitureDTO;
import be.vinci.pae.domain.interfaces.User;
import be.vinci.pae.domain.interfaces.VisitDTO;
import be.vinci.pae.domain.interfaces.VisitDTO.VisitCondition;
import be.vinci.pae.factories.AddressFactoryImpl;
import be.vinci.pae.factories.EditionFactoryImpl;
import be.vinci.pae.factories.FurnitureFactoryImpl;
import be.vinci.pae.factories.OptionFactoryImpl;
import be.vinci.pae.factories.PhotoFactoryImpl;
import be.vinci.pae.factories.SaleFactoryImpl;
import be.vinci.pae.factories.TypeOfFurnitureFactoryImpl;
import be.vinci.pae.factories.UserFactoryImpl;
import be.vinci.pae.factories.VisitFactoryImpl;
import be.vinci.pae.factories.interfaces.AddressFactory;
import be.vinci.pae.factories.interfaces.EditionFactory;
import be.vinci.pae.factories.interfaces.FurnitureFactory;
import be.vinci.pae.factories.interfaces.OptionFactory;
import be.vinci.pae.factories.interfaces.PhotoFactory;
import be.vinci.pae.factories.interfaces.SaleFactory;
import be.vinci.pae.factories.interfaces.TypeOfFurnitureFactory;
import be.vinci.pae.factories.interfaces.UserFactory;
import be.vinci.pae.factories.interfaces.VisitFactory;

public class ObjectDistributor {

  private static UserFactory userFactory = new UserFactoryImpl();
  private static AddressFactory addressFactory = new AddressFactoryImpl();
  private static VisitFactory visitFactory = new VisitFactoryImpl();
  private static FurnitureFactory furnitureFactory = new FurnitureFactoryImpl();
  private static SaleFactory saleFactory = new SaleFactoryImpl();
  private static OptionFactory optionFactory = new OptionFactoryImpl();
  private static PhotoFactory photoFactory = new PhotoFactoryImpl();
  private static TypeOfFurnitureFactory typeOfFurnitureFactory = new TypeOfFurnitureFactoryImpl();
  private static EditionFactory editionFactory = new EditionFactoryImpl();


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

  private static AddressDTO getAddress() {
    AddressDTO address = addressFactory.getAddress();
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
   * Construct a visit with no furniture.
   * 
   * @return the visit
   */
  public static VisitDTO getVisitWithNoFurniture() {
    VisitDTO visit = visitFactory.getVisitDTO();
    visit.setIdClient(1);
    return visit;
  }

  /**
   * Construct a furniture.
   * 
   * @return the furniture
   */
  public static FurnitureDTO getFurnitureForVisitUCCTest() {
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    return furniture;
  }

  /**
   * Construct a photo.
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
    return furniture;
  }

  /**
   * Construct a furniture concidered as "a good furniture model", and return it.
   * 
   * @return the good furniture
   */
  public static FurnitureDTO getFurnitureForFurnitureUCCTest() {
    FurnitureDTO goodFurniture = furnitureFactory.getFurnitureDTO();
    goodFurniture.setId(1);
    goodFurniture.setTypeId(1);
    goodFurniture.setType("Armoire");
    goodFurniture.setRequestForVisitId(1);
    goodFurniture.setSellerId(1);
    goodFurniture.setSeller(getGoodValidatedUser());
    goodFurniture.setCondition("acheté");
    goodFurniture.setDescription("meuble");
    goodFurniture.setPurchasePrice(10);
    goodFurniture.setPickUpDate(LocalDateTime.now());
    goodFurniture.setStoreDeposit(false);
    goodFurniture.setFavouritePhotoId(1);
    return goodFurniture;
  }

  public static void setFurnitureCondition(FurnitureDTO furniture, String condition) {
    furniture.setCondition(condition);
  }

  /**
   * Construct a photo.
   * 
   * @return the photo
   */
  public static PhotoDTO createPhoto() {
    PhotoDTO photo = photoFactory.getPhotoDTO();
    photo.setId(1);
    photo.setIdFurniture(1);
    photo.setPhoto("goodPhoto");
    return photo;
  }


  /**
   * Construct a sold furniture.
   * 
   * @return the sold furniture
   */
  public static FurnitureDTO getSoldFurniture() {
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    furniture.setId(1);
    furniture.setCondition(Condition.VENDU.toString());
    furniture.setOfferedSellingPrice(10);
    // furniture.set
    return furniture;
  }


  /**
   * Construct a sale with a sold furniture.
   * 
   * @return the bad sale
   */
  public static SaleDTO getSale() {
    SaleDTO sale = saleFactory.getSaleDTO();
    sale.setId(1);
    sale.setIdFurniture(getSoldFurniture().getId());
    sale.setIdBuyer(1);
    sale.setDateOfSale(LocalDateTime.now());
    sale.setSellingPrice(11);
    return sale;
  }


  /**
   * Construct a good option.
   * 
   * @return the good option
   */
  public static OptionDTO getGoodOption() {
    OptionDTO option = optionFactory.getOptionDTO();
    option.setId(1);
    option.setIdFurniture(1);
    option.setIdUser(1);
    option.setDate(LocalDateTime.now());
    option.setOptionTerm(4);
    return option;
  }

  /**
   * Construct a good type of furniture.
   * 
   * @return the good type of furniture
   */
  public static TypeOfFurnitureDTO getGoodTypeOfFurniture() {
    TypeOfFurnitureDTO type = typeOfFurnitureFactory.getTypeOfFurnitureDTO();
    type.setId(1);
    type.setLabel("Chaise");
    return type;
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
    goodVisit.getFurnitureList().add(getFurnitureForVisitUCCTest());
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
    goodVisit.getFurnitureList().add(getFurnitureForVisitUCCTest());
    goodVisit.setAmountOfFurnitures(0);
    List<PhotoDTO> list = new ArrayList<PhotoDTO>();
    goodVisit.getFurnitureList().get(0).setListPhotos(list);
    goodVisit.getFurnitureList().get(0).getListPhotos().add(getPhoto());
    return goodVisit;
  }

  public static EditionDTO getEmptyEdition() {
    return editionFactory.getEditionDTO();
  }


}
