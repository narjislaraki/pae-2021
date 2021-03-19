package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

import be.vinci.pae.domain.addresses.Address;
import be.vinci.pae.views.Views;

public class FurnitureImpl implements Furniture {

  @JsonView(Views.Public.class)
  private int id;

  // on peut faire comme pour adresse get avec un int
  @JsonView(Views.Public.class)
  private int type;
  @JsonView(Views.Internal.class)
  private int RequestForVisit;
  @JsonView(Views.Internal.class)
  private int seller;
  @JsonView(Views.Internal.class)
  private Condition condition;
  @JsonView(Views.Public.class)
  private String description;
  @JsonView(Views.Internal.class)
  private double purchasePrice;
  @JsonView(Views.Internal.class)
  private LocalDateTime pickUpDate;
  @JsonView(Views.Internal.class)
  private boolean storeDeposit;
  @JsonView(Views.Internal.class)
  private LocalDateTime DepositDate;
  @JsonView(Views.Public.class)
  private double offeredSellingPrice;
  @JsonView(Views.Internal.class)
  private int favouritePhoto;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getRequestForVisit() {
    return RequestForVisit;
  }

  public void setRequestForVisit(int requestForVisit) {
    RequestForVisit = requestForVisit;
  }

  public int getSeller() {
    return seller;
  }

  public void setSeller(int seller) {
    this.seller = seller;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
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
    return DepositDate;
  }

  public void setDepositDate(LocalDateTime depositDate) {
    DepositDate = depositDate;
  }

  public double getOfferedSellingPrice() {
    return offeredSellingPrice;
  }

  public void setOfferedSellingPrice(double offeredSellingPrice) {
    this.offeredSellingPrice = offeredSellingPrice;
  }

  public int getFavouritePhoto() {
    return favouritePhoto;
  }

  public void setFavouritePhoto(int favouritePhoto) {
    this.favouritePhoto = favouritePhoto;
  }

  public void introduceOption(int numberOfDay) {

  }

  public void cancelOption(String cancellationReason) {

  }

  public void introduceRequestForVisite(String timeSlot, Address address,
      Map<Integer, List<String>> furnitures) {

  }



}
