package be.howest.ti.game.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private Config() { /* utility class */ }

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private static final Properties properties = new Properties();

    static {
        properties.setProperty("server.port", "8080");
        properties.setProperty("spec.url", "splendor-api-spec.yaml");
        try (InputStream input = Config.class.getResourceAsStream("/config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not load config.properties", e);
        }
    }

    public static String getString(String key) {
        String value = "server.port".equals(key) ? System.getenv("PORT") : null;
        if (value != null && !value.isBlank()) {
            return value;
        }
        return properties.getProperty(key);
    }

    public static int getInteger(String key) {
        return Integer.parseInt(getString(key));
    }


}
