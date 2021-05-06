package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.edition.EditionDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO;
import be.vinci.pae.domain.furniture.FurnitureDTO.Condition;
import be.vinci.pae.domain.furniture.FurnitureUCC;
import be.vinci.pae.domain.furniture.OptionDTO;
import be.vinci.pae.domain.furniture.TypeOfFurnitureDTO;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserDTO.Role;
import be.vinci.pae.domain.visit.PhotoDTO;
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.views.Views;
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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
   * @return a list of furnitures wrapped in a Response
   */
  @GET
  @Path("public")
  public Response getPublicFurnituresList(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList(null);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of furnitures for the logged users.
   * 
   * @param request the request
   * @return a list of furnitures adapted if it's the user is a client or an admin, wrapped in a
   *         Response
   */
  @GET
  @Authorize
  public Response getFurnituresList(@Context ContainerRequest request) {
    UserDTO user = (UserDTO) request.getProperty("user");
    List<FurnitureDTO> list = furnitureUCC.getFurnitureList(user);

    String r = null;
    try {
      if (user.getRole().equals(Role.ADMIN)) {
        r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
      } else {
        r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
      }
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of furnitures for research purposes.
   * 
   * @param request the request
   * @return a list of furnitures adapted if wrapped in a Response
   */
  @GET
  @Path("research")
  @AdminAuthorize
  public Response getFurnituresListForResearch(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.getFurnitureListForResearch();
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of furniture to be placed in the carousel (on sale, sold and under options).
   * 
   * @param request the request
   * @return a list of furniture wrapped in a Response
   */
  @GET
  @Path("slider")
  public Response getSliderFurnituresList(@Context ContainerRequest request) {
    List<FurnitureDTO> list = furnitureUCC.getSliderFurnitureList(10);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }


  /**
   * Get a list of furnitures of a certain type.
   * 
   * @param idType the id of the furniture's type
   * @return a list of furnitures of a certain type adapted if it's the user is a client or an
   *         admin, wrapped in a Response
   * 
   */
  @GET
  @Authorize
  @Path("type/{idType}")
  public Response getFurnituresListByType(@Context ContainerRequest request,
      @PathParam("idType") int idType) {
    UserDTO user = (UserDTO) request.getProperty("user");
    List<FurnitureDTO> list = furnitureUCC.getFurnitureListByType(user, idType);

    String r = null;
    try {
      if (user.getRole().equals(Role.ADMIN)) {
        r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
      } else {
        r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
      }
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }


  // This method is ultimately not used, but kept in the event that a change request needs it.
  /**
   * Get a specific furniture for unlogged users by giving its id.
   * 
   * @param id the furniture's id
   * @return the furniture wrapped in a Response
   */
  @GET
  @Path("public/{id}")
  public Response getPublicFurniture(@PathParam("id") int id) {
    FurnitureDTO furniture = furnitureUCC.getFurnitureById(id);

    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(furniture);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }

    return responseOkWithEntity(r);
  }

  /**
   * Get a specific furniture for logger users by giving its id.
   * 
   * @param id the furniture's id
   * @return the furniture wrapped in a Response
   */
  @GET
  @Authorize
  @Path("{id}")
  public Response getFurniture(@Context ContainerRequest request, @PathParam("id") int id) {
    UserDTO user = (UserDTO) request.getProperty("user");
    FurnitureDTO furniture = furnitureUCC.getFurnitureById(id);

    String r = null;
    try {
      if (user.getRole().equals(Role.ADMIN)) {
        r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(furniture);
      } else {
        r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(furniture);
      }
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }

    return responseOkWithEntity(r);
  }



  /**
   * Getting active option from a specific furniture by giving its id.
   * 
   * @param request the request
   * @param id the furniture's id
   * @return the option wrapped in a Response
   */
  @Authorize
  @GET
  @Path("{id}/getOption")
  public Response getOption(@Context ContainerRequest request, @PathParam("id") int id) {
    OptionDTO opt = furnitureUCC.getOption(id);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(opt);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }

    return responseOkWithEntity(r);
  }

  /**
   * Getting the sum of days of options that the logged user has already taken for the furniture.
   * given by idFurniture.
   * 
   * @param request the request
   * @param idFurniture the furniture id
   * @return the number of days
   */
  @Authorize
  @GET
  @Path("{idFurniture}/getSumOfOptionDays")
  @Produces(MediaType.APPLICATION_JSON)
  public int getSumOfOptionDaysForAUserAboutAFurniture(@Context ContainerRequest request,
      @PathParam("idFurniture") int idFurniture) {
    int idUser = ((UserDTO) request.getProperty("user")).getId();
    return furnitureUCC.getSumOfOptionDaysForAUserAboutAFurniture(idFurniture, idUser);
  }

  /**
   * Changes the state of the furniture given by its id to "en restauration".
   * 
   * @param request the request
   * @param id the furniture id
   * @return true
   */
  @AdminAuthorize
  @POST
  @Path("{id}/workShop")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean sendToWorkShop(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateSentToWorkshop(id);
    return true;
  }

  /**
   * Changes the state of the furniture given by its id to "déposé en magasin".
   * 
   * @param request the request
   * @param id the furniture id
   * @return true
   */
  @AdminAuthorize
  @POST
  @Path("{id}/dropOfStore")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean dropOfStore(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.indicateDropOfStore(id);
    return true;
  }

  /**
   * Changes the state of the furniture given by its id to "en vente" and set the offered selling
   * price.
   * 
   * @param request the request
   * @param id the furniture id
   * @param json the json
   * @return true or an error
   */
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

  /**
   * Withdraw a furniture and set its state to a withdrawn state.
   * 
   * @param request the request
   * @param id the furniture id
   * @return true or an error
   */
  @AdminAuthorize
  @POST
  @Path("{id}/withdrawSale")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean withdrawSale(@Context ContainerRequest request, @PathParam("id") int id) {
    furnitureUCC.withdrawSale(id);
    return true;
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

  /**
   * Introduce an option related to a furniture and a user.
   * 
   * @param request the request
   * @param idFurniture the furniture id
   * @param idUser the user id
   * @param json the json
   * @return true
   */
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

  /**
   * Add a sale and change the state of the furniture from the sale to "vendu".
   * 
   * @param request the request
   * @param sale the sale
   * @return
   */
  @AdminAuthorize
  @POST
  @Path("sale")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean addSale(@Context ContainerRequest request, SaleDTO sale) {
    return furnitureUCC.addSale(sale);
  }

  /**
   * Get a list of types of furniture.
   * 
   * @param request the request
   * @return a list of types of furniture
   */
  @GET
  @Path("typeOfFurnitureList")
  @Produces(MediaType.APPLICATION_JSON)
  public List<TypeOfFurnitureDTO> getTypeOfFurnitureList(@Context ContainerRequest request) {
    List<TypeOfFurnitureDTO> list = furnitureUCC.getTypesOfFurnitureList();
    return list;
  }

  /**
   * Get the photos related to a particuliar furniture.
   * 
   * @param request the request
   * @param idFurniture the furniture id
   * @return a list of photos
   */
  @GET
  @Path("/{idFurniture}/photos")
  @Authorize
  @Produces(MediaType.APPLICATION_JSON)
  public List<PhotoDTO> getPhotos(@Context ContainerRequest request,
      @PathParam("idFurniture") int idFurniture) {

    List<PhotoDTO> list =
        furnitureUCC.getFurniturePhotos(idFurniture, (UserDTO) request.getProperty("user"));

    return list;
  }

  /**
   * It will change the state of the furniture on the list to "purchased" or "refused" and if the
   * furniture is purchased, update its purchase price and date of shipment.
   * 
   * @param request the request
   * @param visit the visit that contains the list of furnitures to update
   * @return true
   */
  @AdminAuthorize
  @POST
  @Path("furnituresListToBeProcessed")
  @Produces(MediaType.APPLICATION_JSON)
  public Response processVisit(@Context ContainerRequest request, VisitDTO visit) {
    System.out.println(visit);
    if (!checkFieldsProcess(visit)) {
      return responseWithStatus(Status.UNAUTHORIZED, "Missing fields or uncorrect fields");
    }
    return responseWithStatus(Status.CREATED, furnitureUCC.processVisit(visit.getFurnitureList()));
  }

  private boolean checkFieldsProcess(VisitDTO visit) {
    for (FurnitureDTO furniture : visit.getFurnitureList()) {
      if (furniture.getCondition().equals(Condition.ACHETE) && (furniture.getPurchasePrice() <= 0
          || furniture.getPickUpDate() == null || furniture.getPickUpDate() == null)) {
        return false;
      }
    }
    return true;
  }



  /**
   * Allows to edit a furniture such as: description, type id, offered selling price and favourite
   * photo ID. Also allows to deal with photos with id's lists for: display, hide, delete. Finally,
   * allows to add photos to a furniture.
   * 
   * @param request the request
   * @param idFurniture the furniture's id
   * @param edition the data to be edited
   * @return true if OK
   */
  @POST
  @Path("/{idFurniture}/edit")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  public boolean edit(@Context ContainerRequest request, @PathParam("idFurniture") int idFurniture,
      EditionDTO edition) {
    edition.setIdFurniture(idFurniture);
    return furnitureUCC.edit(edition);
  }
}
