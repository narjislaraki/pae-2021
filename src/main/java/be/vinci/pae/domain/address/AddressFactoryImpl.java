package be.vinci.pae.domain.address;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressImpl getAddress() {
    return new AddressImpl();
  }

}
