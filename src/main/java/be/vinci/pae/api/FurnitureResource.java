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
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/furnitures")
public class FurnitureResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private FurnitureUCC furnitureUCC;

  public FurnitureResource() {
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
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getPublicFurnituresList() {
    return furnitureUCC.getFurnitureList();
  }

  @GET
  @Path("furniture/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public FurnitureDTO getFurniture(@Context ContainerRequest request, @PathParam("id") int id) {
    return furnitureUCC.getFurnitureById(id);
  }



  @AdminAuthorize
  @PATCH
  @Path("furniture/{id}/workShop")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean workShop(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateSentToWorkshop(id);
    return true;
  }

  @AdminAuthorize
  @PATCH
  @Path("furniture/{id}/dropOfStore")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean dropOfStore(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateDropOfStore(id);
    return true;
  }

  @AdminAuthorize
  @PATCH
  @Path("furniture/{id}/offeredForSale/{price}")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean offeredForSale(@Context ContainerRequest request, @PathParam("id") int id,
      @PathParam("price") double price) {
    furnitureUCC.indicateOfferedForSale(id, price);
    return true;
  }

  @AdminAuthorize
  @PATCH
  @Path("furniture/{id}/withdrawSale")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean withdrawSale(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.withdrawSale(id);
    return true;
  }

}
