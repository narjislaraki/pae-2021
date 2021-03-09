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
  public static HttpServer startServer() {
    // Create a resource config that scans for JAX-RS resources and providers
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        // .packages("org.glassfish.jersey.examples.jackson")
        .register(JacksonFeature.class).register(ApplicationBinder.class)
        .property("jersey.config.server.wadl.disableWadl", true);

    // Create and start a new instance of grizzly http server
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);
  }

  /**
   * Main method.
   * 
   * @param args command line arguments
   */
  static UserDAO ds = new UserDAOImpl();
  static String env;

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
}
