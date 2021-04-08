package be.vinci.pae.api;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
  public List<UserDTO> getListOfUnvalidatedUsers(@Context ContainerRequest request) {
    List<UserDTO> list = userUCC.getUnvalidatedUsers();
    // TODO retirer password des users de la liste

    // List<String> list2 = list.stream().map((UserDTO e) -> {
    // try {
    // return jsonMapper.writerWithView(Views.Private.class).writeValueAsString(e);
    // } catch (JsonProcessingException e1) {
    // e1.printStackTrace();
    // }
    // return null;
    // }).collect(Collectors.toList());

    return list;
  }

  /**
   * Validation of a user.
   * 
   * @param request the request
   * @param id the user's id
   * @return true if OK
   */
  @POST
  @Path("{id}/accept")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean acceptUser(@Context ContainerRequest request, @PathParam("id") int id,
      JsonNode json) {
    String role = json.get("role").asText();
    return userUCC.acceptUser(id, role);
  }

  /**
   * Deletion of a user.
   * 
   * @param request the request
   * @param id the id user's id
   * @return true if OK, false if nKO
   */
  @DELETE
  @Path("{id}")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public boolean refuseUser(@Context ContainerRequest request, @PathParam("id") int id) {
    return userUCC.deleteUser(id);
  }

}
