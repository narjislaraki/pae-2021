package be.vinci.pae.domain.furniture;

import java.util.List;
import java.util.Map;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.services.dao.FurnitureDAO;
import jakarta.inject.Inject;

public class FurnitureUCCImpl implements FurnitureUCC {

  @Inject
  private FurnitureDAO furnitureDao;

  @Override
  public void introduceOption(int optionTerm, int idUser, int idFurniture) {
    // TODO Auto-generated method stub

  }

  @Override
  public void cancelOption(String cancellationReason, int idOption) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateSentToWorkshop(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateDropOfStore(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void indicateOfferedForSale(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void withdrawSale(int id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<FurnitureDTO> SeeFurnitureList() {
    // TODO Auto-generated method stub

    return null;
  }

}
