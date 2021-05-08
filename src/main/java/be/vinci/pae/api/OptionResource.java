package be.vinci.pae.api;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.ucc.FurnitureUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/options")
public class OptionResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private FurnitureUCC furnitureUCC;

  public OptionResource() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Cancel an option.
   * 
   * @param request the request
   * @param id the option id
   * @param json the json
   * @return true
   */
  @Authorize
  @POST
  @Path("{id_option}/cancelOption")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean cancelOption(@Context ContainerRequest request, @PathParam("id_option") int id,
      JsonNode json) {
    String reason = json.get("cancelReason").asText();
    furnitureUCC.cancelOption(reason, id, (UserDTO) request.getProperty("user"));
    return true;
  }
}
