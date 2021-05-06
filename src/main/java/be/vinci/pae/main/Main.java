package be.vinci.pae.main;

import java.io.IOException;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.logging.FileHandler;
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


    if (!testLoggerPath()) {
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

  private static boolean testLoggerPath() {
    String path = Config.getStringProperty("logPath");
    String fileName = Config.getStringProperty("logFileName");

    if (path.toLowerCase().equals("default")) {
      path = System.getProperty("user.dir");
      path += "/Logs";
    }

    if (!path.endsWith("/")) {
      path += "/";
    }
    String fullPath = path + fileName;
    try {
      new FileHandler(fullPath);
    } catch (NoSuchFileException e) {
      System.err.println("Le dossier " + path
          + " n'existe pas.\nVeuillez:\n\tLe créer\n\tModifier la destination dans le fichier properties");
      return false;
    } catch (SecurityException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
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
