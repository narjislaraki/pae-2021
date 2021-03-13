package be.vinci.pae.domain.furniture;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonView;

import be.vinci.pae.views.Views;

public class FurnitureImpl implements Furniture {

  @JsonView(Views.Public.class)
  private int id;

  // on peut faire comme pour adresse get avec un int
  @JsonView(Views.Public.class)
  private int type;
  @JsonView(Views.Internal.class)
  private int visitRequest;
  @JsonView(Views.Internal.class)
  private int seller;
  // A revoir
  @JsonView(Views.Internal.class)
  private String state;
  @JsonView(Views.Public.class)
  private String description;
  @JsonView(Views.Internal.class)
  private double buyingPrice;
  @JsonView(Views.Internal.class)
  private LocalDateTime pickUpDatePurchase;
  @JsonView(Views.Internal.class)
  private boolean storeDeposit;
  @JsonView(Views.Internal.class)
  private LocalDateTime storeDepositDate;
  @JsonView(Views.Public.class)
  private double offeredSellingPrice;
  @JsonView(Views.Public.class)
  private int favoritePicture;

}
