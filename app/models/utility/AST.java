package models.utility;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import play.libs.ws.WSResponse;

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

    public static String getUserAddress(){
        String userAddress;

        JsonNode response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/user").get().map(WSResponse::asJson).get(10000);

        JsonNode street =  response.get("address").get("street");
        JsonNode housenumber = response.get("address").get("houseNumber");
        JsonNode city = response.get("address").get("city");
        JsonNode postcode = response.get("address").get("postcode");
        //concat the users address
        userAddress = street.asText() + " " + housenumber.asText()  + ", "  + city.asText() + ", "  + postcode.asText();

        return userAddress;
    }
}
