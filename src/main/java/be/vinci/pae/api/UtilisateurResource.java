package be.vinci.pae.api;

// import be.vinci.pae.api.filters.Authorize;
// import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.UtilisateurFactory;
import be.vinci.pae.services.DataServiceUtilisateurCollection;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;

@Singleton
@Path("/utilisateurs")
public class UtilisateurResource {

  @Inject
  private DataServiceUtilisateurCollection dataService;

  @Inject
  private UtilisateurFactory utilisateurFactory;

  /*
   * @POST
   * 
   * @Path("init")
   * 
   * @Produces(MediaType.APPLICATION_JSON) public Utilisateur init() { Utilisateur user = this.utilisateurFactory.getUser(); user.setID(1);
   * user.setLogin("james"); user.setPassword(user.hashPassword("password")); this.dataService.addUser(user); // load the user data from a public JSON
   * view to filter out the private info not // to be returned by the API (such as password) return Json.filterPublicJsonView(user, User.class); }
   * 
   * @GET
   * 
   * @Path("me")
   * 
   * @Produces(MediaType.APPLICATION_JSON)
   * 
   * @Authorize public User getUser(@Context ContainerRequest request) { User currentUser = (User) request.getProperty("user"); return
   * Json.filterPublicJsonView(currentUser, User.class); }
   * 
   * 
   * @GET
   * 
   * @Produces(MediaType.APPLICATION_JSON)
   * 
   * @Authorize public List<User> getAllUsers() { System.out.println("List after serialization : " +
   * Json.filterPublicJsonViewAsList(this.dataService.getUsers(), User.class).toString()); return
   * Json.filterPublicJsonViewAsList(this.dataService.getUsers(), User.class); }
   */

}
