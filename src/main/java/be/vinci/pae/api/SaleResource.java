package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.vinci.pae.api.filters.AdminAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.interfaces.SaleDTO;
import be.vinci.pae.domain.interfaces.UserDTO;
import be.vinci.pae.ucc.interfaces.SaleUCC;
import be.vinci.pae.views.Views;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/sales")
public class SaleResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private SaleUCC saleUCC;

  public SaleResource() {
    jsonMapper.findAndRegisterModules();
  }



  /**
   * Get a list of all the sales.
   * 
   * @param request the request
   * @return a list of sales wrapped in a Response
   */
  @GET
  @Authorize
  public Response getSalesList(@Context ContainerRequest request) {
    List<SaleDTO> list = saleUCC.getSalesList();
    String r = null;
    try {
      r = jsonMapper.writerWithView(Views.Private.class).writeValueAsString(list);
    } catch (JsonProcessingException e) {
      responseWithStatus(Status.INTERNAL_SERVER_ERROR, "Problem while converting data");
    }
    return responseOkWithEntity(r);
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
  @Produces(MediaType.APPLICATION_JSON)
  public boolean addSale(@Context ContainerRequest request, SaleDTO sale) {
    return saleUCC.addSale(sale, (UserDTO) request.getProperty("user"));
  }
}
