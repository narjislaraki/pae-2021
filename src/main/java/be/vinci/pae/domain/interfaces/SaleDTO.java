package be.vinci.pae.domain.interfaces;

import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import be.vinci.pae.domain.SaleImpl;

@JsonDeserialize(as = SaleImpl.class)
public interface SaleDTO {

  int getId();

  void setId(int id);

  int getIdFurniture();

  void setIdFurniture(int idFurniture);

  int getIdBuyer();

  void setIdBuyer(int idBuyer);

  LocalDateTime getDateOfSale();

  void setDateOfSale(LocalDateTime dateOfSale);

  double getSellingPrice();

  void setSellingPrice(double sellingPrice);
  
  FurnitureDTO getFurniture();
  
  void setFurniture(FurnitureDTO furniture);

}
