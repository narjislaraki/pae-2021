package be.vinci.pae.factories;

import be.vinci.pae.domain.AddressImpl;
import be.vinci.pae.factories.interfaces.AddressFactory;

public class AddressFactoryImpl implements AddressFactory {

  @Override
  public AddressImpl getAddress() {
    return new AddressImpl();
  }

}
