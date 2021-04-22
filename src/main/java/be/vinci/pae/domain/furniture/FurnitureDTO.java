package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.visit.PhotoDTO;

@JsonDeserialize(as = FurnitureImpl.class)
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

  int getId();

  void setId(int id);

  int getTypeId();

  void setTypeId(int typeId);

  String getType();

  void setType(String type);

  int getRequestForVisitId();

  void setRequestForVisitId(int requestForVisitId);

  int getSellerId();

  void setSellerId(int sellerId);

  UserDTO getSeller();

  void setSeller(UserDTO seller);

  Condition getCondition();

  void setCondition(String condition);

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

  int getFavouritePhotoId();

  void setFavouritePhotoId(int favouritePhotoId);

  String getFavouritePhoto();

  void setFavouritePhoto(String favouritePhoto);

  ArrayList<PhotoDTO> getListPhotos();

  void setListPhotos(ArrayList<PhotoDTO> listPhotos);

  String toString();

}
