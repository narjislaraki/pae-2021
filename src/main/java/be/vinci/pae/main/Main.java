package be.vinci.pae.main;

import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import be.vinci.pae.services.UserDAO;
import be.vinci.pae.services.UserDAOImpl;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class Main {

  static UserDAO ds = new UserDAOImpl();
  static String env;

  /**
   * Main method.
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) throws IOException {

    try {
      env = args[0];
    } catch (Exception e) {
      env = "test";
    }

    switch (env) {
      case "prod":
        Config.load(Config.PROD);
        break;

      default:
        Config.load(Config.TEST);
        break;
    }

    final HttpServer server = startServer();

    System.out.println("Jersey app started at " + Config.getProperty("BaseUri"));
    // Listen to key press and shutdown server
    System.out.println("Hit enter to stop it...");
    System.in.read();
    server.shutdownNow();
  }

  /**
   * Start a Grizzly server.
   * 
   * @return HttpServer the server
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .register(JacksonFeature.class).register(ApplicationBinder.class)
        .property("jersey.config.server.wadl.disableWadl", true);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

}
