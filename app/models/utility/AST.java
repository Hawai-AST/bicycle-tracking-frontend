package models.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import play.libs.ws.WSResponse;

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
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
        ArrayNode arrayNode =  (ArrayNode) response.get("bikes");

        if (arrayNode == null){
            System.err.print("\n\n ----AST#46 didn't work----");
            options.put("bike1", "didn't work");
        } else {
            System.err.print("\n\n ----AST#49 hier----");
            options.put("bike1", "hier sollten Fahrräder stehen");
            System.out.print(arrayNode.toString());
        }
        return options;
    }
}

/*
  JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
  return ok(bikes.render(response.get("amount").asInt(), (ArrayNode) response.get("bikes")));
*/