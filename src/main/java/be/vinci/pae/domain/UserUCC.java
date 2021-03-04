package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserUCC.class)
public interface UserUCC {

  UserDTO connection(String username);

}
