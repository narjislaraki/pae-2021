package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitDTO.VisitCondition;
import be.vinci.pae.domain.visit.VisitUCC;
import be.vinci.pae.services.dao.VisitDAO;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class VisitUCCTest {

  private static VisitUCC visitUCC;
  private static VisitDAO visitDAO;

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
  }

  /**
   * Resetting before each test.
   */
  @BeforeEach
  public void reset() {
    Mockito.reset(visitDAO);
  }

  @DisplayName("Test getNotConfirmedVisits with empty list")
  @Test
  public void getNotConfirmedVisitsTest1() {
    List<VisitDTO> list = new ArrayList<>();
    Mockito.when(visitDAO.getNotConfirmedVisits()).thenReturn(list);
    assertEquals(list, visitUCC.getNotConfirmedVisits());
  }

  @DisplayName("Test getNotConfirmedVisits with one visit")
  @Test
  public void getNotConfirmedVisitsTest2() {
    List<VisitDTO> listA = new ArrayList<>();
    listA.add(ObjectDistributor.getNotConfirmedVisitDTO());
    Mockito.when(visitDAO.getNotConfirmedVisits()).thenReturn(listA);
    List<VisitDTO> listB = visitUCC.getNotConfirmedVisits();
    assertAll(() -> assertEquals(listA, listB),
        () -> assertEquals(VisitCondition.EN_ATTENTE, listB.get(0).getVisitCondition()));
  }

  @DisplayName("Test getNotConfirmedVisits with three visit")
  @Test
  public void getNotConfirmedVisitsTest3() {
    List<VisitDTO> listA = new ArrayList<>();
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

}
