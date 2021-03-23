package be.vinci.pae.services.dao;

import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.addresses.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;

public interface FurnitureDAO {

  void setCondition(Furniture furniture, String condition);

  void introduceOption(int numberOfDay);

  void cancelOption(String cancellationReason);

  void indicateSentToWorkshop(int id);

  void indicateDropOfStore(int id);

  void indicateOfferedForSale(int id);

  void withdrawSale(int id);

  List<FurnitureDTO> SeeFurnitureList();

  // pas encore pour le livrable
  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);


}
