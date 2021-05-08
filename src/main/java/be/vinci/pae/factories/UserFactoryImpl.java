package be.vinci.pae.factories;

import be.vinci.pae.domain.UserImpl;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.factories.interfaces.UserFactory;

public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
