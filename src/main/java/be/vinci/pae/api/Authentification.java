package be.vinci.pae.api;

import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.Utilisateur;
import be.vinci.pae.domain.UtilisateurFactory;
import be.vinci.pae.services.UtilisateurDAO;
import be.vinci.pae.utils.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/auths")
public class Authentification {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UtilisateurDAO dataService;

  @Inject
  private UtilisateurFactory userFactory;

  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("motDePasse")) {
      return Response.status(Status.UNAUTHORIZED).entity("Pseudo et mot de passe nécessaires")
          .type(MediaType.TEXT_PLAIN).build();
    }
    String pseudo = json.get("pseudo").asText();
    String motDePasse = json.get("motDePasse").asText();
    // Try to login
    Utilisateur utilisateur = this.dataService.getUtilisateur(pseudo);
    if (utilisateur == null || !utilisateur.checkMotDePasse(motDePasse)) {
      return Response.status(Status.UNAUTHORIZED).entity("Pseudo ou mot de passe incorrect")
          .type(MediaType.TEXT_PLAIN).build();
    }
    // Create token
    String token;
    try {
      token = JWT.create().withIssuer("auth0").withClaim("utilisateur", utilisateur.getId())
          .sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Incapable de créer un token", e,
          Status.INTERNAL_SERVER_ERROR);
    }
    // Build response

    // load the user data from a public JSON view to filter out the private info not
    // to be returned by the API (such as password)
    Utilisateur utilisateurDTO = Json.filterPublicJsonView(utilisateur, Utilisateur.class);
    ObjectNode node =
        jsonMapper.createObjectNode().put("token", token).putPOJO("utilisateur", utilisateurDTO);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();

  }

  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response register(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("motDePasse")) {
      return Response.status(Status.UNAUTHORIZED).entity("Pseudo et mot de passe nécessaires")
          .type(MediaType.TEXT_PLAIN).build();
    }
    String pseudo = json.get("pseudo").asText();
    // Check if user exists
    if (this.dataService.getUtilisateur(pseudo) != null) {
      return Response.status(Status.CONFLICT).entity("Ce pseudo est déjà utilisé")
          .type(MediaType.TEXT_PLAIN).build();
    }

    String password = json.get("motDePasse").asText();
    // create user
    Utilisateur utilisateur = (Utilisateur) this.userFactory.getUtilisateurDTO();
    utilisateur.setId(1);
    utilisateur.setPseudo(pseudo);
    utilisateur.setMotDePasse(utilisateur.hashMotDePasse(password));
    this.dataService.addUtilisateur(utilisateur);

    // Create token
    String token;
    try {
      token = JWT.create().withIssuer("auth0").withClaim("utilisateur", utilisateur.getId())
          .sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Incapable de créer un token", e,
          Status.INTERNAL_SERVER_ERROR);
    }
    // Build response

    // load the user data from a public JSON view to filter out the private info not
    // to be returned by the API (such as password)
    // String publicSerializedUser = Json.serializePublicJsonView(utilisateur);
    Utilisateur utilisateurDTO = Json.filterPublicJsonView(utilisateur, Utilisateur.class);
    ObjectNode node =
        jsonMapper.createObjectNode().put("token", token).putPOJO("utilisateur", utilisateurDTO);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();

  }

}
