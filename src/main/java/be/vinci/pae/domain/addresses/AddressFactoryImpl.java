package be.vinci.pae.domain.addresses;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressImpl getAddress() {
    return new AddressImpl();
  }

}
