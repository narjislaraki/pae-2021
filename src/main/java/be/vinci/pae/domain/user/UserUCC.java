package be.vinci.pae.domain.user;

import java.util.List;

public interface UserUCC {

	UserDTO connection(String email, String password);

	boolean registration(UserDTO user);

	boolean acceptUser(int id, String role);

	boolean deleteUser(int id);

	List<UserDTO> getUnvalidatedUsers();

	UserDTO getUserFromId(int id);

	List<UserDTO> getValidatedUsers();
}
