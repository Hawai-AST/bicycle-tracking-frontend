package config;

import akka.japi.Pair;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BackendConfig {
    private static Config config;
    private static String backendURL;
    private static String currentAPI;
    private static Pair<String, String> authHeader;

    public static void load() {
        config = ConfigFactory.load();
        backendURL = config.getString("backend.url");
        currentAPI = config.getString("backend.api");
        authHeader = new Pair(
                config.getString("auth.header.key"),
                config.getString("auth.header.value"));
    }

    private static String backendURL() {
        return backendURL;
    }

    private static String currentApiURL() {
        return currentAPI;
    }

    private static String baseURL() {
        return backendURL() + "/" + currentApiURL() + "/";
    }

    public static Pair<String, String> authHeader() {
        return authHeader;
    }

    public static String userURL() {
        return baseURL() + "user";
    }

    public static String userPasswordURL() {
        return userURL() + "/password";
    }

    public static String routeURL() {
        return baseURL() + "route";
    }

    public static String routeURL(String id) {
        return routeURL() + "/" + id;
    }

    public static String bikeURL() {
        return baseURL() + "bike";
    }

    public static String bikeURL(String id) {
        return bikeURL() + "/" + id;
    }

    public static String bikesURL() {
        return baseURL() + "bikes";
    }

    public static String registerURL() {
        return baseURL() + "register";
    }

    public static String bikeTypes() {
        return baseURL() + "biketypes";
    }

    public static String oAuthToken() {
        return backendURL() + "/oauth/token";
    }
}
