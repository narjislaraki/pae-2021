package be.vinci.pae.domain;

public class UserFactoryImpl implements UserFactory {

  @Override
  public UserDTO getUserDTO() {
    return new UserImpl();
  }

}
