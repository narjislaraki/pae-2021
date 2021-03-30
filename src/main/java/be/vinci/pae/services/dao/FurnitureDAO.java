package be.vinci.pae.services.dao;

import java.util.List;
import java.util.Map;
import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.OptionDTO;

public interface FurnitureDAO {

  FurnitureDTO getFurnitureById(int id);

  // A REVOIR


  void setCondition(Furniture furniture, Condition condition);

  int getNumberOfOptions(int idFurniture, int idUser);

  void introduceOption(int optionTerm, int idUser, int idFurniture);

  int cancelOption(String cancellationReason, int idOption);

  void indicateUnderOption(int id);

  void indicateSentToWorkshop(int id);

  void indicateDropInStore(int id);

  void indicateOfferedForSale(Furniture furniture, double price);

  void withdrawSale(int id);

  List<FurnitureDTO> getFurnitureList();

  // pas encore pour le livrable
  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);


  String getTypeById(int id);

  String getFavouritePhotoById(int id);

  OptionDTO getOption(int id);
}
