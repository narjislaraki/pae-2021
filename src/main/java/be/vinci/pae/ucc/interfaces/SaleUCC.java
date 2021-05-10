package be.vinci.pae.ucc.interfaces;

import java.util.List;

import be.vinci.pae.domain.interfaces.SaleDTO;

public interface SaleUCC {

  /**
   * Get the list of furniture sold.
   * 
   * @return the list of sale
   */
  List<SaleDTO> getSalesList();

  /**
   * Change the condition of a furniture and add a sale.
   * 
   * @param sale the sale to be add
   * @return true if the sale as been done, false if the furniture is already sold
   */
  boolean addSale(SaleDTO sale);
}
