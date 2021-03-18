package be.vinci.pae.domain.user;

public interface UserUCC {

  UserDTO connection(String email, String password);

  void confirmRegistration(User user, String role);

  void refuseRegistration(User user);

}
