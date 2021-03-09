package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserImpl;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;

public class UserUCCTest {

  private static UserUCC userUCC;
  private User goodUser;
  private User goodUserNotValidated;
  private String goodEmail;
  private String badEmail;
  private String goodPassword;
  private String badPassword;
  private String goodEmailNotValidated;


  @BeforeAll
  public static void init() {
    Config.load();
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    userUCC = locator.getService(UserUCC.class);
  }

  @BeforeEach
  public void setUp() throws Exception {
    goodPassword = "1234";
    goodEmail = "test@test.com";
    badEmail = "test.test@test.com";
    badPassword = "5678";
    goodEmailNotValidated = "test3@test.com";
    
    String str = "2021-01-05 00:00";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
    
    goodUser = new UserImpl();
    goodUser.setId(0);
    goodUser.setUsername("test");
    goodUser.setLastName("Heuzer");
    goodUser.setFirstName("Nina");
    goodUser.setEmail(goodEmail);
    goodUser.setRole("admin");
    goodUser.setRegistrationDate(dateTime);
    goodUser.setValidated(true);
    goodUser.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234
    goodUser.setAddress(1);

    str = "2021-02-07 00:00";
    dateTime = LocalDateTime.parse(str, formatter);
    
    goodUserNotValidated = new UserImpl();
    goodUserNotValidated.setId(1);
    goodUserNotValidated.setUsername("test3");
    goodUserNotValidated.setLastName("de Theux");
    goodUserNotValidated.setFirstName("Boris");
    goodUserNotValidated.setEmail(goodEmailNotValidated);
    goodUserNotValidated.setRole("client");
    goodUserNotValidated.setRegistrationDate(dateTime);
    goodUserNotValidated.setValidated(false);
    goodUserNotValidated
        .setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234
    goodUserNotValidated.setAddress(1);



    // this.goodUser = (User) userDAO.getUser(goodEmail);

    // this.wrongUser = (User) userDAO.getUser(badEmail);

  }

  @Test
  public void demoTest() {
    assertTrue(true);
  }


  // Email et mdp sont bons, sans remember me

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
