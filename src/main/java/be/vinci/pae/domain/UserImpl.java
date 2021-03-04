package be.vinci.pae.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserImpl implements User {

  private int id;
  private int address;
  private String username;
  private String lastName;
  private String firstName;
  private String email;
  private String password;
  private Role role;
  private boolean validated;
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
  public int getAddress() {
    return address;
  }

  @Override
  public void setAddress(int address) {
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
  public boolean isValidated() { // TODO Modifier titre?
    return validated;
  }

  @Override
  public void setIsValidated(boolean isValidated) {
    this.validated = isValidated;
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
    return "{id:" + id + ", login:" + username + ", password:" + password + "}";
  }

}
