package be.vinci.pae.services;

import java.time.LocalDateTime;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.UserImpl;
import jakarta.inject.Inject;

public class MockUserDAO implements UserDAO {

  @Inject
  private DalServices dalService;

  @Inject
  private UserFactory userFactory;

  public MockUserDAO() {
    // TODO Beware, the connection has to be done in an extern class
  }


  @Override
  public UserDTO getUser(String email) throws NullPointerException{
    UserDTO user = null;
    if (email.equals("valid@email.com")) {
        user = new UserImpl();
        user.setId(0);
        user.setUsername("test");
        user.setLastName("Jean");
        user.setFirstName("Cérien");
        user.setEmail("valid@email.com");
        user.setRole("client");
        user.setRegistrationDate(LocalDateTime.now());
        user.setValidated(true);
        user.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); //1234
        user.setAddress(0);
    }
    return user;
  }


  @Override
  public UserDTO getUser(int id) {
    UserDTO user = null;
    if (id == 0) {
        user = new UserImpl();
        user.setId(0);
        user.setUsername("test");
        user.setLastName("Jean");
        user.setFirstName("Cérien");
        user.setEmail("valid@email.com");
        user.setRole("client");
        user.setRegistrationDate(LocalDateTime.now());
        user.setValidated(true);
        user.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); //1234
        user.setAddress(0);
    }
    return user;
  }

}
