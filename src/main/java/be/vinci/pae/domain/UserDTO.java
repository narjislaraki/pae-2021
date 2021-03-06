package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = UserImpl.class)
public interface UserDTO {

  enum Role {
    ADMIN, ANTIQUAIRE, CLIENT
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
