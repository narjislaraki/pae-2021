package be.vinci.pae.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.utils.Config;
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
public class Authentication {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private UserUCC userUCC;

  // @Inject private UtilisateurFactory uf;

  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      return Response.status(Status.UNAUTHORIZED)
          .entity("Les champs avec la mention * doivent être remplis").type(MediaType.TEXT_PLAIN)
          .build();
    }
    String email = json.get("email").asText();
    String password = json.get("password").asText();
    // Try to login
    // TODO cast ou DTO?
    User user = (User) userUCC.connection(email, password);
    if (user == null) {
      return Response.status(Status.UNAUTHORIZED).entity("Les données entrées sont incorrectes")
          .type(MediaType.TEXT_PLAIN).build();
    }

    return createToken(user);
  }

  private Response createToken(User user) {
    String token;
    try {
      token =
          JWT.create().withIssuer("auth0").withClaim("user", user.getId()).sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Incapable de créer un token", e,
          Status.INTERNAL_SERVER_ERROR);
    }
    // Build response

    // load the user data from a public JSON view to filter out the private info not
    // to be returned by the API (such as password)
    User userDTO = Json.filterPublicJsonView(user, User.class);
    ObjectNode node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", userDTO);
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }
}
