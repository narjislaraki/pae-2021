package be.vinci.pae.api;

import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/furnitures")
public class FurnitureRessource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private FurnitureUCC furnitureUCC;

  public FurnitureRessource(FurnitureUCC furnitureUCC) {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Get a list of furnitures.
   * 
   * @param request the request
   * @return a list of furnitures
   */
  @GET
  @Path("list")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getFurnituresList(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.seeFurnitureList();
    return list;
  }

}
