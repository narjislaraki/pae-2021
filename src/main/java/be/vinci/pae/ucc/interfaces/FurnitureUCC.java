package be.vinci.pae.ucc.interfaces;

import java.util.List;

import be.vinci.pae.domain.interfaces.EditionDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.domain.interfaces.PhotoDTO;
import be.vinci.pae.domain.interfaces.TypeOfFurnitureDTO;
import be.vinci.pae.domain.interfaces.UserDTO;

public interface FurnitureUCC {

  /**
   * Changes the state of the furniture given by its id to "en restauration".
   * 
   * @param id the furniture id
   */
  void indicateSentToWorkshop(int id);

  /**
   * Changes the state of the furniture given by its id to "déposé en magasin".
   * 
   * @param id the furniture id
   */
  void indicateDropOfStore(int id);

  /**
   * Changes the state of the furniture given by its id to "en vente" and set the offered selling
   * price.
   * 
   * @param id the furniture id
   * @param price the offered price
   */
  void indicateOfferedForSale(int id, double price);

  /**
   * Withdraw a furniture and set its state to a withdrawn state.
   * 
   * @param id the furniture id
   */
  void withdrawSale(int id);

  /**
   * Introduce an option related to a furniture and a user.
   * 
   * @param optionTerm the time of the option
   * @param idUser the user id
   * @param idFurniture the furniture id
   */
  void introduceOption(int optionTerm, int idUser, int idFurniture);

  /**
   * Cancel an option.
   * 
   * @param cancellationReason the reason of the cancellation
   * @param idOption the option id
   * @param user the user wanting to cancel that option
   */
  void cancelOption(String cancellationReason, int idOption, UserDTO user);

  /**
   * Get a list of furnitures. The furniture's attributes are filtered depending on user role. The
   * view "Public" is use for the unlogged user and the client and antiquaire. The view "Private" is
   * use for the administrator.
   * 
   * @param user the user
   * @return a list of furnitures
   */
  List<FurnitureDTO> getFurnitureList(UserDTO user);

  /**
   * Get a list of furnitures for research purposes.Filtered depending the user's role.
   * 
   * @return a list of furniture
   */
  List<FurnitureDTO> getFurnitureListForResearch();

  /**
   * Get a list of furnitures of a certain type. Filtered depending the user's role.
   * 
   * @param user the user
   * @param idType the id of the furniture's type
   * 
   * @return a list of furniture
   */
  List<FurnitureDTO> getFurnitureListByType(UserDTO user, int idType);

  /**
   * Get the furniture associate with the id.
   * 
   * @param id the id of the furniture
   * @return the furniture found, otherwise an exception
   */
  FurnitureDTO getFurnitureById(int id);

  /**
   * Get a furniture by their id and get it with their photos and seller.
   * 
   * @param id of the furniture
   * @return the furniture
   */
  FurnitureDTO getFurnitureWithPhotosById(int id);

  /**
   * Get an option associate to the id of a furniture.
   * 
   * @param idFurniture the id of the furniture
   * @return the option
   */
  OptionDTO getOption(int idFurniture);

  /**
   * Get the amount of days already taken by a user for a furniture.
   * 
   * @param idFurniture the id of a furniture
   * @param idUser the id of the user
   * @return the sum of the option for a user on a furniture
   */
  int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser);

  /**
   * Cancel an option when the time requested reached his term.
   */
  void cancelOvertimedOptions();

  /**
   * Get the list of furniture type.
   * 
   * @return the list of furniture type
   */
  List<TypeOfFurnitureDTO> getTypesOfFurnitureList();

  /**
   * Get the photos of the furniture specified, and filtered it if the user isn't an admin.
   * 
   * @param idFurniture the id of the furniture
   * @param user the user
   * @return a list of photo
   */
  List<PhotoDTO> getFurniturePhotos(int idFurniture, UserDTO user);

  /**
   * Process a pending visit.
   * 
   * @param listFurnitures the list of furnitures to be process
   * @return true when the visit has been processed
   */
  boolean processVisit(List<FurnitureDTO> listFurnitures);

  /**
   * Edit a furniture with the data entered in the "edition" paramater. When one photos is link with
   * another furniture an error is thrown.
   * 
   * @param edition the data to be changed
   * @return true when the furniture has been edited
   */
  boolean edit(EditionDTO edition);

  /**
   * Get a list of furniture not exceeding the limit length.
   * 
   * @param limit the amount of furniture max
   * @return a list of furniture
   */
  List<FurnitureDTO> getSliderFurnitureList(int limit);

  /**
   * Get a list of furniture not exceeding the limit length filtered by type.
   * 
   * @param limit the amount of furniture max
   * @param idType the id of a furniture type
   * @return a list of furniture for a type
   */
  List<FurnitureDTO> getSliderFurnitureListByType(int limit, int idType);

}
