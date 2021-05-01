package be.vinci.pae.services.dao;

import java.util.List;
import be.vinci.pae.domain.sale.SaleDTO;

public interface SaleDAO {

  void addSale(SaleDTO sale);

  List<SaleDTO> getSalesList();

}
