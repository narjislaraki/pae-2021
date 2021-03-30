package be.vinci.pae.api;

import java.util.List;
import java.util.stream.Collectors;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserDTO.Role;
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
  @Path("public")
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getPublicFurnituresList(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList();
    return list.stream().filter(
        e -> e.getCondition() == Condition.EN_VENTE || e.getCondition() == Condition.SOUS_OPTION)
        .collect(Collectors.toList());
  }

  @GET
  @Authorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<FurnitureDTO> getFurnituresList(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList();
    if (user.getRole() == Role.ADMIN)
      return list;
    return list.stream().filter(
        e -> e.getCondition() == Condition.EN_VENTE || e.getCondition() == Condition.SOUS_OPTION)
        .collect(Collectors.toList());
  }

  @GET
  @Path("furniture/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public FurnitureDTO getFurniture(@Context ContainerRequest request, @PathParam("id") int id) {
    return furnitureUCC.getFurnitureById(id);
  }

  @Authorize
  @GET
  @Path("furniture/{id}/getOption")
  @Produces(MediaType.APPLICATION_JSON)
  public OptionDTO getOption(@Context ContainerRequest request, @PathParam("id") int id) {
    OptionDTO opt = furnitureUCC.getOption(id);
    return opt;
  }

  @Authorize
  @GET
  @Path("furniture/{idFurniture}/{idUser}/getNbOfDay")
  @Produces(MediaType.APPLICATION_JSON)
  public int getNbOfDay(@Context ContainerRequest request,
      @PathParam("idFurniture") int idFurniture, @PathParam("idUser") int idUser) {
    return furnitureUCC.getNbOfDay(idFurniture, idUser);
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

  @Authorize
  @PATCH
  @Path("furniture/{id_option}/{reason}/cancelOption")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean cancelOption(@Context ContainerRequest request, @PathParam("id_option") int id,
      @PathParam("reason") String reason) {
    furnitureUCC.cancelOption(reason, id);
    return true;
  }

  @Authorize
  @PATCH
  @Path("furniture/{optionTerm}/{idFurniture}/{idUser}/introduceOption")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean introduceOption(@Context ContainerRequest request,
      @PathParam("optionTerm") int optionTerm, @PathParam("idFurniture") int idFurniture,
      @PathParam("idUser") int idUser) {
    furnitureUCC.introduceOption(optionTerm, idUser, idFurniture);
    return true;
  }



}
