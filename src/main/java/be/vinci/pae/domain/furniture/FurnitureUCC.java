package be.vinci.pae.domain.furniture;

import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.user.UserDTO;

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

  List<FurnitureDTO> getFurnitureList();

  // pas encore pour le livrable
  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);

  FurnitureDTO getFurnitureById(int id);

  OptionDTO getOption(int idFurniture);

  int getNbOfDay(int idFurniture, int idUser);
}
