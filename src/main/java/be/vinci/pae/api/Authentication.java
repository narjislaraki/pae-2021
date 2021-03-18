package be.vinci.pae.api;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import be.vinci.pae.domain.address.Address;
import be.vinci.pae.domain.address.AddressFactory;
import be.vinci.pae.domain.user.User;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.domain.user.UserFactory;
import be.vinci.pae.domain.user.UserUCC;
import be.vinci.pae.services.dao.AddressDAO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.APILogger;
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

  private static final Logger LOGGER = APILogger.getLogger();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getStringProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();

  public Authentication() {
    jsonMapper.findAndRegisterModules();
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Inject
  private UserUCC userUCC;

  @Inject
  private UserDAO userDAO;

  @Inject
  private UserFactory userFactory;

  @Inject
  private AddressFactory addressFactory;

  @Inject
  private AddressDAO addressDAO;

  /**
   * Quick way to construct HTTP Response with text only.
   * 
   * @param status the status
   * @param message the message
   * @return a Response containing the status and the message
   */
  private Response constructResponse(Status status, String message) {
    return Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build();
  }

  /**
   * This method is used to attempt to log a client in.Valid email and password are required to be
   * able to send a token and a response 200.
   * 
   * @param json post received from the client
   * @return Response 401 if KO; 200 and credentials + token if OK
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response login(JsonNode json) {
    if (!checkLoginFields(json)) {
      return constructResponse(Status.PRECONDITION_FAILED, "The input data is invalid");
    }

    String email = json.get("email").asText();
    String password = json.get("password").asText();

    User user = (User) userUCC.connection(email, password);

    return createToken(user);
  }

  private boolean checkLoginFields(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("password")
        || json.get("email").asText().isEmpty() || json.get("password").asText().isEmpty()) {
      return false;
    }
    return true;
  }


  /**
   * This method is used for registering a user. It also adds the address into the database
   * 
   * @param json post received from the client
   * @return Response 401 Or 409 if KO; token if OK
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response register(JsonNode json) {
    if (!checkFieldsRegister(json)) {
      return Response.status(Status.UNAUTHORIZED).entity("Missing fields")
          .type(MediaType.TEXT_PLAIN).build();
    }

    final String email = json.get("email").asText();
    final String username = json.get("username").asText();
    final String password = json.get("password").asText();
    final String confirmPassword = json.get("confirmPassword").asText();
    final String firstName = json.get("firstName").asText();
    final String lastName = json.get("lastName").asText();
    final String street = json.get("street").asText();
    final String buildingNumber = json.get("buildingNumber").asText();
    final String city = json.get("city").asText();
    final String postcode = json.get("postcode").asText();
    final String country = json.get("country").asText();
    int unitNumber = 0;
    if (!json.hasNonNull("unitNumber") && !json.get("unitNumber").asText().isEmpty()) {
      unitNumber = json.get("unitNumber").asInt();
    }

    User user = (User) userDAO.getUserFromEmail(email);
    if (user != null) {
      return Response.status(Status.CONFLICT).entity("This email is already in use")
          .type(MediaType.TEXT_PLAIN).build();
    }
    user = (User) userDAO.getUserFromUsername(username);
    if (user != null) {
      return Response.status(Status.CONFLICT).entity("This username is already in use")
          .type(MediaType.TEXT_PLAIN).build();
    }

    if (!password.equals(confirmPassword)) {
      return Response.status(Status.UNAUTHORIZED).entity("The passwords don't match")
          .type(MediaType.TEXT_PLAIN).build();
    }

    Address address = addressFactory.getAddress();
    address.setStreet(street);
    address.setCity(city);
    address.setBuildingNumber(buildingNumber);
    address.setPostCode(postcode);
    address.setCountry(country);
    if (unitNumber != 0) {
      address.setUnitNumber(unitNumber);
    }

    int idAddress = addressDAO.addAddress(address);
    address.setId(idAddress);

    UserDTO userDTO = userFactory.getUserDTO();
    User userCast = (User) userDTO;
    userCast.setEmail(email);
    userCast.setUsername(username);
    userCast.setFirstName(firstName);
    userCast.setLastName(lastName);
    userCast.setPassword(userCast.hashPassword(password));
    userCast.setValidated(false);
    userCast.setRegistrationDate(LocalDateTime.now());
    userCast.setRole("client");
    userCast.setAddress(address);

    userDAO.addUser(userCast);
    return createToken(userCast);
  }

  private boolean checkFieldsRegister(JsonNode json) {
    if (!json.hasNonNull("email") || !json.hasNonNull("password")
        || !json.hasNonNull("confirmPassword") || !json.hasNonNull("firstName")
        || !json.hasNonNull("lastName") || !json.hasNonNull("street")
        || !json.hasNonNull("buildingNumber") || !json.hasNonNull("city")
        || !json.hasNonNull("postcode") || !json.hasNonNull("country")
        || json.get("email").asText().isEmpty() || json.get("password").asText().isEmpty()
        || json.get("confirmPassword").asText().isEmpty()
        || json.get("firstName").asText().isEmpty() || json.get("lastName").asText().isEmpty()
        || json.get("street").asText().isEmpty() || json.get("buildingNumber").asText().isEmpty()
        || json.get("city").asText().isEmpty() || json.get("postcode").asText().isEmpty()
        || json.get("country").asText().isEmpty()) {
      return false;
    }
    return true;
  }

  /**
   * Create a valid token to be sent to a user. Also append user's public data.
   * 
   * @param user a non null user
   * @return a valid token with user's public data
   */
  private Response createToken(User user) {
    String token;
    try {
      token = JWT.create().withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
          .withIssuer("auth0").withClaim("user", user.getId()).sign(this.jwtAlgorithm);
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
    LOGGER.log(Level.INFO, "Connection of user:" + user.getUsername() + " :: " + user.getId());
    return Response.ok(node, MediaType.APPLICATION_JSON).build();
  }

}
