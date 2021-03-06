package be.vinci.pae.domain.user;


import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.views.Views;


public class UserImpl implements User {

  // TODO assurer les bonnes vues où on le veut
  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Private.class)
  private Address address;
  @JsonView(Views.Public.class)
  private String username;
  @JsonView(Views.Private.class)
  private String lastName;
  @JsonView(Views.Private.class)
  private String firstName;
  @JsonView(Views.Private.class)
  private String email;
  @JsonView(Views.Internal.class)
  private String password;
  @JsonView(Views.Internal.class)
  private String passwordVerification;
  @JsonView(Views.Public.class)
  private Role role;
  @JsonView(Views.Private.class)
  private boolean validated;
  @JsonView(Views.Private.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime registrationDate; // TODO DateTime?

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public Address getAddress() {
    return address;
  }

  @Override
  public void setAddress(Address address) {
    this.address = address;
  }


  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getPasswordVerification() {
    return passwordVerification;
  }

  public void setPasswordVerification(String passwordVerification) {
    this.passwordVerification = passwordVerification;
  }

  @Override
  public Role getRole() {
    return role;
  }

  @Override
  public void setRole(String role) {
    switch (role.toLowerCase()) {
      case "admin":
        this.role = Role.ADMIN;
        break;

      case "antiquaire":
        this.role = Role.ANTIQUAIRE;
        break;

      case "client":
        this.role = Role.CLIENT;
        break;

      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  public boolean isValidated() {
    return validated;
  }

  @Override
  public void setValidated(boolean validated) {
    this.validated = validated;
  }

  @Override
  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }

  @Override
  public void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }

  @Override
  public boolean checkPassword(String password) {
    return BCrypt.checkpw(password, this.password);
  }

  @Override
  public String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public String toString() {
    return "UserImpl [id=" + id + ", address=" + address + ", username=" + username + ", lastName="
        + lastName + ", firstName=" + firstName + ", email=" + email + ", password=" + password
        + ", passwordVerification=" + passwordVerification + ", role=" + role + ", validated="
        + validated + ", registrationDate=" + registrationDate + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UserImpl other = (UserImpl) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

}
