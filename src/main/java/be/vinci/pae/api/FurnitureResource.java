package be.vinci.pae.api;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.user.UserDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
   * Get a list of furnitures for the unlogged users.
   * 
   * @param request the request
   * @return a list of furnitures
   */
  @GET
  @Path("public")
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getPublicFurnituresList(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList(null);
    return list;
  }

  /**
   * Get a list of furnitures for the logged users.
   * 
   * @param request the request
   * @return a list of furniture adapted if it's the user is a client or an admin
   */
  @GET
  @Authorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getFurnituresList(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList(user);

    return list;
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public FurnitureDTO getFurniture(@Context ContainerRequest request, @PathParam("id") int id) {
    return furnitureUCC.getFurnitureById(id);
  }

  @Authorize
  @GET
  @Path("{id}/getOption")
  @Produces(MediaType.APPLICATION_JSON)
  public OptionDTO getOption(@Context ContainerRequest request, @PathParam("id") int id) {
    OptionDTO opt = furnitureUCC.getOption(id);
    return opt;
  }

  @Authorize
  @GET
  @Path("{idFurniture}/getSumOfOptionDays")
  @Produces(MediaType.APPLICATION_JSON)
  public int getSumOfOptionDaysForAUserAboutAFurniture(@Context ContainerRequest request,
      @PathParam("idFurniture") int idFurniture) {
    int idUser = ((UserDTO) request.getProperty("user")).getId();
    return furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(idFurniture, idUser);
  }

  @AdminAuthorize
  @POST
  @Path("{id}/workShop")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean sendToWorkShop(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateSentToWorkshop(id);
    return true;
  }

  @AdminAuthorize
  @POST
  @Path("{id}/dropOfStore")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean dropOfStore(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateDropOfStore(id);
    return true;
  }

  @AdminAuthorize
  @POST
  @Path("{id}/offeredForSale")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean offeredForSale(@Context ContainerRequest request, @PathParam("id") int id,
      JsonNode json) {
    double price = json.get("furniturePrice").asDouble();
    furnitureUCC.indicateOfferedForSale(id, price);
    return true;
  }

  @AdminAuthorize
  @POST
  @Path("{id}/withdrawSale")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean withdrawSale(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.withdrawSale(id);
    return true;
  }

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

  @Authorize
  @POST
  @Path("{idFurniture}/{idUser}/introduceOption")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean introduceOption(@Context ContainerRequest request,
      @PathParam("idFurniture") int idFurniture, @PathParam("idUser") int idUser, JsonNode json) {
    int optionTerm = json.get("duration").asInt();
    furnitureUCC.introduceOption(optionTerm, idUser, idFurniture);
    return true;
  }

}
