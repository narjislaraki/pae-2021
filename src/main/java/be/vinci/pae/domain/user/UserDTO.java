package be.vinci.pae.domain.user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  enum Role {
    ADMIN("admin"), ANTIQUAIRE("antiquaire"), CLIENT("client");

    private String role;

    Role(String role) {
      this.role = role;
    }

    public String getString() {
      return this.role;
    }
  }

  int getId();

  void setId(int id);

  int getAddress();

  void setAddress(int address);

  String getUsername();

  void setUsername(String username);

  String getLastName();

  void setLastName(String lastname);

  String getFirstName();

  void setFirstName(String firstname);

  String getEmail();

  void setEmail(String email);

  String getPassword();

  void setPassword(String password);

  Role getRole();

  void setRole(String role);

  boolean isValidated();

  void setValidated(boolean isValidated);

  LocalDateTime getRegistrationDate();

  void setRegistrationDate(LocalDateTime registrationDate);

}
