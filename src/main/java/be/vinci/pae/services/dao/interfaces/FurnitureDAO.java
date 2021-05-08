package be.vinci.pae.services.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO.Condition;
import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.domain.interfaces.PhotoDTO;
import be.vinci.pae.domain.interfaces.TypeOfFurnitureDTO;

public interface FurnitureDAO {

  /**
   * Get the furniture whose id is passed as a parameter.
   * 
   * @param id the furniture id
   * @return the furniture or null if there is no furniture with this id.
   */
  FurnitureDTO getFurnitureById(int id);

  /**
   * Changes the condition of the furniture passed in parameter with the condition passed in second
   * parameter.
   * 
   * @param furniture the furniture to change
   * @param condition the new condition for the furniture
   */
  void setFurnitureCondition(FurnitureDTO furniture, Condition condition);

  /**
   * Calculate and return the number of days booked by the user for a furniture.
   * 
   * @param idFurniture the furniture id
   * @param idUser the user id
   * @return the sum of option days
   */
  int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser);

  /**
   * Add an option in the database having the current date, the term of the option passed in first
   * parameter, the state "en cours" and for which the user who introduced it is the one whose id is
   * passed as the third parameter and the furniture is the one whose id is passed as the last
   * parameter.
   * 
   * @param optionTerm the option term
   * @param idUser the user id
   * @param idFurniture the furniture id
   */
  void introduceOption(int optionTerm, int idUser, int idFurniture);

  /**
   * Change the state of the option whose id is passed as the second parameter to "annulée" and
   * update its cancellation reason with the first parameter.
   * 
   * @param cancellationReason the cancellation reason
   * @param idOption the option id
   * @return the id of the furniture on which the option was or -1 if an error has arisen
   */
  int cancelOption(String cancellationReason, int idOption);

  /**
   * Change the condition of the furniture whose id is the one passed in parameter by "sous option".
   * 
   * @param id the furniture id
   */
  void indicateFurnitureUnderOption(int id);

  /**
   * Change the condition of the furniture whose id is the one passed in parameter by "en
   * restauration".
   * 
   * @param id the furniture id
   */
  void indicateSentToWorkshop(int id);

  /**
   * Change the condition of the furniture whose id is the one passed as a parameter by "déposé en
   * magasin", update the "store_deposit" field to true and the "deposit_date" field to the current
   * date.
   * 
   * @param id the furniture id
   */
  void indicateDropInStore(int id);

  /**
   * Change the condition of the furniture whose id is the one passed as a parameter by "en vente"
   * and update the "offering_selling_price" field to the price passed as a parameter.
   * 
   * @param furniture the furniture
   * @param price the offering selling price
   */
  void indicateOfferedForSale(FurnitureDTO furniture, double price);

  /**
   * Change the condition of the furniture whose id is the one passed in parameter by "retiré".
   * 
   * @param id the furniture id
   */
  void withdrawSale(int id);

  /**
   * Get the list of furnitures with a condition other than "en attente" or "refusé".
   * 
   * @return the furniture list
   */
  List<FurnitureDTO> getFurnitureList();

  /**
   * Get the list of furnitures.
   * 
   * @return
   */
  List<FurnitureDTO> getFurnitureListForResearch();

  /**
   * Get the list of furnitures with a condition other than "en vente" or "sous option".
   * 
   * @return the furniture list
   */
  List<FurnitureDTO> getPublicFurnitureList();


  /**
   * Get the list of furnitures having the type whose id is passed in parameter.
   * 
   * @param idType the id type
   * @return the furniture list
   */
  List<FurnitureDTO> getFurnitureListByType(int idType);

  /**
   * Get the list of furnitures having the type whose id is passed in parameter and whose status is
   * "en vente" or "sous option"
   * 
   * @param idType the id type
   * @return the furniture list
   */
  List<FurnitureDTO> getPublicFurnitureListByType(int idType);

  /**
   * Returns the type of the furniture based on its id.
   * 
   * @param id the id of the type
   * @return the furniture type or "" if the id is invalide
   */
  String getFurnitureTypeById(int id);

  /**
   * Returns the favourite photo based on its id.
   * 
   * @param id the id photo
   * @return the favourite photo or "" if the id is invalide
   */
  String getFavouritePhotoById(int id);

  /**
   * Get the option being on the furniture whose id is passed in parameter.
   * 
   * @param id the furniture id
   * @return the option or null if there is no option on the furniture whose id is passed as a
   *         parameter
   */
  OptionDTO getOption(int id);

  /**
   * Update options that are overtimed by setting their status to "annulée" and specifying the
   * reason for the cancellation to "Temps dépassé". Update the condition of the furnitures which
   * were under option and which are therefore no longer.
   */
  void cancelOvertimedOptions();

  /**
   * Get a list of all types of furnitures.
   * 
   * @return the type of furnitures list
   */
  List<TypeOfFurnitureDTO> getTypesOfFurnitureList();

  /**
   * Add a furniture to the database. The following fields will be set: condition = "en vente",
   * description is the description of furniture passed as a parameter, the type of furniture is the
   * type of furniture of furniture passed as a parameter, the visit request id will be the one
   * passed in parameter and the vendor id will be the one passed in parameter.
   * 
   * @param furniture the furniture to add
   * @param idRequestForVisit the id of request for visit
   * @param idSeller the id of the seller
   * @return the id of the furniture added or 0 if an error has arisen
   */
  int addFurniture(FurnitureDTO furniture, int idRequestForVisit, int idSeller);

  /**
   * Add a photo to the database by setting the id of the furniture to which it is linked by the id
   * of the furniture provided as a parameter.
   * 
   * @param photo the photo to add
   * @param idFurniture the id of the furniture
   */
  void addClientPhoto(PhotoDTO photo, int idFurniture);

  /**
   * Get the list of photos of the furniture whose id is the one passed in parameter.
   * 
   * @param idFurniture the furniture id
   * @return the list of photos
   */
  List<PhotoDTO> getFurniturePhotos(int idFurniture);

  /**
   * Process the furniture whose id is the one passed in parameter. If the condition in parameter is
   * "acheté", the method update the condition, the purchase price and the pick up date. Else if the
   * condition in parameter is "refusé", the method update only the condition.
   * 
   * @param id the furniture id
   * @param condition the condition "acheté" or "refusé"
   * @param purchasePrice the purchase price
   * @param pickUpDate the pick up date
   */
  void processFurniture(int id, String condition, double purchasePrice, LocalDateTime pickUpDate);

  /**
   * Update the fields of a furniture whose id is passed as a parameter with the description, the id
   * of the type of cabinet, the sale price and the favorite photo.
   * 
   * @param id the furniture id
   * @param description the new description
   * @param idType the new id of type of furniture
   * @param offeredSellingPrice the new offered selling price
   * @param favouritePhoto the new favorite photo
   * @return true
   */
  boolean edit(int id, String description, int idType, double offeredSellingPrice,
      int favouritePhoto);

  /**
   * Delete the photo whose id is passed as a parameter.
   * 
   * @param id the photo id
   * @return the id of furniture for which the photo exists of -1 if an error has arisen
   */
  int deletePhoto(int id);

  /**
   * Set the "is_visible" field to true of the photo whose id is passed as a parameter.
   * 
   * @param id the photo id
   * @return the id of furniture for which the photo exists of -1 if an error has arisen
   */
  int displayPhoto(int id);

  /**
   * Set the "is_visible" field to false of the photo whose id is passed as a parameter.
   *
   * @param id the photo id
   * @return the id of furniture for which the photo exists of -1 if an error has arisen
   */
  int hidePhoto(int id);

  /**
   * Add the photo passed in parameter in the database after having set its field "id_furniture"
   * with the id of the furniture passed in second parameter.
   * 
   * @param photo the photo to add
   * @param idFurniture the furniture id
   * @return the id of furniture for which the photo exists of -1 if an error has arisen
   */
  int addAdminPhoto(PhotoDTO photo, int idFurniture);

  /**
   * Get a list of furniture with the condition "en vente", "sous option" or "vendu". This method
   * will take exactly the number of furniture equal to the limit passed in parameter.
   * 
   * @param limit the number of furniture we want
   * @return the list of furnitures
   */
  List<FurnitureDTO> getSliderFurnitureList(int limit);

  /**
   * Get a list of furniture with the condition "en vente", "sous option" or "vendu" and having the
   * type whose id is passed in parameter. This method will take exactly the number of furniture
   * equal to the limit passed in parameter.
   * 
   * @param limit the number of furniture we want
   * @param idType the type id
   * @return the list of furnitures
   */
  List<FurnitureDTO> getSliderFurnitureListByType(int limit, int idType);

  /**
   * Get a list of furniture with a condition other than "refusé" and whose seller is the one whose
   * id is passed in parameter.
   * 
   * @param id the seller id
   * @return the list of furnitures
   */
  List<FurnitureDTO> getTransactionsSeller(int id);

}
