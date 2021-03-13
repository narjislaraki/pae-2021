package be.vinci.pae.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class APILogger {

  public static final String APILOGS = "APILogs";
  private static Logger logger;

  public static Logger getLogger() {
    if (logger == null) {
      logger = Logger.getLogger(APILOGS);
      FileHandler fh;

      String path = Config.getProperty("logPath");
      String fileName = Config.getProperty("logFileName");
      String fullLogPath;

      if (path.toLowerCase().equals("default")) {
        path = System.getProperty("user.dir");
        path += "/Logs";
      }

      if (!path.endsWith("/")) {
        path += "/";
      }

      fullLogPath = path + fileName;

      try {

        // This block configure the logger with handler and formatter
        // The boolean value is to append to an existing file if exists
        fh = new FileHandler(fullLogPath, true);
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
}
