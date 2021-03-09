package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.domain.UserUCCImpl;
import be.vinci.pae.services.MockUserDAO;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;
import jakarta.inject.Inject;

public class UserUCCTest {

  private UserUCC userUCC;
  private User goodUser;
  private User wrongUser;
  @Inject
  private UserDAO userDAO;
  private String goodEmail;
  private String badEmail;
  private String goodPassword;
  private String badPassword;
  private String goodEmailNotValidated;

  @BeforeEach
  public void setUp() throws Exception {
   // ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
   // this.userUCC = locator.getService(UserUCC.class);
    
    
    this.goodEmail = "valid@email.com";
    this.goodPassword = "1234";
    this.badEmail = "invalid@email.com";
    this.badPassword = "5678";
    this.goodEmailNotValidated = "test3@test.com";
    // this.goodUser = (User) userDAO.getUser(goodEmail);
    
    // this.wrongUser = (User) userDAO.getUser(badEmail);

  }

  @Test
  public void demoTest() {
    assertTrue(true);
  }

/*
  // Email et mdp sont bons, sans remember me

  @DisplayName("Test connection with right email and password")
  @Test
  public void connection1Test() {
    assertEquals(goodUser, userUCC.connection(goodEmail, goodPassword));
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

*/
}
