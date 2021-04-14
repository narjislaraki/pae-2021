package be.vinci.pae.domain.sale;

import java.time.LocalDateTime;

public class SaleImpl implements SaleDTO {

  private int id;
  private double sellingPrice;
  private int idFurniture;
  private int idBuyer;
  private LocalDateTime dateOfSale;

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
