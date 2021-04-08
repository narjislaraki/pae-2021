package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.UserDAO;
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
  private static UserDAO userDAO;
  private static AddressDAO addressDAO;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    Config.load();


    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    userUCC = locator.getService(UserUCC.class);

    userDAO = locator.getService(UserDAO.class);

    addressDAO = locator.getService(AddressDAO.class);
  }

  @BeforeEach
  public void reset() {
    Mockito.reset(userDAO);

    badEmail = UserDistributor.getBadEmail();
    badPassword = UserDistributor.getBadPassword();

    goodUser = UserDistributor.getGoodValidatedUser();
    goodPassword = UserDistributor.getGoodPassword();
    goodEmail = UserDistributor.getGoodEmail();
    goodUserNotValidated = UserDistributor.getGoodNotValidatedUser();
    goodEmailNotValidated = UserDistributor.getGoodEmailNotValidated();
  }

  @DisplayName("Test connection with right email and password")
  @Test
  public void connectionTest1() {
    Mockito.when(userDAO.getUserFromEmail(goodEmail)).thenReturn(goodUser);
    assertEquals(goodUser, userUCC.connection(goodEmail, goodPassword));
  }

  @DisplayName("Test connection with bad email")
  @Test
  public void connectionTest2() {
    Mockito.when(userDAO.getUserFromEmail(badEmail)).thenReturn(null);
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(badEmail, goodPassword));
  }

  @DisplayName("Test connection with bad password")
  @Test
  public void connectionTest3() {
    Mockito.when(userDAO.getUserFromEmail(goodEmail)).thenReturn(goodUser);
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(goodEmail, badPassword));
  }

  @DisplayName("Test connection with empty email")
  @Test
  public void connectionTest4() {
    Mockito.when(userDAO.getUserFromEmail("")).thenReturn(null);
    assertThrows(UnauthorizedException.class, () -> userUCC.connection("", goodPassword));
  }

  @DisplayName("Test connection with empty password")
  @Test
  public void connectionTest5() {
    Mockito.when(userDAO.getUserFromEmail(goodEmail)).thenReturn(goodUser);
    assertThrows(UnauthorizedException.class, () -> userUCC.connection(goodEmail, ""));
  }

  @DisplayName("Test connection with empty email and empty password")
  @Test
  public void connectionTest6() {
    Mockito.when(userDAO.getUserFromEmail("")).thenReturn(null);
    assertThrows(UnauthorizedException.class, () -> userUCC.connection("", ""));
  }

  @DisplayName("Test connection with good credentials but the user isn't validated yet")
  @Test
  public void connectionTest7() {
    Mockito.when(userDAO.getUserFromEmail(goodEmailNotValidated)).thenReturn(goodUserNotValidated);
    assertThrows(UnauthorizedException.class,
        () -> userUCC.connection(goodEmailNotValidated, goodPassword));
  }

  @DisplayName("Test getting user from id with an invalid id")
  @Test
  public void getUserFromIdTest1() {
    int id = -5;
    Mockito.when(userDAO.getUserFromId(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> userUCC.getUserFromId(id));
  }

  @DisplayName("Test getting user from id with a valid id but no user has this id")
  @Test
  public void getUserFromIdTest2() {
    int id = 55;
    Mockito.when(userDAO.getUserFromId(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> userUCC.getUserFromId(id));
  }

  @DisplayName("Test getting user from id with a valid id")
  @Test
  public void getUserFromIdTest3() {
    int id = goodUser.getId();
    Mockito.when(userDAO.getUserFromId(id)).thenReturn(goodUser);
    assertEquals(goodUser, userUCC.getUserFromId(id));
  }

  @DisplayName("Test deleting user from id with an invalid id")
  @Test
  public void deleteUserTest1() {
    int id = -5;
    Mockito.when(userDAO.deleteUser(id)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.deleteUser(id));
  }

  @DisplayName("Test deleting user from id with a valid id but no user has this id")
  @Test
  public void deleteUserTest2() {
    int id = 55;
    Mockito.when(userDAO.deleteUser(id)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.deleteUser(id));
  }

  @DisplayName("Test deleting user from id with a valid id")
  @Test
  public void deleteUserTest3() {
    int id = 1;
    Mockito.when(userDAO.deleteUser(id)).thenReturn(true);
    assertTrue(userUCC.deleteUser(id));
  }

  @DisplayName("Test user's vaidation with an invalid id and a valid role")
  @Test
  public void acceptUserTest1() {
    int id = -5;
    String role = "client";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with an invalid id and an invalid role")
  @Test
  public void acceptUserTest2() {
    int id = -5;
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and an invalid role and the user is already validated")
  @Test
  public void acceptUserTest3() {
    int id = goodUser.getId();
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and a valid role but the user is already validated")
  @Test
  public void acceptUserTest4() {
    int id = goodUser.getId();
    String role = "antiquaire";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and a valid role and the user is not validated yet")
  @Test
  public void acceptUserTest5() {
    int id = goodUserNotValidated.getId();
    String role = "antiquaire";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and a valid role and the user is not validated yet")
  @Test
  public void acceptUserTest6() {
    int id = goodUserNotValidated.getId();
    String role = "client";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and a valid role and the user is not validated yet")
  @Test
  public void acceptUserTest7() {
    int id = goodUserNotValidated.getId();
    String role = "admin";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's vaidation with a valid id and an invalid role and the user is not validated yet")
  @Test
  public void acceptUserTest8() {
    int id = goodUserNotValidated.getId();
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test getting list of not yet validated users when there are no unvalidated user")
  @Test
  public void getUnvalidatedUsersTest1() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    Mockito.when(userDAO.getUnvalidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getUnvalidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(0, listB.size()));
  }

  @DisplayName("Test getting list of not yet validated users when there is one unvalidated user")
  @Test
  public void getUnvalidatedUsersTest2() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    listA.add(goodUserNotValidated);
    Mockito.when(userDAO.getUnvalidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getUnvalidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test getting list of not yet validated users when there is three unvalidated user")
  @Test
  public void getUnvalidatedUsersTest3() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    listA.add(goodUserNotValidated);
    listA.add(goodUserNotValidated);
    listA.add(goodUserNotValidated);
    Mockito.when(userDAO.getUnvalidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getUnvalidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(3, listB.size()));
  }

  @DisplayName("Test registering with an already used email and an already used username")
  @Test
  public void registerTest1() {
    UserDTO user = UserDistributor.getGoodNotValidatedUser();
    user.setUsername(goodUser.getUsername());
    user.setEmail(goodUser.getEmail());
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(goodUser);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(goodUser);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with an already used email and a valid username")
  @Test
  public void registerTest2() {
    UserDTO user = UserDistributor.getGoodNotValidatedUser();
    user.setUsername("James");
    user.setEmail(goodUser.getEmail());
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(goodUser);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(null);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with a valid email and an already used username")
  @Test
  public void registerTest3() {
    UserDTO user = UserDistributor.getGoodNotValidatedUser();
    user.setUsername(goodUser.getUsername());
    user.setEmail("james@bond.uk");
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(null);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(goodUser);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with a valid email and a valid username")
  @Test
  public void registerTest4() {

    UserDTO user = UserDistributor.getGoodNotValidatedUser();
    Mockito.when(addressDAO.addAddress(user.getAddress())).thenReturn(1);
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(null);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(null);
    assertTrue(userUCC.registration(user));
  }

}
