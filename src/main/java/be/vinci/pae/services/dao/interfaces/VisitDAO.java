package be.vinci.pae.services.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.VisitDTO;

public interface VisitDAO {

  /**
   * Get a list of requests for visits whose "condition" field is "en attente".
   * 
   * @return the list of not confirmed visits
   */
  List<VisitDTO> getNotConfirmedVisits();

  /**
   * Get a list of requests for visits whose "condition" field is "acceptée".
   * 
   * @return the list of confirmed visits
   */
  List<VisitDTO> getVisitsToBeProcessed();

  /**
   * Get the list of visit requests introduced by the client whose id is the one passed in
   * parameter.
   * 
   * @param idClient the client
   * @return the list of visits introduced by the client
   */
  List<VisitDTO> getVisitsListForAClient(int idClient);

  /**
   * Add the visit request passed as a parameter after setting the "condition" field to "en attente"
   * and the date of the request to the current date.
   * 
   * @param visit the visit to add
   * @return the visit id
   */
  int submitRequestOfVisit(VisitDTO visit);

  /**
   * Update the visit request whose id is passed as a parameter by setting the "condition" fields to
   * "accepted" and "scheduled_date_time" to the scheduled date time passed as a parameter.
   * 
   * @param idVisit the visit id to accept
   * @param scheduledDateTime the scheduled date time
   * @return true if the executeUpdate method returns 1, false otherwise
   */
  boolean acceptVisit(int idVisit, LocalDateTime scheduledDateTime);

  /**
   * Update the visit request whose id is passed as a parameter by setting the "condition" fields to
   * "annulée" and "explanatory_note" to the explanatory note passed as a parameter.
   * 
   * @param idVisit the visit id to cancel
   * @param explanatoryNote the explanatory note
   * @return true if the executeUpdate method returns 1, false otherwise
   */
  boolean cancelVisit(int idVisit, String explanatoryNote);

  /**
   * Searching through the database for the request for visit, using his id.
   * 
   * @param idVisit the request for visit id
   * @return the request for visit
   */
  VisitDTO getVisitById(int idVisit);

  /**
   * Get the list of furnitures which has the "request_visit" field equal to the id passed in
   * parameter.
   * 
   * @param idVisit the visit id
   * @return the list of furnitures for a certain visit
   */
  List<FurnitureDTO> getListFurnituresForOnVisit(int idVisit);


}
