package be.vinci.pae.domain.interfaces;

public interface User extends UserDTO {

  boolean checkPassword(String password);

  String hashPassword(String password);

}
