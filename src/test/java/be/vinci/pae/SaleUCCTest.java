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
import be.vinci.pae.domain.interfaces.FurnitureDTO.Condition;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.domain.interfaces.UserDTO.Role;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.services.dao.interfaces.UserDAO;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class SaleUCCTest {

  private static SaleUCC saleUCC;
  private static SaleDAO saleDAO;
  private static FurnitureDAO furnitureDAO;
  private static UserDAO userDAO;
  private static SaleDTO sale;
  private static FurnitureDTO goodFurniture;
  private static FurnitureDTO badFurniture;
  private static UserDTO user;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    Config.load();


    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    saleUCC = locator.getService(SaleUCC.class);
    saleDAO = locator.getService(SaleDAO.class);
    furnitureDAO = locator.getService(FurnitureDAO.class);
    userDAO = locator.getService(UserDAO.class);
  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(saleDAO);
    sale = ObjectDistributor.getSale();
    goodFurniture = ObjectDistributor.getFurnitureForFurnitureUCCTest();
    badFurniture = ObjectDistributor.getFurnitureForFurnitureUCCTest();
    user = ObjectDistributor.getGoodValidatedUser();
  }

  @DisplayName("Test to getSalesList with a no sale")
  @Test
  public void getSalesListTest1() {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    Mockito.when(saleDAO.getSalesList()).thenReturn(list);
    List<SaleDTO> listB = saleUCC.getSalesList();
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(0, listB.size()));
  }

  @DisplayName("Test to getSalesList with one sale")
  @Test
  public void getSalesListTest2() {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    list.add(sale);
    Mockito.when(saleDAO.getSalesList()).thenReturn(list);
    List<SaleDTO> listB = saleUCC.getSalesList();
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(1, listB.size()));
  }

  @DisplayName("Test to getSalesList with three sales")
  @Test
  public void getSalesListTest3() {
    List<SaleDTO> list = new ArrayList<SaleDTO>();
    list.add(sale);
    list.add(sale);
    list.add(sale);
    Mockito.when(saleDAO.getSalesList()).thenReturn(list);
    List<SaleDTO> listB = saleUCC.getSalesList();
    assertAll(() -> assertEquals(list, listB), () -> assertEquals(3, listB.size()));
  }

  @DisplayName("Testing a sale with an unvalidated user")
  @Test
  public void addSaleTest1() {
    user.setValidated(false);
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale with a null user")
  @Test
  public void addSaleTest2() {
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale with a null sale")
  @Test
  public void addSaleTest3() {
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(null));
  }

  @DisplayName("Testing a sale with an invalid furniture id")
  @Test
  public void addSaleTest4() {
    sale.setIdFurniture(-5);
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(null);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_ATTENTE' with 'admin' user")
  @Test
  public void addSaleTest5() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.EN_ATTENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_ATTENTE' with 'client' user")
  @Test
  public void addSaleTest6() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.EN_ATTENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_ATTENTE' with 'antiquaire' user")
  @Test
  public void addSaleTest7() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    badFurniture.setCondition(Condition.EN_ATTENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'VENDU' with 'admin' user")
  @Test
  public void addSaleTest8() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.VENDU.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'VENDU' with 'client' user")
  @Test
  public void addSaleTest9() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.VENDU.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'VENDU' with 'antiquaire' user")
  @Test
  public void addSaleTest10() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    badFurniture.setCondition(Condition.VENDU.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'REFUSE' with 'admin' user")
  @Test
  public void addSaleTest11() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.REFUSE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'REFUSE' with 'client' user")
  @Test
  public void addSaleTest12() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.REFUSE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'REFUSE' with 'antiquaire' user")
  @Test
  public void addSaleTest13() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    badFurniture.setCondition(Condition.REFUSE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'RETIRE' with 'admin' user")
  @Test
  public void addSaleTest14() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.RETIRE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'RETIRE' with 'client' user")
  @Test
  public void addSaleTest15() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.RETIRE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'RETIRE' with 'antiquaire' user")
  @Test
  public void addSaleTest16() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    badFurniture.setCondition(Condition.RETIRE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'SOUS_OPTION' with 'admin' user")
  @Test
  public void addSaleTest17() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.SOUS_OPTION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'SOUS_OPTION' with 'client' user")
  @Test
  public void addSaleTest18() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.SOUS_OPTION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'SOUS_OPTION' with 'antiquaire' user")
  @Test
  public void addSaleTest19() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    badFurniture.setCondition(Condition.SOUS_OPTION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_VENTE' with 'client' user")
  @Test
  public void addSaleTest20() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    goodFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_VENTE' with 'admin' user")
  @Test
  public void addSaleTest21() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    goodFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_VENTE' with 'antiquaire' user")
  @Test
  public void addSaleTest22() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    goodFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'DEPOSE_EN_MAGASIN' with 'admin' user")
  @Test
  public void addSaleTest23() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'DEPOSE_EN_MAGASIN' with 'client' user")
  @Test
  public void addSaleTest24() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'DEPOSE_EN_MAGASIN' with 'antiquaire' user")
  @Test
  public void addSaleTest25() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    goodFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'ACHETE' with 'admin' user")
  @Test
  public void addSaleTest26() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'ACHETE' with 'client' user")
  @Test
  public void addSaleTest27() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'ACHETE' with 'antiquaire' user")
  @Test
  public void addSaleTest28() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    goodFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_RESTAURATION' with 'admin' user")
  @Test
  public void addSaleTest29() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.ADMIN.toString());
    badFurniture.setCondition(Condition.EN_RESTAURATION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_RESTAURATION' with 'client' user")
  @Test
  public void addSaleTest30() {
    sale.setIdFurniture(badFurniture.getId());
    user.setRole(Role.CLIENT.toString());
    badFurniture.setCondition(Condition.EN_RESTAURATION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_RESTAURATION' with 'antiquaire' user")
  @Test
  public void addSaleTest31() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    goodFurniture.setCondition(Condition.EN_RESTAURATION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(user);
    assertTrue(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_RESTAURATION' with no user")
  @Test
  public void addSaleTest32() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.EN_RESTAURATION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'ACHETE' with no user")
  @Test
  public void addSaleTest33() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.ACHETE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'DEPOSE_EN_MAGASIN' with no user")
  @Test
  public void addSaleTest34() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.DEPOSE_EN_MAGASIN.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_ATTENTE' with no user")
  @Test
  public void addSaleTest35() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.EN_ATTENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'REFUSE' with no user")
  @Test
  public void addSaleTest36() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.REFUSE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'RETIRE' with no user")
  @Test
  public void addSaleTest37() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.RETIRE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'VENDU' with no user")
  @Test
  public void addSaleTest38() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.VENDU.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'SOUS_OPTION' with no user")
  @Test
  public void addSaleTest39() {
    sale.setIdFurniture(badFurniture.getId());
    badFurniture.setCondition(Condition.SOUS_OPTION.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertThrows(BusinessException.class, () -> saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition is 'EN_VENTE' with no user")
  @Test
  public void addSaleTest40() {
    sale.setIdFurniture(goodFurniture.getId());
    user.setRole(Role.ANTIQUAIRE.toString());
    goodFurniture.setCondition(Condition.EN_VENTE.toString());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    Mockito.when(userDAO.getUserFromId(user.getId())).thenReturn(null);
    assertTrue(saleUCC.addSale(sale));
  }

}
