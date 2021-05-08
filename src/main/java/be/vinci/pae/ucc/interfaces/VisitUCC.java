package be.vinci.pae.ucc.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.VisitDTO;

public interface VisitUCC {

  /**
   * Get the list of unconfirm visits.
   * 
   * @return the list of unconfirm visits
   */
  List<VisitDTO> getNotConfirmedVisits();

  /**
   * Get the list of visits waiting to be process.
   * 
   * @return the list of visits
   */
  List<VisitDTO> getVisitsToBeProcessed();

  /**
   * Get the list of visit for one client.
   * 
   * @param idClient the id of the client
   * @return the list of visits
   */
  List<VisitDTO> getVisitsListForAClient(int idClient);

  /**
   * Send a request of visit which will wait to be processed. Check if the address is the user
   * address or a new one.
   * 
   * @param visit the visit create
   * @return true if the visit has been submited
   */
  boolean submitRequestOfVisit(VisitDTO visit);

  /**
   * Accept a visit and change the condition and set the scheduled date time.
   * 
   * @param idVisit the id of the visit
   * @param scheduledDateTime the moment of the visit
   * @return true if the visit has been accepted
   */
  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  /**
   * Cancel the request of visit and set a explanatory note for the user.
   * 
   * @param idVisit the id of the visit
   * @param explanatoryNote the explanation of the cancalation
   * @return true if the visit has been canceled
   */
  boolean cancelVisit(int idVisit, String explanatoryNote);

  /**
   * Get a visit by her id.
   * 
   * @param id the id of the visit
   * @return the visit
   */
  VisitDTO getVisitById(int id);

  /**
   * Get all the furniture introduce for the id visit specified.
   * 
   * @param idVisit the id of the visit
   * @return a list of furniture
   */
  List<FurnitureDTO> getListFurnituresForOneVisit(int idVisit);


}
