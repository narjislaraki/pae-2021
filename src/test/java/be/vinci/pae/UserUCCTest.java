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

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.User;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.services.dao.interfaces.AddressDAO;
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.services.dao.interfaces.UserDAO;
import be.vinci.pae.ucc.interfaces.UserUCC;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class UserUCCTest {

  private static UserUCC userUCC;
  private static User goodUser;
  private static User goodUserNotValidated;
  private static SaleDTO goodSale;
  private static FurnitureDTO goodFurniture;
  private static String goodEmail;
  private static String badEmail;
  private static String goodPassword;
  private static String badPassword;
  private static String goodEmailNotValidated;
  private static UserDAO userDAO;
  private static AddressDAO addressDAO;
  private static SaleDAO saleDAO;
  private static FurnitureDAO furnitureDAO;

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

    furnitureDAO = locator.getService(FurnitureDAO.class);

    saleDAO = locator.getService(SaleDAO.class);


  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(userDAO);

    badEmail = ObjectDistributor.getBadEmail();
    badPassword = ObjectDistributor.getBadPassword();

    goodUser = ObjectDistributor.getGoodValidatedUser();
    goodPassword = ObjectDistributor.getGoodPassword();
    goodEmail = ObjectDistributor.getGoodEmail();
    goodUserNotValidated = ObjectDistributor.getGoodNotValidatedUser();
    goodEmailNotValidated = ObjectDistributor.getGoodEmailNotValidated();
    goodSale = ObjectDistributor.getSale();
    goodFurniture = ObjectDistributor.getFurnitureInSale();
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

  @DisplayName("Test user's validation with an invalid id and a valid role")
  @Test
  public void acceptUserTest1() {
    int id = -5;
    String role = "client";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with an invalid id and an invalid role")
  @Test
  public void acceptUserTest2() {
    int id = -5;
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, invalid role and the user is validated")
  @Test
  public void acceptUserTest3() {
    int id = goodUser.getId();
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, valid role but the user is validated")
  @Test
  public void acceptUserTest4() {
    int id = goodUser.getId();
    String role = "antiquaire";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(false);
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, valid role and the user is not validated")
  @Test
  public void acceptUserTest5() {
    int id = goodUserNotValidated.getId();
    String role = "antiquaire";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id,a valid role and the user is not validated")
  @Test
  public void acceptUserTest6() {
    int id = goodUserNotValidated.getId();
    String role = "client";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, valid role and the user is not validated")
  @Test
  public void acceptUserTest7() {
    int id = goodUserNotValidated.getId();
    String role = "admin";
    Mockito.when(userDAO.acceptUser(id, role)).thenReturn(true);
    assertTrue(userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, invalid role and the user is not validated")
  @Test
  public void acceptUserTest8() {
    int id = goodUserNotValidated.getId();
    String role = "wrong";
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test user's validation with valid id, null role and the user is not validated")
  @Test
  public void acceptUserTest9() {
    int id = goodUserNotValidated.getId();
    String role = null;
    assertThrows(BusinessException.class, () -> userUCC.acceptUser(id, role));
  }

  @DisplayName("Test getting list of validated users when there are no validated user")
  @Test
  public void getValidatedUsersTest1() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    Mockito.when(userDAO.getValidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getValidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(0, listB.size()));
  }

  @DisplayName("Test getting list of validated users when there is one validated user")
  @Test
  public void getValidatedUsersTest2() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    listA.add(goodUser);
    Mockito.when(userDAO.getValidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getValidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(1, listB.size()));

  }

  @DisplayName("Test getting list of validated users when there is three validated users")
  @Test
  public void getValidatedUsersTest3() {
    List<UserDTO> listA = new ArrayList<UserDTO>();
    listA.add(goodUser);
    listA.add(goodUser);
    listA.add(goodUser);
    Mockito.when(userDAO.getValidatedUsers()).thenReturn(listA);
    List<UserDTO> listB = userUCC.getValidatedUsers();
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(3, listB.size()));
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

  @DisplayName("Test getting list of not yet validated users when there is three unvalidated users")
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
    UserDTO user = ObjectDistributor.getGoodNotValidatedUser();
    user.setUsername(goodUser.getUsername());
    user.setEmail(goodUser.getEmail());
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(goodUser);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(goodUser);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with an already used email and a valid username")
  @Test
  public void registerTest2() {
    UserDTO user = ObjectDistributor.getGoodNotValidatedUser();
    user.setUsername("James");
    user.setEmail(goodUser.getEmail());
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(goodUser);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(null);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with a valid email and an already used username")
  @Test
  public void registerTest3() {
    UserDTO user = ObjectDistributor.getGoodNotValidatedUser();
    user.setUsername(goodUser.getUsername());
    user.setEmail("james@bond.uk");
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(null);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(goodUser);
    assertThrows(BusinessException.class, () -> userUCC.registration(user));
  }

  @DisplayName("Test registering with a valid email and a valid username")
  @Test
  public void registerTest4() {

    UserDTO user = ObjectDistributor.getGoodNotValidatedUser();
    Mockito.when(addressDAO.addAddress(user.getAddress())).thenReturn(1);
    Mockito.when(userDAO.getUserFromEmail(user.getEmail())).thenReturn(null);
    Mockito.when(userDAO.getUserFromUsername(user.getUsername())).thenReturn(null);
    assertTrue(userUCC.registration(user));
  }

  @DisplayName("Test getTransactionsBuyer with a empty list")
  @Test
  public void getTransactionsBuyerTest1() {
    List<SaleDTO> listA = new ArrayList<SaleDTO>();
    Mockito.when(saleDAO.getTransactionsBuyer(goodUser.getId())).thenReturn(listA);
    List<SaleDTO> listB = userUCC.getTransactionsBuyer(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(0, listB.size()));
  }

  @DisplayName("Test getTransactionsBuyer with a list of one sale")
  @Test
  public void getTransactionsBuyerTest2() {
    List<SaleDTO> listA = new ArrayList<SaleDTO>();
    goodSale.setIdBuyer(goodUser.getId());
    listA.add(goodSale);
    Mockito.when(saleDAO.getTransactionsBuyer(goodUser.getId())).thenReturn(listA);
    List<SaleDTO> listB = userUCC.getTransactionsBuyer(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test getTransactionsBuyer with a list of threes sales")
  @Test
  public void getTransactionsBuyerTest3() {
    List<SaleDTO> listA = new ArrayList<SaleDTO>();
    goodSale.setIdBuyer(goodUser.getId());
    listA.add(goodSale);
    listA.add(goodSale);
    listA.add(goodSale);
    Mockito.when(saleDAO.getTransactionsBuyer(goodUser.getId())).thenReturn(listA);
    List<SaleDTO> listB = userUCC.getTransactionsBuyer(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(3, listB.size()));
  }

  @DisplayName("Test getTransactionsSeller with a empty list")
  @Test
  public void getTransactionsSellerTest1() {
    List<FurnitureDTO> listA = new ArrayList<FurnitureDTO>();
    Mockito.when(furnitureDAO.getTransactionsSeller(goodUser.getId())).thenReturn(listA);
    List<FurnitureDTO> listB = userUCC.getTransactionsSeller(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(0, listB.size()));
  }

  @DisplayName("Test getTransactionsSeller with a list of one furniture")
  @Test
  public void getTransactionsSellerTest2() {
    List<FurnitureDTO> listA = new ArrayList<FurnitureDTO>();
    goodFurniture.setSellerId(goodUser.getId());
    listA.add(goodFurniture);
    Mockito.when(furnitureDAO.getTransactionsSeller(goodUser.getId())).thenReturn(listA);
    List<FurnitureDTO> listB = userUCC.getTransactionsSeller(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test getTransactionsSeller with a list of threes furnitures")
  @Test
  public void getTransactionsSellerTest3() {
    List<FurnitureDTO> listA = new ArrayList<FurnitureDTO>();
    goodSale.setFurniture(goodFurniture);
    goodFurniture.setSellerId(goodUser.getId());
    listA.add(goodFurniture);
    listA.add(goodFurniture);
    listA.add(goodFurniture);
    Mockito.when(furnitureDAO.getTransactionsSeller(goodUser.getId())).thenReturn(listA);
    List<FurnitureDTO> listB = userUCC.getTransactionsSeller(goodUser.getId());
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(3, listB.size()));
  }

}
