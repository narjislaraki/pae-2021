package be.vinci.pae.ucc;

import java.util.List;

import be.vinci.pae.domain.interfaces.EditionDTO;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.OptionDTO;
import be.vinci.pae.domain.interfaces.PhotoDTO;
import be.vinci.pae.domain.interfaces.TypeOfFurnitureDTO;
import be.vinci.pae.domain.interfaces.UserDTO;

public interface FurnitureUCC {

  void indicateSentToWorkshop(int id);

  void indicateDropOfStore(int id);

  void indicateOfferedForSale(int id, double price);

  void withdrawSale(int id);

  void introduceOption(int optionTerm, int idUser, int idFurniture);

  void cancelOption(String cancellationReason, int idOption, UserDTO user);

  List<FurnitureDTO> getFurnitureList(UserDTO user);

  List<FurnitureDTO> getFurnitureListForResearch();

  List<FurnitureDTO> getFurnitureListByType(UserDTO user, int idType);

  FurnitureDTO getFurnitureById(int id);

  FurnitureDTO getFurnitureWithPhotosById(int id);

  OptionDTO getOption(int idFurniture);

  int getSumOfOptionDaysForAUserAboutAFurniture(int idFurniture, int idUser);

  void cancelOvertimedOptions();

  List<TypeOfFurnitureDTO> getTypesOfFurnitureList();

  List<PhotoDTO> getFurniturePhotos(int idFurniture, UserDTO user);

  boolean processVisit(List<FurnitureDTO> listFurnitures);

  boolean edit(EditionDTO edition);

  List<FurnitureDTO> getSliderFurnitureList(int limit);

  List<FurnitureDTO> getSliderFurnitureListByType(int limit, int idType);

}
