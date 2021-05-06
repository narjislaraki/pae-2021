package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitDTO.VisitCondition;
import be.vinci.pae.domain.visit.VisitUCC;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.services.dao.VisitDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class VisitUCCTest {

  private static VisitUCC visitUCC;
  private static VisitDAO visitDAO;
  private static UserDAO userDAO;
  private static VisitDTO goodVisit;
  private static VisitDTO goodVisitWithoutAddress;
  private static UserDTO goodValidatedUser;
  private static LocalDateTime goodScheduledDateTime;
  private static LocalDateTime badScheduledDateTime;
  private static String goodExplanatoryNote;
  private static String badExplanatoryNote;
  private static String emptyExplanatoryNote;

  /**
   * Initialisation before every tests.
   */
  @BeforeAll
  public static void init() {
    Config.load();


    ServiceLocator locator =
        ServiceLocatorUtilities.bind(new ApplicationBinder(), new ApplicationBinderTest());
    visitUCC = locator.getService(VisitUCC.class);

    visitDAO = locator.getService(VisitDAO.class);

    userDAO = locator.getService(UserDAO.class);
  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(visitDAO);
    goodVisit = ObjectDistributor.getGoodVisit();
    goodScheduledDateTime = ObjectDistributor.getGoodScheduledDateTime();
    badScheduledDateTime = ObjectDistributor.getBadScheduledDateTime();
    goodExplanatoryNote = ObjectDistributor.getGoodExplanatoryNote();
    badExplanatoryNote = ObjectDistributor.getBadExplanatoryNote();
    emptyExplanatoryNote = ObjectDistributor.getEmptyExplanatoryNote();
    goodVisitWithoutAddress = ObjectDistributor.getGoodVisitWithoutWarehouseAddress();
    goodValidatedUser = ObjectDistributor.getGoodValidatedUser();
  }

  @DisplayName("Test getNotConfirmedVisits with empty list")
  @Test
  public void getNotConfirmedVisitsTest1() {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    Mockito.when(visitDAO.getNotConfirmedVisits()).thenReturn(list);
    assertEquals(list, visitUCC.getNotConfirmedVisits());
  }

  @DisplayName("Test getNotConfirmedVisits with one visit")
  @Test
  public void getNotConfirmedVisitsTest2() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    listA.add(ObjectDistributor.getNotConfirmedVisitDTO());
    Mockito.when(visitDAO.getNotConfirmedVisits()).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getNotConfirmedVisits();
    assertAll(() -> assertEquals(listA, listB),
        () -> assertEquals(VisitCondition.EN_ATTENTE, listB.get(0).getVisitCondition()));
  }

  @DisplayName("Test getNotConfirmedVisits with three visit")
  @Test
  public void getNotConfirmedVisitsTest3() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    listA.add(ObjectDistributor.getNotConfirmedVisitDTO());
    listA.add(ObjectDistributor.getNotConfirmedVisitDTO());
    listA.add(ObjectDistributor.getNotConfirmedVisitDTO());
    Mockito.when(visitDAO.getNotConfirmedVisits()).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getNotConfirmedVisits();
    assertAll(() -> assertEquals(listA, listB), () -> {
      for (int i = 0; i < listB.size(); i++) {
        assertEquals(VisitCondition.EN_ATTENTE, listB.get(i).getVisitCondition());
      }
    });
  }

  @DisplayName("Test getVisitsToBeProcessed with empty list")
  @Test
  public void getVisitsToBeProcessedTest1() {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    Mockito.when(visitDAO.getVisitsToBeProcessed()).thenReturn(list);
    assertEquals(list, visitUCC.getVisitsToBeProcessed());
  }

  @DisplayName("Test getVisitsToBeProcessed with one visit")
  @Test
  public void getVisitsToBeProcessedTest2() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    Mockito.when(visitDAO.getVisitsToBeProcessed()).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getVisitsToBeProcessed();
    assertAll(() -> assertEquals(listA, listB),
        () -> assertEquals(VisitCondition.ACCEPTEE, listB.get(0).getVisitCondition()),
        () -> assertEquals(Condition.EN_ATTENTE,
            listB.get(0).getFurnitureList().get(0).getCondition()));
  }

  @DisplayName("Test getVisitsToBeProcessed with three visit")
  @Test
  public void getVisitsToBeProcessedTest3() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    Mockito.when(visitDAO.getVisitsToBeProcessed()).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getVisitsToBeProcessed();
    assertAll(() -> assertEquals(listA, listB), () -> {
      for (int i = 0; i < listB.size(); i++) {
        assertEquals(VisitCondition.ACCEPTEE, listB.get(i).getVisitCondition());
        assertEquals(Condition.EN_ATTENTE, listB.get(i).getFurnitureList().get(0).getCondition());
      }
    });
  }

  @DisplayName("Test getVisitsListForAClient with empty list")
  @Test
  public void getVisitsListForAClientTest1() {
    List<VisitDTO> list = new ArrayList<VisitDTO>();
    UserDTO user = ObjectDistributor.getGoodValidatedUser();
    Mockito.when(visitDAO.getVisitsListForAClient(user.getId())).thenReturn(list);
    assertEquals(list, visitUCC.getVisitsListForAClient(user.getId()));
  }

  @DisplayName("Test getVisitsListForAClient with one visit")
  @Test
  public void getVisitsListForAClientTest2() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    UserDTO user = ObjectDistributor.getGoodValidatedUser();
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    Mockito.when(visitDAO.getVisitsListForAClient(user.getId())).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getVisitsListForAClient(user.getId());
    assertEquals(listA, listB);
  }

  @DisplayName("Test getVisitsListForAClient with three visit")
  @Test
  public void getVisitsListForAClientTest3() {
    List<VisitDTO> listA = new ArrayList<VisitDTO>();
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    listA.add(ObjectDistributor.getVisitsToBeProcessedDTO());
    UserDTO user = ObjectDistributor.getGoodValidatedUser();
    Mockito.when(visitDAO.getVisitsListForAClient(user.getId())).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getVisitsListForAClient(user.getId());
    assertEquals(listA, listB);
  }


  @DisplayName("Test getListFurnituresForOneVisit with empty list")
  @Test
  public void getListFurnituresForOneVisitTest1() {
    List<FurnitureDTO> list = new ArrayList<FurnitureDTO>();
    VisitDTO visitDTO = ObjectDistributor.getVisitWithNoFurniture();
    Mockito.when(visitDAO.getListFurnituresForOnVisit(visitDTO.getIdRequest())).thenReturn(list);
    assertEquals(list, visitUCC.getListFurnituresForOneVisit(visitDTO.getIdRequest()));
  }

  @DisplayName("Test getListFurnituresForOneVisit with one furniture")
  @Test
  public void getListFurnituresForOneVisitTest2() {
    List<FurnitureDTO> listA = new ArrayList<FurnitureDTO>();
    VisitDTO visitDTO = ObjectDistributor.getVisitWithNoFurniture();
    FurnitureDTO furniture = ObjectDistributor.getFurnitureForVisitUCCTest();
    furniture.setRequestForVisitId(visitDTO.getIdRequest());
    listA.add(furniture);
    Mockito.when(visitDAO.getListFurnituresForOnVisit(visitDTO.getIdRequest())).thenReturn(listA);
    List<FurnitureDTO> listB = visitUCC.getListFurnituresForOneVisit(visitDTO.getIdRequest());
    assertAll(() -> assertEquals(listA, listB));
  }

  @DisplayName("Test getListFurnituresForOneVisit with three furnitures")
  @Test
  public void getListFurnituresForOneVisitTest3() {
    List<FurnitureDTO> listA = new ArrayList<FurnitureDTO>();
    VisitDTO visitDTO = ObjectDistributor.getVisitWithNoFurniture();
    FurnitureDTO furniture = ObjectDistributor.getFurnitureForVisitUCCTest();
    furniture.setRequestForVisitId(visitDTO.getIdRequest());
    listA.add(furniture);
    listA.add(furniture);
    listA.add(furniture);
    Mockito.when(visitDAO.getListFurnituresForOnVisit(visitDTO.getIdRequest())).thenReturn(listA);
    List<FurnitureDTO> listB = visitUCC.getListFurnituresForOneVisit(visitDTO.getIdRequest());
    assertAll(() -> assertEquals(listA, listB));
  }

  @DisplayName("Test getting visit by id with an invalid id")
  @Test
  public void getVisitByIdTest1() {
    int id = -5;
    Mockito.when(visitDAO.getVisitById(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> visitUCC.getVisitById(id));
  }

  @DisplayName("Test getting visit by id with a valid id but no visit has this id")
  @Test
  public void getVisitByIdTest2() {
    int id = 55;
    Mockito.when(visitDAO.getVisitById(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> visitUCC.getVisitById(id));
  }

  @DisplayName("Test getting visit by id with a valid id")
  @Test
  public void getVisitByIdTest3() {
    int id = goodVisit.getIdRequest();
    Mockito.when(visitDAO.getVisitById(id)).thenReturn(goodVisit);
    assertEquals(goodVisit, visitUCC.getVisitById(id));
  }

  @DisplayName("Test getting visit by id with a valid id and a idAdress == 0")
  @Test
  public void getVisitByIdTest4() {
    int id = goodVisit.getIdRequest();
    goodVisit.setWarehouseAddressId(0);
    Mockito.when(visitDAO.getVisitById(id)).thenReturn(goodVisit);
    assertEquals(goodVisit, visitUCC.getVisitById(id));
  }

  @DisplayName("Test getting visit by id with a valid id and a idClient == 0")
  @Test
  public void getVisitByIdTest5() {
    int id = goodVisit.getIdRequest();
    goodVisit.setIdClient(0);
    Mockito.when(visitDAO.getVisitById(id)).thenReturn(goodVisit);
    assertEquals(goodVisit, visitUCC.getVisitById(id));
  }


  @DisplayName("Test accept a visit with a invalid id and a non-null schduled date time")
  @Test
  public void acceptVisitTest1() {
    int id = -5;
    assertThrows(BusinessException.class, () -> visitUCC.acceptVisit(id, goodScheduledDateTime));
  }

  @DisplayName("Test accept a visit with a good id and a non-null scheduled date time")
  @Test
  public void acceptVisitTest2() {
    int id = goodVisit.getIdRequest();
    Mockito.when(visitDAO.acceptVisit(id, goodScheduledDateTime)).thenReturn(true);
    assertTrue(visitUCC.acceptVisit(id, goodScheduledDateTime));
  }

  @DisplayName("Test accept a visit whit a good id and a null scheduled date time")
  @Test
  public void acceptVisitTest3() {
    int id = goodVisit.getIdRequest();
    assertThrows(BusinessException.class, () -> visitUCC.acceptVisit(id, badScheduledDateTime));
  }

  @DisplayName("Test accept a visit whit a invalid id and a null scheduled date time")
  @Test
  public void acceptVisitTest4() {
    int id = -5;
    assertThrows(BusinessException.class, () -> visitUCC.acceptVisit(id, badScheduledDateTime));
  }

  @DisplayName("Test accept a visit with a invalid id and a non-null explanatory note")
  @Test
  public void cancelVisitTest1() {
    int id = -5;
    assertThrows(BusinessException.class, () -> visitUCC.cancelVisit(id, goodExplanatoryNote));
  }

  @DisplayName("Test accept a visit with a good id and a non-null explanatory note")
  @Test
  public void cancelVisitTest2() {
    int id = goodVisit.getIdRequest();
    Mockito.when(visitDAO.cancelVisit(id, goodExplanatoryNote)).thenReturn(true);
    assertTrue(visitUCC.cancelVisit(id, goodExplanatoryNote));
  }

  @DisplayName("Test accept a visit whit a good id and a null explanatory note")
  @Test
  public void cancelVisitTest3() {
    int id = goodVisit.getIdRequest();
    assertThrows(BusinessException.class, () -> visitUCC.cancelVisit(id, badExplanatoryNote));
  }

  @DisplayName("Test accept a visit whit a good id and a empty explanatory note")
  @Test
  public void cancelVisitTest4() {
    int id = goodVisit.getIdRequest();
    assertThrows(BusinessException.class, () -> visitUCC.cancelVisit(id, emptyExplanatoryNote));
  }

  @DisplayName("Test accept a visit whit a invalid id and a null explanatory note")
  @Test
  public void cancelVisitTest5() {
    int id = -5;
    assertThrows(BusinessException.class, () -> visitUCC.cancelVisit(id, badExplanatoryNote));
  }

  @DisplayName("Test accept a visit whit a invalid id and a empty explanatory note")
  @Test
  public void cancelVisitTest6() {
    int id = -5;
    assertThrows(BusinessException.class, () -> visitUCC.cancelVisit(id, emptyExplanatoryNote));
  }

  @DisplayName("Test submit a request for visit")
  @Test
  public void submitARequestOfVisitTest1() {
    Mockito.when(visitDAO.submitRequestOfVisit(goodVisit)).thenReturn(1);
    assertTrue(visitUCC.submitRequestOfVisit(goodVisit));
  }

  @DisplayName("Test submit a request for visit without warehouse address")
  @Test
  public void submitARequestOfVisitTest2() {
    Mockito.when(userDAO.getUserFromId(goodVisitWithoutAddress.getIdClient()))
        .thenReturn(goodValidatedUser);
    Mockito.when(visitDAO.submitRequestOfVisit(goodVisitWithoutAddress)).thenReturn(1);
    assertTrue(visitUCC.submitRequestOfVisit(goodVisitWithoutAddress));
  }


}
