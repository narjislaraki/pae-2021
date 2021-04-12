package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.visit.VisitDTO;
import be.vinci.pae.domain.visit.VisitUCC;
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
   * Get a list of not confirmed requests of visits.
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
   * Acceptation of a request for visit.
   * 
   * @param request the request
   * @param id the visit's id
   * @return true if OK
   */
  @POST
  @Path("{id}/accept")
  @AdminAuthorize
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public boolean acceptVisit(@Context ContainerRequest request, @PathParam("id") int idVisit,
      JsonNode json) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm:ss");
    LocalDateTime scheduledDateTime =
        LocalDateTime.parse(json.get("scheduledDateTime").asText(), formatter);
    return visitUCC.acceptVisit(idVisit, scheduledDateTime);
  }

  /**
   * Cancellation of a request for visit.
   * 
   * @param request the request
   * @param id the request visit's id
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
   * Get a specific furniture for logger users by giving its id.
   * 
   * @param id the furniture's id
   * @return the furniture wrapped in a Response
   */
  @GET
  @Authorize
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

}
