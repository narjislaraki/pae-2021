package be.vinci.pae.api;

import java.util.Map;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import be.vinci.pae.domain.User;
import be.vinci.pae.domain.UserUCC;
import be.vinci.pae.utils.Config;
import be.vinci.pae.views.Views;
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

  public Authentication() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Inject
  private UserUCC userUCC;

  /**
   * This method is used to attempt to log a client in.
   * 
   * Valid email and password are required to be able to send a token and a response 200.
   * 
   * @param json post received from the client
   * @return Response 401 if KO; 200 and credentials + token if OK
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response login(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("password")) {
      return Response.status(Status.UNAUTHORIZED)
          .entity("Les champs avec la mention * doivent être remplis").type(MediaType.TEXT_PLAIN)
          .build();
    }
    String email = json.get("email").asText();
    String password = json.get("password").asText();


    User user = (User) userUCC.connection(email, password);
    if (user == null) {
      return Response.status(Status.UNAUTHORIZED).entity("Les données entrées sont incorrectes")
          .type(MediaType.TEXT_PLAIN).build();
    }

    return createToken(user);
  }

  /**
   * Create a valid token to be sent to a user.
   * 
   * Also append user's public data.
   * 
   * @param user a non null user
   * @return a valid token with user's public data
   */
  private Response createToken(User user) {
    String token;
    try {
      token =
          JWT.create().withIssuer("auth0").withClaim("user", user.getId()).sign(this.jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Incapable de créer un token", e,
          Status.INTERNAL_SERVER_ERROR);
    }

    ObjectNode node = null;
    try {
      String json = jsonMapper.writerWithView(Views.Public.class).writeValueAsString(user);
      @SuppressWarnings("unchecked")
      Map<String, String> map = jsonMapper.readValue(json, Map.class);
      node = jsonMapper.createObjectNode().put("token", token).putPOJO("user", map);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }

}
