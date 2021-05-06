package be.vinci.pae.services.dao;

import java.time.LocalDateTime;
import java.util.List;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.furniture.TypeOfFurnitureDTO;
import be.vinci.pae.domain.visit.PhotoDTO;

public interface FurnitureDAO {

  FurnitureDTO getFurnitureById(int id);

  // A REVOIR

  void setFurnitureCondition(FurnitureDTO furniture, Condition condition);

  int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser);

  void introduceOption(int optionTerm, int idUser, int idFurniture);

  int cancelOption(String cancellationReason, int idOption);

  void indicateFurnitureUnderOption(int id);

  void indicateSentToWorkshop(int id);

  void indicateDropInStore(int id);

  void indicateOfferedForSale(FurnitureDTO furniture, double price);

  void withdrawSale(int id);

  List<FurnitureDTO> getFurnitureList();

  List<FurnitureDTO> getPublicFurnitureList();

  List<FurnitureDTO> getFurnitureListForResearch();

  List<FurnitureDTO> getFurnitureListByType(int idType);

  List<FurnitureDTO> getPublicFurnitureListByType(int idType);

  String getFurnitureTypeById(int id);

  String getFavouritePhotoById(int id);

  OptionDTO getOption(int id);

  void cancelOvertimedOptions();

  List<TypeOfFurnitureDTO> getTypesOfFurnitureList();

  int addFurniture(FurnitureDTO furniture, int idRequestForVisit, int idSeller);

  void addClientPhoto(PhotoDTO photo, int idFurniture);

  List<PhotoDTO> getFurniturePhotos(int idFurniture);

  void processFurniture(int id, String condition, double purchasePrice, LocalDateTime pickUpDate);

  boolean edit(int id, String description, int idType, double offeredSellingPrice,
      int favouritePhoto);

  int deletePhoto(int id);

  int displayPhoto(int id);

  int hidePhoto(int id);

  int addAdminPhoto(PhotoDTO photo, int idFurniture);

  List<FurnitureDTO> getSliderFurnitureList(int limit);

}
