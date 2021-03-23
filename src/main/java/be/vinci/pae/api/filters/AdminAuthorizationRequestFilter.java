package be.vinci.pae.api.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import be.vinci.pae.api.exceptions.UnauthorizedException;
import be.vinci.pae.domain.user.UserDTO;
import be.vinci.pae.services.dao.UserDAO;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@AdminAuthorize
public class AdminAuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getStringProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier =
      JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();

  @Inject
  private UserDAO userDAO;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource").build());
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new WebApplicationException("Malformed token", e, Status.UNAUTHORIZED);
      }
      UserDTO currentUser = this.userDAO.getUserFromId(decodedToken.getClaim("user").asInt());
      requestContext.setProperty("user", currentUser);

      if (!currentUser.getRole().getString().toLowerCase().equals("admin")) {
        throw new UnauthorizedException("Admin only");
      }
    }
  }
}
