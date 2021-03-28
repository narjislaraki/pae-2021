package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import be.vinci.pae.domain.user.UserDTO;

public interface FurnitureDTO {

  enum Condition {
    EN_ATTENTE("proposé"), ACHETE("acheté"), REFUSE("refusé"), EN_RESTAURATION(
        "en restauration"), DEPOSE_EN_MAGASIN("déposé en magasin"), EN_VENTE(
            "en vente"), SOUS_OPTION("sous option"), VENDU("vendu"), EMPORTE(
                "emporté"), LIVRE("livré"), RESERVE("réservé"), RETIRE("retiré de la vente");

    private String condition;

    Condition(String condition) {
      this.condition = condition;
    }

    public String toString() {
      return this.condition;
    }
  }

  public int getId();

  public void setId(int id);

  public int getTypeId();

  public void setTypeId(int typeId);

  public String getType();

  public void setType(String type);

  public int getRequestForVisitId();

  public void setRequestForVisitId(int requestForVisitId);

  public int getSellerId();

  public void setSellerId(int sellerId);

  public UserDTO getSeller();

  public void setSeller(UserDTO seller);

  public Condition getCondition();

  public void setCondition(String condition);

  public String getDescription();

  public void setDescription(String description);

  public double getPurchasePrice();

  public void setPurchasePrice(double purchasePrice);

  public LocalDateTime getPickUpDate();

  public void setPickUpDate(LocalDateTime pickUpDate);

  public boolean isStoreDeposit();

  public void setStoreDeposit(boolean storeDeposit);

  public LocalDateTime getDepositDate();

  public void setDepositDate(LocalDateTime depositDate);

  public double getOfferedSellingPrice();

  public void setOfferedSellingPrice(double offeredSellingPrice);

  public int getFavouritePhotoId();

  public void setFavouritePhotoId(int favouritePhotoId);

  public byte[] getFavouritePhoto();

  public void setFavouritePhoto(byte[] favouritePhoto);

  public void setFavouritePhoto(String encodedPhoto);

  public String toString();

}
