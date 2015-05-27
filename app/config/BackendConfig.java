package config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BackendConfig {
    private static Config config;
    private static String backendURL;

    public static void load() {
        config = ConfigFactory.load();
        backendURL = config.getString("backend.url");
    }

    public static String backendURL() {
        return backendURL;
    }
}
