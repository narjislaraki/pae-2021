package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import be.vinci.pae.services.dao.interfaces.FurnitureDAO;
import be.vinci.pae.services.dao.interfaces.SaleDAO;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class SaleUCCTest {

  private static SaleUCC saleUCC;
  private static SaleDAO saleDAO;
  private static FurnitureDAO furnitureDAO;
  private static SaleDTO sale;
  private static FurnitureDTO goodFurniture;
  private static FurnitureDTO badFurniture;

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

  @DisplayName("Testing a sale which condition is 'VENDU' ")
  @Test
  public void addSaleTest1() {
    sale.setIdFurniture(1);
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(badFurniture);
    badFurniture.setCondition(Condition.VENDU.toString());
    assertFalse(saleUCC.addSale(sale));
  }

  @DisplayName("Testing a sale which condition isn't 'VENDU' but 'EN_ATTENTE")
  @Test
  public void addSaleTest2() {
    sale.setIdFurniture(goodFurniture.getId());
    Mockito.when(furnitureDAO.getFurnitureById(sale.getIdFurniture())).thenReturn(goodFurniture);
    goodFurniture.setCondition(Condition.EN_ATTENTE.toString());
    assertTrue(saleUCC.addSale(sale));
  }
}
