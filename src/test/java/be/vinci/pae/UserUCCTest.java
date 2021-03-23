package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;

public class UserUCCTest {

  private static UserUCC userUCC;
  private static User goodUser;
  private static User goodUserNotValidated;
  private static String goodEmail;
  private static String badEmail;
  private static String goodPassword;
  private static String badPassword;
  private static String goodEmailNotValidated;
  private static UserDAO userDAO;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    badEmail = "test.test@test.com";
    badPassword = "5678";

    goodUser = UserDistributor.getGoodValidatedUser();
    goodPassword = "1234";
    goodEmail = goodUser.getEmail();
    goodUserNotValidated = UserDistributor.getGoodNotValidatedUser();
    goodEmailNotValidated = goodUserNotValidated.getEmail();

    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    userUCC = locator.getService(UserUCC.class);

    userDAO = locator.getService(UserDAO.class);
  }

  @BeforeEach
  public void reset() {
    Mockito.reset(userDAO);
  }

  @DisplayName("Test connection with right email and password")
  @Test
  public void connection1Test() {
    Mockito.when(userDAO.getUserFromEmail(goodEmail)).thenReturn(goodUser);
    assertEquals(goodUser, userUCC.connection(goodEmail, goodPassword));
  }

  @DisplayName("Test connection with bad email")
  @Test
  public void connection2Test() {
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(badEmail, goodPassword));
  }

  @DisplayName("Test connection with bad password")
  @Test
  public void connection3Test() {
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(goodEmail, badPassword));
  }

  @DisplayName("Test connection with empty email")
  @Test
  public void connection4Test() {
    assertThrows(UnauthorizedException.class, () -> userUCC.connection("", goodPassword));
  }

  @DisplayName("Test connection with empty password")
  @Test
  public void connection5Test() {
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(goodEmail, ""));
  }

  @DisplayName("Test connection with empty email and empty password")
  @Test
  public void connection6Test() {
    assertThrows(UnauthorizedException.class, () -> userUCC.connection("", ""));
  }

  @DisplayName("Test connection with good credentials but the user isn't validated yet")
  @Test
  public void connection7Test() {
    Mockito.when(userDAO.getUserFromEmail(goodEmailNotValidated)).thenReturn(goodUserNotValidated);
    assertThrows(UnauthorizedException.class,
        () -> userUCC.connection(goodEmailNotValidated, goodPassword));
  }


}
