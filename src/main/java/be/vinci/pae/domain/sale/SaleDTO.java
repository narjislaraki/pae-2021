package be.vinci.pae.domain.sale;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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

}