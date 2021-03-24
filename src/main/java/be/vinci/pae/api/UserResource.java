package be.vinci.pae.api;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/users")
public class UserResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserUCC userUCC;

  public UserResource() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Get a list of unvalidated users.
   * 
   * @param request the request
   * @return a list of unvalidated users
   */
  @GET
  @Path("unvalidatedList")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getUnvalidatedList(@Context ContainerRequest request) {
    List<UserDTO> list = userUCC.getUnvalidatedUsers();
    return list;
  }

  /**
   * Validation of a user.
   * 
   * @param request the request
   * @param id the user's id
   * @return true if OK
   */
  @PATCH
  @Path("user/{id}/accept/{role}")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public boolean acceptUser(@Context ContainerRequest request, @PathParam("id") int id,
      @PathParam("role") String role) {
    userUCC.acceptUser(id, role);
    return true;
  }

  /**
   * Delete a user.
   * 
   * @param request the request
   * @param id the id user's id
   * @return true if OK
   */
  @DELETE
  @Path("user/{id}")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public boolean refuseUser(@Context ContainerRequest request, @PathParam("id") int id) {
    userUCC.deleteUser(id);
    return true;
  }

}
