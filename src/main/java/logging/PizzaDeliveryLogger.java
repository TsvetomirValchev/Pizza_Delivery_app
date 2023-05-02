package logging;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PizzaDeliveryLogger {
    private static FileHandler fileHandler;
    private static final Path logFilePath = Paths.get("errors.log");

    static {
        try {
            fileHandler = new FileHandler(logFilePath.toString(), true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            getLogger(PizzaDeliveryLogger.class.getName()).addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println(e + "Logger couldn't access file!");
        }
    }

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.addHandler(fileHandler);
        return logger;
    }
}
