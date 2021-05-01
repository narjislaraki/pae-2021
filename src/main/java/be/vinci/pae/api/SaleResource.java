package be.vinci.pae.api;

import static be.vinci.pae.utils.ResponseTool.responseOkWithEntity;
import static be.vinci.pae.utils.ResponseTool.responseWithStatus;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.sale.SaleDTO;
import be.vinci.pae.domain.sale.SaleUCC;
import be.vinci.pae.views.Views;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
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
}
