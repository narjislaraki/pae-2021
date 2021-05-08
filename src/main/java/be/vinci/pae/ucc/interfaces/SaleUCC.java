package be.vinci.pae.ucc.interfaces;

import java.util.List;

import be.vinci.pae.domain.interfaces.SaleDTO;

public interface SaleUCC {

  List<SaleDTO> getSalesList();

  boolean addSale(SaleDTO sale);
}
