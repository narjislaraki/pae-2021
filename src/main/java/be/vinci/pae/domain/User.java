package be.vinci.pae.domain;

public interface User extends UserDTO {

  boolean checkPassword(String password);

  String hashPassword(String password);

}
