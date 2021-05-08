package be.vinci.pae.factories;

import be.vinci.pae.domain.SaleImpl;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.factories.interfaces.SaleFactory;

public class SaleFactoryImpl implements SaleFactory {

  @Override
  public SaleDTO getSaleDTO() {
    return new SaleImpl();
  }

}
