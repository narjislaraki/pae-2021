package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.tests.UserDistributor;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class UserUCCTest {

  private static UserUCC userUCC;
  private static User goodUser;
  private static User goodUserNotValidated;
  private static String goodEmail;
  private static String badEmail;
  private static String goodPassword;
  private static String badPassword;
  private static String goodEmailNotValidated;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    Config.load();
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    userUCC = locator.getService(UserUCC.class);

    badEmail = "test.test@test.com";
    badPassword = "5678";

    goodUser = UserDistributor.getGoodValidatedUser();
    goodPassword = "1234";
    goodEmail = goodUser.getEmail();
    goodUserNotValidated = UserDistributor.getGoodNotValidatedUser();
    goodEmailNotValidated = goodUserNotValidated.getEmail();

  }

  @DisplayName("Test connection with right email and password")
  @Test
  public void connection1Test() {
    User u = (User) userUCC.connection(goodEmail, goodPassword);
    assertEquals(goodUser, u);
  }

  @DisplayName("Test connection with bad email")
  @Test
  public void connection2Test() {
    assertNull(userUCC.connection(badEmail, goodPassword));
  }

  @DisplayName("Test connection with bad password")
  @Test
  public void connection3Test() {
    assertNull(userUCC.connection(goodEmail, badPassword));
  }

  @DisplayName("Test connection with empty email")
  @Test
  public void connection4Test() {
    assertNull(userUCC.connection("", goodPassword));
  }

  @DisplayName("Test connection with empty password")
  @Test
  public void connection5Test() {
    assertNull(userUCC.connection(goodEmail, ""));
  }

  @DisplayName("Test connection with empty email and empty password")
  @Test
  public void connection6Test() {
    assertNull(userUCC.connection("", ""));
  }

  @DisplayName("Test connection with good credentials but the user isn't validated yet")
  @Test
  public void connection7Test() {
    assertNull(userUCC.connection(goodEmailNotValidated, goodPassword));
  }


}
