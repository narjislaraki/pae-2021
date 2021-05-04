package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import be.vinci.pae.domain.edition.EditionDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.furniture.TypeOfFurnitureDTO;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserDTO.Role;
import be.vinci.pae.domain.visit.PhotoDTO;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.exceptions.UnauthorizedException;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.services.dao.SaleDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class FurnitureUCCTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureDAO furnitureDAO;
  private static FurnitureDTO goodFurniture;
  private static FurnitureDTO badFurniture;
  private static OptionDTO goodOption;
  private static SaleDAO saleDAO;
  private static SaleDTO sale;
  private static PhotoDTO photo;
  private static UserDTO goodUser;
  private static String goodReason = "good cancelationReason";
  private static TypeOfFurnitureDTO goodType;
  private static UserDAO userDAO;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    Config.load();


    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    furnitureUCC = locator.getService(FurnitureUCC.class);

    furnitureDAO = locator.getService(FurnitureDAO.class);

    saleDAO = locator.getService(SaleDAO.class);

    userDAO = locator.getService(UserDAO.class);
  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(furnitureDAO);
    goodFurniture = ObjectDistributor.getFurnitureForFurnitureUCCTest();
    badFurniture = ObjectDistributor.getFurnitureForFurnitureUCCTest();
    goodOption = ObjectDistributor.getGoodOption();
    photo = ObjectDistributor.createPhoto();
    goodUser = ObjectDistributor.getGoodValidatedUser();
    goodType = ObjectDistributor.getGoodTypeOfFurniture();
    sale = ObjectDistributor.getSale();
  }

  @DisplayName("Test getting the option by id with a valid id")
  @Test
  public void getOptionTest1() {
    int id = goodOption.getId();
    Mockito.when(furnitureDAO.getOption(id)).thenReturn(goodOption);
    assertEquals(goodOption, furnitureUCC.getOption(id));
  }

  @DisplayName("Test sum of options days with unvalid furniture id and unvalid user id")
  @Test
  public void getSumOfOptionDaysForAUserAboutAFurnitureTest1() {
    int furnitureId = -5;
    int userId = -5;
    assertEquals(0, furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(furnitureId, userId));
  }

  @DisplayName("Test sum of options days with unvalid furniture id and valid user id")
  @Test
  public void getSumOfOptionDaysForAUserAboutAFurnitureTest2() {
    int furnitureId = -5;
    int userId = ObjectDistributor.getGoodValidatedUser().getId();
    assertEquals(0, furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(furnitureId, userId));
  }

  @DisplayName("Test sum of options days with unvalid furniture id and valid user id")
  @Test
  public void getSumOfOptionDaysForAUserAboutAFurnitureTest3() {
    int furnitureId = ObjectDistributor.getFurnitureInSale().getId();
    int userId = -5;
    assertEquals(0, furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(furnitureId, userId));
  }

  @DisplayName("Test sum of options days with valid furniture id and valid user id")
  @Test
  public void getSumOfOptionDaysForAUserAboutAFurnitureTest4() {
    int furnitureId = ObjectDistributor.getFurnitureInSale().getId();
    int userId = ObjectDistributor.getGoodValidatedUser().getId();
    Mockito.when(furnitureDAO.getSumOfOptionDaysForAUserAboutAFurniture(furnitureId, userId))
        .thenReturn(3);
    assertEquals(3, furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(furnitureId, userId));
  }

  @DisplayName("Test to indicate sentToWorkshop with invalid condition furniture")
  @Test
  public void indicateSentToWorkshopTest1() {
    badFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(badFurniture.getId())).thenReturn(badFurniture);
    assertThrows(BusinessException.class,
        () -> furnitureUCC.indicateSentToWorkshop(badFurniture.getId()));
  }

  @DisplayName("Test to indicate sentToWorkshop with a valid condition furniture")
  @Test
  public void indicateSentToWorkshopTest2() {
    goodFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.indicateSentToWorkshop(goodFurniture.getId()));
  }

  @DisplayName("Test to indicate dropOfStore with a invalid condition furniture")
  @Test
  public void indicateDropOfStoreTest1() {
    badFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(badFurniture.getId())).thenReturn(badFurniture);
    assertThrows(BusinessException.class,
        () -> furnitureUCC.indicateDropOfStore(badFurniture.getId()));
  }

  @DisplayName("Test to indicate dropOfStore with a valid condition furniture")
  @Test
  public void indicateDropOfStoreTest2() {
    goodFurniture.setCondition(Condition.EN_RESTAURATION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.indicateDropOfStore(goodFurniture.getId()));
  }

  @DisplayName("Test to indicate dropOfStore with a second valid condition furniture")
  @Test
  public void indicateDropOfStoreTest3() {
    goodFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.indicateDropOfStore(goodFurniture.getId()));
  }


  @DisplayName("Test to indicate offerdForSale with a valid condition furniture and invalid price")
  @Test
  public void indicateOfferedForSaleTest1() {
    double price = -8;
    goodFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertThrows(BusinessException.class,
        () -> furnitureUCC.indicateOfferedForSale(goodFurniture.getId(), price));
  }

  @DisplayName("Test to indicate offerdForSale with a invalid condition furniture and valid price")
  @Test
  public void indicateOfferedForSaleTest2() {
    double price = 22;
    badFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(badFurniture.getId())).thenReturn(badFurniture);
    assertThrows(BusinessException.class,
        () -> furnitureUCC.indicateOfferedForSale(badFurniture.getId(), price));
  }

  @DisplayName("Test to indicate offerdForSale with a valid condition furniture and valid price")
  @Test
  public void indicateOfferedForSaleTest3() {
    double price = 22;
    goodFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.indicateOfferedForSale(goodFurniture.getId(), price));
  }


  @DisplayName("Test to withdraw a sale with a invalid condition furniture")
  @Test
  public void withdrawSaleTest1() {
    badFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(badFurniture.getId())).thenReturn(badFurniture);
    assertThrows(BusinessException.class, () -> furnitureUCC.withdrawSale(badFurniture.getId()));
  }

  @DisplayName("Test to withdraw a sale with a valid condition furniture")
  @Test
  public void withdrawSaleTest2() {
    goodFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.withdrawSale(goodFurniture.getId()));
  }

  @DisplayName("Test to withdraw a sale with a second valid condition furniture")
  @Test
  public void withdrawSaleTest3() {
    goodFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertDoesNotThrow(() -> furnitureUCC.withdrawSale(goodFurniture.getId()));
  }

  @DisplayName("Test  to introduce a option with a term <= 0")
  @Test
  public void introduceOptionTest1() {
    int term = 0;
    assertThrows(UnauthorizedException.class,
        () -> furnitureUCC.introduceOption(term, goodUser.getId(), goodFurniture.getId()));
  }

  @DisplayName("Test to introduce a option when the alowed day are already reach")
  @Test
  public void introduceOptionTest2() {
    Mockito.when(furnitureDAO.getSumOfOptionDaysForAUserAboutAFurniture(goodFurniture.getId(),
        goodUser.getId())).thenReturn(5);
    assertThrows(UnauthorizedException.class,
        () -> furnitureUCC.introduceOption(2, goodUser.getId(), goodFurniture.getId()));
  }

  @DisplayName("Test to introduce a option with a sum >5")
  @Test
  public void introduceOptionTest3() {
    int term = 3;
    Mockito.when(furnitureDAO.getSumOfOptionDaysForAUserAboutAFurniture(goodFurniture.getId(),
        goodUser.getId())).thenReturn(3);
    assertThrows(UnauthorizedException.class,
        () -> furnitureUCC.introduceOption(term, goodUser.getId(), goodFurniture.getId()));
  }

  @DisplayName("Test to introduce a good option ")
  @Test
  public void introduceOptionTest4() {
    int term = 2;
    Mockito.when(furnitureDAO.getSumOfOptionDaysForAUserAboutAFurniture(goodFurniture.getId(),
        goodUser.getId())).thenReturn(3);
    assertDoesNotThrow(
        () -> furnitureUCC.introduceOption(term, goodUser.getId(), goodFurniture.getId()));
  }

  @DisplayName("Test to cancel an option  with a id <= 0")
  @Test
  public void cancelOptionTest1() {
    int id = -5;
    assertThrows(BusinessException.class,
        () -> furnitureUCC.cancelOption(goodReason, id, goodUser));
  }

  @DisplayName("Test to cancel an option with a different id user")
  @Test
  public void cancelOptionTest2() {
    int id = goodUser.getId() + 1;
    goodOption.setIdUser(id);
    goodUser.setRole("client");
    Mockito.when(furnitureDAO.getOption(goodOption.getId())).thenReturn(goodOption);
    assertThrows(BusinessException.class,
        () -> furnitureUCC.cancelOption(goodReason, goodOption.getId(), goodUser));
  }

  @DisplayName("Test to cancel an option with a similar id")
  @Test
  public void cancelOptionTest3() {
    int id = goodUser.getId();
    goodOption.setIdUser(id);
    goodUser.setRole("client");
    Mockito.when(furnitureDAO.getOption(goodOption.getId())).thenReturn(goodOption);
    Mockito.when(furnitureDAO.getFurnitureById(goodOption.getIdFurniture()))
        .thenReturn(goodFurniture);
    Mockito.when(furnitureDAO.cancelOption(goodReason, goodOption.getId()))
        .thenReturn(goodOption.getIdFurniture());
    assertDoesNotThrow(() -> furnitureUCC.cancelOption(goodReason, goodOption.getId(), goodUser));
  }

  @DisplayName("Test to cancel an option when user is admin")
  @Test
  public void cancelOptionTest4() {
    int id = goodUser.getId() + 1;
    goodOption.setIdUser(id);
    Mockito.when(furnitureDAO.getOption(goodOption.getId())).thenReturn(goodOption);
    Mockito.when(furnitureDAO.getFurnitureById(goodOption.getIdFurniture()))
        .thenReturn(goodFurniture);
    Mockito.when(furnitureDAO.cancelOption(goodReason, goodOption.getId()))
        .thenReturn(goodOption.getIdFurniture());
    assertDoesNotThrow(() -> furnitureUCC.cancelOption(goodReason, goodOption.getId(), goodUser));
  }

  @DisplayName("Test to get the furniture list with a null user")
  @Test
  public void getFurnitureListTest1() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    Mockito.when(furnitureDAO.getPublicFurnitureList()).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureList(null);
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to get the furniture list with a client user")
  @Test
  public void getFurnitureListTest2() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    goodUser.setRole(Role.CLIENT.toString());
    Mockito.when(furnitureDAO.getPublicFurnitureList()).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureList(goodUser);
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to get the furniture list with a non null user admin")
  @Test
  public void getFurnitureListTest3() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    Mockito.when(furnitureDAO.getFurnitureList()).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureList(goodUser);
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to get the furniture list with a null user")
  @Test
  public void getFurnitureListByTypeTest1() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);

    Mockito.when(furnitureDAO.getPublicFurnitureListByType(goodType.getId())).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureListByType(null, goodType.getId());
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to get the furniture list with a client user")
  @Test
  public void getFurnitureListByTypeTest2() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    goodUser.setRole(Role.CLIENT.toString());
    Mockito.when(furnitureDAO.getPublicFurnitureListByType(goodType.getId())).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureListByType(goodUser, goodType.getId());
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to get the furniture list with a non null user admin")
  @Test
  public void getFurnitureListByTypeTest3() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    Mockito.when(furnitureDAO.getFurnitureListByType(goodType.getId())).thenReturn(list);
    List<FurnitureDTO> listB = furnitureUCC.getFurnitureListByType(goodUser, goodType.getId());
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test getting furniture by id with an invalid id")
  @Test
  public void getFurnitureByIdTest1() {
    int id = -5;
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> furnitureUCC.getFurnitureById(id));
  }

  @DisplayName("Test getting furniture by id with a valid id but no furniture has this id")
  @Test
  public void getFurnitureByIdTest2() {
    int id = 56;
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> furnitureUCC.getFurnitureById(id));
  }

  @DisplayName("Test getting furniture by id with valid id")
  @Test
  public void getFurnitureByIdTest3() {
    int id = goodFurniture.getId();
    photo.setIdFurniture(goodFurniture.getId());
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(goodFurniture);
    Mockito.when(furnitureDAO.getFavouritePhotoById(goodFurniture.getFavouritePhotoId()))
        .thenReturn(photo.getPhoto());
    Mockito.when(furnitureDAO.getFurnitureTypeById(goodType.getId()))
        .thenReturn(goodType.getLabel());
    assertEquals(goodFurniture, furnitureUCC.getFurnitureById(id));
  }

  @DisplayName("Test getting furniture by id with a valid id seller")
  @Test
  public void getFurnitureByIdTest4() {
    int id = goodFurniture.getId();
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(goodFurniture);
    assertEquals(goodFurniture.getSellerId(), furnitureUCC.getFurnitureById(id).getSellerId());
  }

  @DisplayName("Test getting furniture by id with a valid id photo")
  @Test
  public void getFurnitureByIdTest5() {

  }

  @DisplayName("Test to cancel a option after his term")
  @Test
  public void cancelOvertimedOptionsTest1() {
    assertDoesNotThrow(() -> furnitureUCC.cancelOvertimedOptions());
  }

  @DisplayName("Test get the list of the type of furniture")
  @Test
  public void getTypesOfFurnitureListTest1() {
    List<TypeOfFurnitureDTO> list = new ArrayList<TypeOfFurnitureDTO>();
    list.add(goodType);
    list.add(goodType);
    Mockito.when(furnitureDAO.getTypesOfFurnitureList()).thenReturn(list);
    List<TypeOfFurnitureDTO> listB = furnitureUCC.getTypesOfFurnitureList();
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(2, listB.size()));
  }


  @DisplayName("Testing a sale which condition is 'VENDU' ")
  @Test
  public void addSaleTest1() {
    SaleDTO saleDTO = sale;
    sale.setIdFurniture(1);
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    badFurniture.setCondition(Condition.VENDU.toString());
    assertFalse(furnitureUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition isn't 'VENDU' but 'EN_ATTENTE")
  @Test
  public void addSaleTest2() {
    sale.setIdFurniture(goodFurniture.getId());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    goodFurniture.setCondition(Condition.EN_ATTENTE.toString());
    assertTrue(furnitureUCC.addSale(sale));
  }

  @DisplayName("Testing getting a photo of a furniture by an id")
  @Test
  public void getFurniturePhotosTest1() {
    int id = goodFurniture.getId();
    List<PhotoDTO> listA = new ArrayList<PhotoDTO>();
    listA.add(photo);
    listA.add(photo);
    Mockito.when(furnitureDAO.getFurniturePhotos(id)).thenReturn(listA);
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(goodFurniture);
    List<PhotoDTO> listB = furnitureUCC.getFurniturePhotos(id, goodUser);
    assertAll(() -> assertEquals(listA, listB), () -> assertEquals(2, listB.size()));
  }

  @DisplayName("Test getting furniture with photo by an id with a null furniture")
  @Test
  public void getFurnitureWithPhotosByIdTest1() {
    int id = badFurniture.getId();
    Mockito.when(furnitureDAO.getFurnitureById(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> furnitureUCC.getFurnitureWithPhotosById(id));
  }

  @DisplayName("Test getting furniture with photo by id")
  @Test
  public void getFurnitureWithPhotosByIdTest2() {
    goodFurniture.setSeller(goodUser);
    goodFurniture.setSellerId(goodUser.getId());
    goodFurniture.setFavouritePhoto(photo.getPhoto());
    goodFurniture.setTypeId(goodType.getId());
    goodFurniture.setType(goodType.getLabel());
    int sellerId = goodFurniture.getSellerId();
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(sellerId)).thenReturn(goodUser);
    Mockito.when(furnitureDAO.getFavouritePhotoById(goodFurniture.getFavouritePhotoId()))
        .thenReturn(photo.getPhoto());
    Mockito.when(furnitureDAO.getFurnitureTypeById(goodFurniture.getTypeId()))
        .thenReturn(goodType.getLabel());
    assertEquals(goodFurniture, furnitureUCC.getFurnitureWithPhotosById(goodFurniture.getId()));
  }

  @DisplayName("Test to process a list of visit")
  @Test
  public void processVisitTest1() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    list.add(goodFurniture);
    list.add(goodFurniture);
    assertTrue(furnitureUCC.processVisit(list));
  }

  @DisplayName("Test edit with empty furniture id")
  @Test
  public void editTest1() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    assertThrows(BusinessException.class, () -> furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and nothing else")
  @Test
  public void editTest2() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a valid price of 0")
  @Test
  public void editTest3() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(0);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a valid price of 100")
  @Test
  public void editTest4() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(100);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and an invalid negative price")
  @Test
  public void editTest5() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(-1);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a valid id type of 1")
  @Test
  public void editTest6() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(1);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and an invalid id type < 1 (0)")
  @Test
  public void editTest7() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(0);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and an invalid id type < 1 (-1)")
  @Test
  public void editTest8() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setOfferedSellingPrice(-1);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a valid description != to the original")
  @Test
  public void editTest9() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setDescription("Random description");
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertAll(() -> assertFalse(edition.getDescription().equals(goodFurniture.getDescription())),
        () -> assertTrue(furnitureUCC.edit(edition)));
  }

  @DisplayName("Test edit with valid furniture id, and a valid description == to the original")
  @Test
  public void editTest10() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setDescription(goodFurniture.getDescription());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertAll(() -> assertTrue(edition.getDescription().equals(goodFurniture.getDescription())),
        () -> assertTrue(furnitureUCC.edit(edition)));
  }

  @DisplayName("Test edit with valid furniture id, and an empty photo list to add")
  @Test
  public void editTest11() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setPhotosToAdd(new ArrayList<PhotoDTO>());
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a non empty photo list to add but ! related")
  @Test
  public void editTest12() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setPhotosToAdd(new ArrayList<PhotoDTO>());
    photo.setIdFurniture(goodFurniture.getId() + 1);
    edition.getPhotosToAdd().add(photo);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    Mockito.when(furnitureDAO.addAdminPhoto(photo, goodFurniture.getId()))
        .thenReturn(goodFurniture.getId());
    assertTrue(furnitureUCC.edit(edition));
  }

  @DisplayName("Test edit with valid furniture id, and a non empty photo list to add related")
  @Test
  public void editTest13() {
    EditionDTO edition = ObjectDistributor.getEmptyEdition();
    edition.setIdFurniture(goodFurniture.getId());
    edition.setPhotosToAdd(new ArrayList<PhotoDTO>());
    edition.getPhotosToAdd().add(photo);
    Mockito.when(furnitureDAO.getFurnitureById(goodFurniture.getId())).thenReturn(goodFurniture);
    Mockito.when(furnitureDAO.addAdminPhoto(photo, goodFurniture.getId()))
        .thenReturn(goodFurniture.getId());
    assertTrue(furnitureUCC.edit(edition));
  }


}
