package be.vinci.pae.api;

import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;
import be.vinci.pae.domain.admin.AdminUCC;
import be.vinci.pae.domain.user.UserDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("/admin")
public class AdminRessource {

  @Inject
  private AdminUCC adminUCC;

  @GET
  @Path("unvalidatedList")
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserDTO> getUnvalidatedList(@Context ContainerRequest request) {

    List<UserDTO> list = adminUCC.getUnvalidatedUsers();

    return list;
    // return Json.filterPublicJsonView(list, ArrayList.class);
    // return Json.filterPublicJsonView(UserDistributor.getGoodValidatedUser(), UserDTO.class);
  }
}
