package be.vinci.pae.services.dao.interfaces;

import java.util.List;

import be.vinci.pae.domain.interfaces.SaleDTO;

public interface SaleDAO {

  void addSale(SaleDTO sale);


  List<SaleDTO> getTransactionsBuyer(int id);


  List<SaleDTO> getSalesList();


}
