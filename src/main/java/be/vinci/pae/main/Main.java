package be.vinci.pae.main;

import java.io.IOException;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import be.vinci.pae.utils.APILogger;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;

public class Main {

  private static String env;
  private static Logger logger;

  /**
   * Main method.
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {

    try {
      env = args[0];
    } catch (Exception e) {
      env = "prod";
    }

    switch (env) {
      case "prod":
        Config.load(Config.PROD);
        break;

      default:
        Config.load(env);
        break;
    }


    /* Logger FileHanlder init and path validity check */
    try {
      APILogger.getFh();
    } catch (NoSuchFileException e) {
      System.err.println(
          "Logger folder does not exists.\n\tCreate the folder.\n\tChange the path in the properties file.");
      return;
    } catch (SecurityException e1) {
      e1.printStackTrace();
      return;
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }

    final HttpServer server = startServer();

    logger = APILogger.getLogger();
    logger.info("Server is starting");

    System.out.println("Jersey app started at " + Config.getStringProperty("BaseUri"));
    // Listen to key press and shutdown server
    System.out.println("Hit enter to stop it...");
    try {
      System.in.read();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    server.shutdownNow();
  }

  /**
   * Start a Grizzly server.
   * 
   * @return HttpServer the server
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.api")
        .packages("be.vinci.pae.exceptions").register(JacksonFeature.class)
        .register(ApplicationBinder.class).property("jersey.config.server.wadl.disableWadl", true);
    return GrizzlyHttpServerFactory
        .createHttpServer(URI.create(Config.getStringProperty("BaseUri")), rc);
  }

}
