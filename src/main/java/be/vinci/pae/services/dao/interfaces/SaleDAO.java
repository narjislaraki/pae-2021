package be.vinci.pae.services.dao.interfaces;

import java.util.List;
import be.vinci.pae.domain.interfaces.SaleDTO;

public interface SaleDAO {

  /**
   * Add the sale passed as a parameter in the database after updating its idBuyer to null if it is
   * an anonymous sale
   * 
   * @param sale the sale to add
   */
  void addSale(SaleDTO sale);

  /**
   * Get a sales list whose buyer's id is the one passed in parameter.
   * 
   * @param id the buyer id
   * @return the list of sales
   */
  List<SaleDTO> getTransactionsBuyer(int id);

  /**
   * Get a list of all sales.
   * 
   * @return a list of all sales
   */
  List<SaleDTO> getSalesList();


}
