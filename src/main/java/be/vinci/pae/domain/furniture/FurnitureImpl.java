package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonView;

import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.visit.PhotoDTO;
import be.vinci.pae.exceptions.BusinessException;
import be.vinci.pae.views.Views;

public class FurnitureImpl implements FurnitureDTO {

  @JsonView(Views.Public.class)
  private int id;

  // on peut faire comme pour adresse get avec un int
  @JsonView(Views.Internal.class)
  private int typeId;
  @JsonView(Views.Public.class)
  private String type;
  @JsonView(Views.Internal.class)
  private int requestForVisitId;
  @JsonView(Views.Internal.class)
  private int sellerId;
  @JsonView(Views.Private.class)
  private UserDTO seller;
  @JsonView(Views.Public.class)
  private Condition condition;
  @JsonView(Views.Public.class)
  private String description;
  @JsonView(Views.Private.class)
  private double purchasePrice;
  @JsonView(Views.Private.class)
  private LocalDateTime pickUpDate;
  @JsonView(Views.Internal.class)
  private boolean storeDeposit;
  @JsonView(Views.Private.class)
  private LocalDateTime depositDate;
  @JsonView(Views.Public.class)
  private double offeredSellingPrice;
  @JsonView(Views.Public.class)
  private int favouritePhotoId;
  @JsonView(Views.Public.class)
  private String favouritePhoto;
  @JsonView(Views.Public.class)
  private ArrayList<PhotoDTO> listPhotos;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getRequestForVisitId() {
    return requestForVisitId;
  }

  public void setRequestForVisitId(int requestForVisitId) {
    this.requestForVisitId = requestForVisitId;
  }

  public int getSellerId() {
    return sellerId;
  }

  public void setSellerId(int sellerId) {
    this.sellerId = sellerId;
  }

  public UserDTO getSeller() {
    return seller;
  }

  public void setSeller(UserDTO seller) {
    this.seller = seller;
  }

  public Condition getCondition() {
    return condition;
  }

  /**
   * Set condition of the furniture.
   * 
   * @param condition the condition
   */
  public void setCondition(String condition) {

    switch (condition.toLowerCase()) {
      case "proposé":
        this.condition = Condition.EN_ATTENTE;
        break;
      case "acheté":
        this.condition = Condition.ACHETE;
        break;
      case "refusé":
        this.condition = Condition.REFUSE;
        break;
      case "en restauration":
        this.condition = Condition.EN_RESTAURATION;
        break;
      case "déposé en magasin":
        this.condition = Condition.DEPOSE_EN_MAGASIN;
        break;
      case "en vente":
        this.condition = Condition.EN_VENTE;
        break;
      case "sous option":
        this.condition = Condition.SOUS_OPTION;
        break;
      case "vendu":
        this.condition = Condition.VENDU;
        break;
      case "réservé":
        this.condition = Condition.RESERVE;
        break;
      case "retiré de la vente":
        this.condition = Condition.RETIRE;
        break;
      default:
        throw new BusinessException("Set condition failed");
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(double purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public LocalDateTime getPickUpDate() {
    return pickUpDate;
  }

  public void setPickUpDate(LocalDateTime pickUpDate) {
    this.pickUpDate = pickUpDate;
  }

  public boolean isStoreDeposit() {
    return storeDeposit;
  }

  public void setStoreDeposit(boolean storeDeposit) {
    this.storeDeposit = storeDeposit;
  }

  public LocalDateTime getDepositDate() {
    return depositDate;
  }

  public void setDepositDate(LocalDateTime depositDate) {
    this.depositDate = depositDate;
  }

  public double getOfferedSellingPrice() {
    return offeredSellingPrice;
  }

  public void setOfferedSellingPrice(double offeredSellingPrice) {
    this.offeredSellingPrice = offeredSellingPrice;
  }

  public int getFavouritePhotoId() {
    return favouritePhotoId;
  }

  public void setFavouritePhotoId(int favouritePhotoId) {
    this.favouritePhotoId = favouritePhotoId;
  }

  public String getFavouritePhoto() {
    return favouritePhoto;
  }

  public void setFavouritePhoto(String favouritePhoto) {
    this.favouritePhoto = favouritePhoto;
  }

  public ArrayList<PhotoDTO> getListPhotos() {
    return this.listPhotos;
  }

  public void setListPhotos(ArrayList<PhotoDTO> listPhotos) {
    this.listPhotos = listPhotos;
  }

  @Override
  public String toString() {
    return "FurnitureImpl [id=" + id + ", typeId=" + typeId + ", type=" + type
        + ", requestForVisitId=" + requestForVisitId + ", sellerId=" + sellerId + ", seller="
        + seller + ", condition=" + condition + ", description=" + description + ", purchasePrice="
        + purchasePrice + ", pickUpDate=" + pickUpDate + ", storeDeposit=" + storeDeposit
        + ", depositDate=" + depositDate + ", offeredSellingPrice=" + offeredSellingPrice
        + ", favouritePhotoId=" + favouritePhotoId + ", favouritePhoto=" + favouritePhoto + "]";
  }

}
