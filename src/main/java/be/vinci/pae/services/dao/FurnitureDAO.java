package be.vinci.pae.services.dao;

import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.furniture.Furniture;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;

public interface FurnitureDAO {

  FurnitureDTO getFurnitureById(int id);

  // A REVOIR
  int getNumberOfReservation(int idFurniture, int idUser);

  void setCondition(Furniture furniture, Condition condition);

  void introduceOption(int optionTerm, int idUser, int idFurniture);

  void cancelOption(String cancellationReason, int idOption);

  void indicateSentToWorkshop(int id);

  void indicateDropOfStore(int id);

  void indicateOfferedForSale(Furniture furniture, double price);

  void withdrawSale(int id);

  List<FurnitureDTO> SeeFurnitureList();

  // pas encore pour le livrable
  void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures);


}
