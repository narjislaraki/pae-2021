package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;

public interface FurnitureDTO {

  enum Condition {
    EN_ATTENTE("proposé"), VALIDE("acheté"), REFUSE("refusé"), EN_RESTAURATION(
        "en restauration"), DEPOSE_EN_MAGASIN("deposé en magasin"), EN_VENTE(
            "en vente"), SOUS_OPTION("sous option"), VENDU("vendu"), EMPORTE(
                "emporté"), LIVRE("livré"), RESERVE("réservé"), RETIRER("retiré de la vente");

    private String condition;

    Condition(String condition) {
      this.condition = condition;
    }

    public String getString() {
      return this.condition;
    }
  }

  int getId();

  void setId(int id);

  int getType();

  void setType(int type);

  int getRequestForVisit();

  void setRequestForVisit(int requestForVisit);

  int getSeller();

  void setSeller(int seller);

  Condition getCondition();

  void setCondition(Condition condition);

  String getDescription();

  void setDescription(String description);

  double getPurchasePrice();

  void setPurchasePrice(double purchasePrice);

  LocalDateTime getPickUpDate();

  void setPickUpDate(LocalDateTime pickUpDate);

  boolean isStoreDeposit();

  void setStoreDeposit(boolean storeDeposit);

  LocalDateTime getDepositDate();

  void setDepositDate(LocalDateTime depositDate);

  double getOfferedSellingPrice();

  void setOfferedSellingPrice(double offeredSellingPrice);

  int getFavouritePhoto();

  void setFavouritePhoto(int favouritePhoto);


}
