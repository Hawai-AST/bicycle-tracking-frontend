package models.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory Class
 */
public class AST {
    public static PreparedJson preparedJson(String url) {
        return new ASTPreparedJson(url);
    }

    public static Map<String, String> genderMap() {
        Map<String, String> options = new HashMap<>();
        options.put("", "Keine Angabe");
        options.put("male", "Männlich");
        options.put("female", "Weiblich");
        return options;
    }

    public static Map<String, String> genderMapInverse() {
        Map<String, String> inverseMap = new HashMap<>();
        for (Map.Entry<String,String> entry : genderMap().entrySet()) {
            inverseMap.put(entry.getValue(), entry.getKey());
        }
        return inverseMap;
    }

    /**
     *
     * @return bikes name of logged in user
     */
    public static Map<String, String> bikeMap() {
        Map<String, String> options = new HashMap<>();
        options.put("", "wähle");
        options.put("bike1", "Bike1");
        options.put("bike2", "Bike2");
        return options;
    }
}
