package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.interfaces.FurnitureDTO;
import be.vinci.pae.domain.interfaces.VisitDTO;
import be.vinci.pae.ucc.interfaces.VisitUCC;
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
@Path("/visits")
public class VisitResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private VisitUCC visitUCC;

  public VisitResource() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  /**
   * Get a list of requests for visits with the status "en attente".
   * 
   * @param request the request
   * @return a list of not confirmed requests of visits wrapped in a Response
   */
  @GET
  @Path("notConfirmedVisits")
  @AdminAuthorize
  public Response getListOfNotConfirmedVisits(@Context ContainerRequest request) {
    List<VisitDTO> list = visitUCC.getNotConfirmedVisits();
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);

    } catch (JsonProcessingException e) {
      return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of visits to be processed (accept/refuse for each furniture).
   * 
   * @param request the request
   * @return a list of confirmed visits
   */
  @GET
  @Path("toBeProcessedVisits")
  @AdminAuthorize
  public Response getListOfVisitsToBeProcessed(@Context ContainerRequest request) {
    List<VisitDTO> list = visitUCC.getVisitsToBeProcessed();
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of furnitures for one request for visit for admins.
   * 
   * @param request the request
   * @param idVisit the id of the request for visit
   * @return a list of furnitures for one requests for visits wrapped in a Response
   */
  @GET
  @Path("{idVisit}/furnitures")
  @AdminAuthorize
  public Response getListFurnituresForOneVisit(@Context ContainerRequest request,
      @PathParam("idVisit") int idVisit) {
    List<FurnitureDTO> list = visitUCC.getListFurnituresForOneVisit(idVisit);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);

    } catch (JsonProcessingException e) {
      return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of furnitures for one request for visit for client.
   * 
   * @param request the request
   * @param idVisit the id of the request for visit
   * @return a list of furnitures for one requests for visits wrapped in a Response
   */
  @GET
  @Path("{idVisit}/furnitures/public")
  @Authorize
  public Response getListFurnituresForOneVisitPublic(@Context ContainerRequest request,
      @PathParam("idVisit") int idVisit) {
    List<FurnitureDTO> list = visitUCC.getListFurnituresForOneVisit(idVisit);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);

    } catch (JsonProcessingException e) {
      return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }

  /**
   * Get a list of requests for visits introduced by the client given by his id.
   * 
   * @param request the request
   * @param idClient the client id
   * @return a list of requests for visits introduced by the client given by his id
   */
  @GET
  @Path("myVisits/{idClient}")
  @Authorize
  public Response getVisitsListForAClient(@Context ContainerRequest request,
      @PathParam("idClient") int idClient) {
    List<VisitDTO> list = visitUCC.getVisitsListForAClient(idClient);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      return responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }


  /**
   * Acceptation of a request for visit.
   * 
   * @param request the request
   * @param idVisit the visit's id
   * @return true if OK
   */
  @POST
  @Path("{id}/accept")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean acceptVisit(@Context ContainerRequest request, @PathParam("id") int idVisit,
      JsonNode json) {
    LocalDateTime scheduledDateTime = LocalDateTime.parse(json.get("scheduledDateTime").asText());
    return visitUCC.acceptVisit(idVisit, scheduledDateTime);
  }

  /**
   * Cancellation of a request for visit.
   * 
   * @param request the request
   * @param idVisit the request visit's id
   * @return true if OK
   */
  @POST
  @Path("{id}/cancel")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean cancelVisit(@Context ContainerRequest request, @PathParam("id") int idVisit,
      JsonNode json) {
    String explanatoryNote = json.get("explanatoryNote").asText();
    return visitUCC.cancelVisit(idVisit, explanatoryNote);
  }

  /**
   * Get a specific visit for logged users by giving its id.
   * 
   * @param id the visit's id
   * @return the visit wrapped in a Response
   */
  @GET
  @AdminAuthorize
  @Path("{id}")
  public Response getVisit(@Context ContainerRequest request, @PathParam("id") int id) {
    VisitDTO visit = visitUCC.getVisitById(id);
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(visit);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
  }


  /**
   * This method is used for introduce a request for visit. It also adds the address into the
   * database if this is different from the customer's address. This method adds each furniture and
   * for each furniture, each photo.
   * 
   * @param visit the request for visit converted from json
   * @return Response 401 Or 409 if KO; token if OK
   */
  @Authorize
  @POST
  @Path("introduce")
  public Response introduceRequestForVisit(VisitDTO visit) {
    if (!checkFieldsIntroduce(visit)) {
      return responseWithStatus(Status.PRECONDITION_FAILED, "Missing fields");
    }
    return responseWithStatus(Status.CREATED, visitUCC.submitRequestOfVisit(visit));
  }

  private boolean checkFieldsIntroduce(VisitDTO visit) {
    if (isAnOtherAddress(visit)) {
      if (visit.getTimeSlot() == null || visit.getWarehouseAddress().getStreet() == null
          || visit.getWarehouseAddress().getBuildingNumber() == null
          || visit.getWarehouseAddress().getCity() == null
          || visit.getWarehouseAddress().getPostCode() == null
          || visit.getWarehouseAddress().getCountry() == null || visit.getIdClient() == 0
          || visit.getTimeSlot().isEmpty() || visit.getWarehouseAddress().getStreet().isEmpty()
          || visit.getWarehouseAddress().getBuildingNumber().isEmpty()
          || visit.getWarehouseAddress().getCity().isEmpty()
          || visit.getWarehouseAddress().getPostCode().isEmpty()
          || visit.getWarehouseAddress().getCountry().isEmpty()) {
        return false;
      }
    } else {
      if (visit.getTimeSlot() == null || visit.getIdClient() == 0
          || visit.getTimeSlot().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  private boolean isAnOtherAddress(VisitDTO visit) {
    if (!visit.getWarehouseAddress().getStreet().isEmpty()
        || !visit.getWarehouseAddress().getBuildingNumber().isEmpty()
        || !visit.getWarehouseAddress().getCity().isEmpty()
        || !visit.getWarehouseAddress().getPostCode().isEmpty()
        || !visit.getWarehouseAddress().getCountry().isEmpty()) {
      return true;
    }
    return false;
  }
}
