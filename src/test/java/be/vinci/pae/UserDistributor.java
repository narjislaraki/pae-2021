
package be.vinci.pae;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.address.AddressImpl;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserImpl;

public class UserDistributor {

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
    User goodUser = new UserImpl();
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
    User goodUserNotValidated = new UserImpl();
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
    Address address = new AddressImpl();
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
  public static LocalDateTime getLocalDateTime(String str) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(str, formatter);
  }
}
