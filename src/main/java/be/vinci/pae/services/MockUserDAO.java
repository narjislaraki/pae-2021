package be.vinci.pae.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import be.vinci.pae.domain.UserDTO;
import be.vinci.pae.domain.UserImpl;

public class MockUserDAO implements UserDAO {

  public MockUserDAO() {
    // TODO Beware, the connection has to be done in an extern class
  }


  @Override
  public UserDTO getUser(String email) throws NullPointerException {
    UserDTO user = null;
    if (email.equals("test@test.com")) {
      
      user = new UserImpl();
      user.setId(0);
      user.setUsername("test");
      user.setLastName("Heuzer");
      user.setFirstName("Nina");
      user.setEmail("test@test.com");
      user.setRole("admin");
      String str = "2021-01-05 00:00";
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
      user.setRegistrationDate(dateTime);
      user.setValidated(true);
      user.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234
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
      user.setPassword("$2a$10$9fCguFzUn1ae/wFf.nHFkObDBPQqX8TII5QOaSO/GTNw7iZtLECJu"); // 1234
      user.setAddress(0);
    }
    return user;
  }

}
