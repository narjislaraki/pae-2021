package be.vinci.pae.domain.furniture;

import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.addresses.Address;

public interface FurnitureUCC {

  // settingPurchasePrice(double price);
  // settingSellingPrice(double price);
  // indicateTheCollectionOfTheFurniture();
  // indicateThatTheFurnitureIsDelivered();
  // indicate
  // indiquer qu'un meuble est deposé + livré + emporté
  // + fixer un prix d'achat + indiquer un prix de vente
  void introduceOption(int numberOfDay);

  void cancelOption(String cancellationReason);

  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);
}
