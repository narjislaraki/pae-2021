package be.vinci.pae.domain.sale;

import java.time.LocalDateTime;
import be.vinci.pae.domain.furniture.FurnitureDTO;

public class SaleImpl implements SaleDTO {

  private int id;
  private double sellingPrice;
  private int idFurniture;
  private int idBuyer;
  private LocalDateTime dateOfSale;
  private FurnitureDTO furniture;

  @Override
  public double getSellingPrice() {
    return sellingPrice;
  }

  @Override
  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int getIdFurniture() {
    return idFurniture;
  }

  @Override
  public void setIdFurniture(int idFurniture) {
    this.idFurniture = idFurniture;
  }

  @Override
  public int getIdBuyer() {
    return idBuyer;
  }


  public FurnitureDTO getFurniture() {
    return furniture;
  }

  public void setFurniture(FurnitureDTO furniture) {
    this.furniture = furniture;
  }

  @Override
  public void setIdBuyer(int idBuyer) {
    this.idBuyer = idBuyer;
  }

  @Override
  public LocalDateTime getDateOfSale() {
    return dateOfSale;
  }

  @Override
  public void setDateOfSale(LocalDateTime dateOfSale) {
    this.dateOfSale = dateOfSale;
  }



}
