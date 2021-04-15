
package be.vinci.pae;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitDTO.VisitCondition;
import be.vinci.pae.domain.visit.VisitFactory;
import be.vinci.pae.domain.visit.VisitFactoryImpl;

public class ObjectDistributor {

  private static UserFactory userFactory = new UserFactoryImpl();
  private static AddressFactory addressFactory = new AddressFactoryImpl();
  private static VisitFactory visitFactory = new VisitFactoryImpl();
  private static FurnitureFactory furnitureFactory = new FurnitureFactoryImpl();

  private static String goodPassword = "1234";
  private static String goodEmail = "test@test.com";
  private static String badEmail = "test.test@test.com";
  private static String badPassword = "5678";
  private static String goodEmailNotValidated = "test3@test.com";

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
   * @return
   */
  private static LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(str, formatter);
  }

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


  public static FurnitureDTO getFurnitureInSale() {
    FurnitureDTO furniture = furnitureFactory.getFurnitureDTO();
    furniture.setCondition(Condition.EN_VENTE.toString());
    furniture.setDepositDate(LocalDateTime.now());
    furniture.setDescription("meuble");
    furniture.setId(1);
    furniture.setPurchasePrice(200);
    // TODO to complete with the tests
    return furniture;
  }

}
