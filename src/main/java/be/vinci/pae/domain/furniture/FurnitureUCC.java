package be.vinci.pae.domain.furniture;

import java.util.List;
import be.vinci.pae.domain.edition.EditionDTO;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.visit.PhotoDTO;

public interface FurnitureUCC {

  // settingPurchasePrice(double price);
  // settingSellingPrice(double price);
  // indicateTheCollectionOfTheFurniture();
  // indicateThatTheFurnitureIsDelivered();
  // indicate
  // indiquer qu'un meuble est deposé + livré + emporté
  // + fixer un prix d'achat + indiquer un prix de vente

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

  boolean addSale(SaleDTO sale);

  List<PhotoDTO> getFurniturePhotos(int idFurniture, UserDTO user);

  boolean processVisit(List<FurnitureDTO> listFurnitures);

  boolean edit(EditionDTO edition);

  List<FurnitureDTO> getSliderFurnitureList(int limit);

  List<FurnitureDTO> getSliderFurnitureListByType(int limit, int idType);

}
