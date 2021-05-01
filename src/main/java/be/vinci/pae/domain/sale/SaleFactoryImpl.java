package be.vinci.pae.domain.sale;

public class SaleFactoryImpl implements SaleFactory {

  @Override
  public SaleDTO getSaleDTO() {
    return new SaleImpl();
  }

}
