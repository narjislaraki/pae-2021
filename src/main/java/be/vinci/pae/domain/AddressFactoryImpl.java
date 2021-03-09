package be.vinci.pae.domain;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressImpl getAddress() {
    return new AddressImpl();
  }

}
