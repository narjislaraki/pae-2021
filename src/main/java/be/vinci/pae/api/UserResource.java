package be.vinci.pae.api;

import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.views.Views;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/users")
public class UserResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  public UserResource() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @GET
  @Path("me")
  @Produces(MediaType.APPLICATION_JSON)
  public String getUser(@Context ContainerRequest request) {
    UserDTO currentUser = (UserDTO) request.getProperty("user");
    try {
      return jsonMapper.writerWithView(Views.Public.class).writeValueAsString(currentUser);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "";
  }



}
