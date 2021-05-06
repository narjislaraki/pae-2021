package be.vinci.pae.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class APILogger {

  public static final String APILOGS = "APILogs";
  private static FileHandler fh;
  private static Logger logger;

  public APILogger() {
    getLogger();
  }

  /**
   * Get a logger that write in a file determined in the properties files. Once the logger called,
   * works as a singleton and send the same logger to every call.
   * 
   * @return a logger
   */
  public static Logger getLogger() {
    if (logger == null) {
      logger = Logger.getLogger(APILOGS);

      try {

        // This block configure the logger with handler and formatter
        // The boolean value is to append to an existing file if exists

        getFileHandler();

        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        // this removes the console log messages
        // logger.setUseParentHandlers(false);

      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return logger;
  }

  private static void getFileHandler() throws SecurityException, IOException {
    if (fh == null) {
      String path = Config.getStringProperty("logPath");
      String fileName = Config.getStringProperty("logFileName");
      String fullLogPath;

      if (path.toLowerCase().equals("default")) {
        path = System.getProperty("user.dir");
        path += "/Logs";
      }

      if (!path.endsWith("/")) {
        path += "/";
      }

      fullLogPath = path + fileName;
      fh = new FileHandler(fullLogPath, true);
    }
  }

  public static FileHandler getFh() throws SecurityException, IOException {
    if (fh == null) {
      getFileHandler();
    }
    return fh;
  }

}
