package be.vinci.pae.services.dao;

import java.util.List;
import java.util.Map;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.furniture.TypeOfFurnitureDTO;

public interface FurnitureDAO {

  FurnitureDTO getFurnitureById(int id);

  // A REVOIR


  void setFurnitureCondition(Furniture furniture, Condition condition);

  int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser);

  void introduceOption(int optionTerm, int idUser, int idFurniture);

  int cancelOption(String cancellationReason, int idOption);

  void indicateFurnitureUnderOption(int id);

  void indicateSentToWorkshop(int id);

  void indicateDropInStore(int id);

  void indicateOfferedForSale(Furniture furniture, double price);

  void withdrawSale(int id);

  List<FurnitureDTO> getFurnitureList();

  List<FurnitureDTO> getPublicFurnitureList();

  // pas encore pour le livrable
  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);


  String getFurnitureTypeById(int id);

  String getFavouritePhotoById(int id);

  OptionDTO getOption(int id);

  void cancelOvertimedOptions();

  void cancelOvertimedReservations();

  List<TypeOfFurnitureDTO> getTypesOfFurnitureList();
}
