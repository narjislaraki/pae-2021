package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.services.dao.FurnitureDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class FurnitureUCCTest {

  private static FurnitureUCC furnitureUCC;
  private static FurnitureDAO furnitureDAO;

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
  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(furnitureDAO);
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

}
