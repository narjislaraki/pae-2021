package be.vinci.pae.api;

import org.glassfish.jersey.server.ContainerRequest;
import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.user.UserDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/users")
public class UserRessource {

  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  public UserDTO getUser(@Context ContainerRequest request) {
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    return Json.filterPublicJsonView(currentUser, UserDTO.class);
  }



}
