package be.vinci.pae.api;

import org.glassfish.jersey.server.ContainerRequest;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.user.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

public class UserRessource {

  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public User getUser(@Context ContainerRequest request) {
    User currentUser = (User) request.getProperty("user");
    return Json.filterPublicJsonView(currentUser, User.class);
  }
}
