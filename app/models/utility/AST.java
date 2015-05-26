package models.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Bike;
import models.TrackRegistration;
import play.libs.ws.WSResponse;

import java.io.IOException;
import java.util.*;

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
    public static List<Bike> bikeMap() {
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
        List<Bike> bikeList = new ArrayList<Bike>();
        for(JsonNode bikeNode : response.get("bikes")) {
            bikeList.add(Bike.fromJson(bikeNode));
        }
        return bikeList;
    }

    public static List<TrackRegistration> trackNameMap() {
        List<TrackRegistration> trackList = new ArrayList<>();
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/route").get().map(WSResponse::asJson).get(10000);
        System.err.println("-------AST Z55 : this is the track response: " + response.toString());

        try {
            List<TrackRegistration> myObjects = new ObjectMapper().readValue(response.toString(), new TypeReference<List<TrackRegistration>>(){});
            System.err.println("-------AST Z59 : this is the track myObjects: " + myObjects);
        } catch (IOException e) {
            System.err.println("-------AST Z61 : did not work");
            e.printStackTrace();
        }

//        for(JsonNode trackNode : response.get("data")){
//            trackList.add(TrackRegistration.fromJson(trackNode));
//        }
//        ArrayNode arrayNode =  (ArrayNode) response.get("name");
        return trackList;
    }


    public static String getTrack(){
        String s = "";
        // TODO (Louisa / Marjan) use/get correct id
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/route").get().map(WSResponse::asJson).get(10000);
//        System.out.println("these are the routes" + response);
        return s;
    }

    public static String getUserAddress(){
        String userAddress;

        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/user").get().map(WSResponse::asJson).get(10000);

        JsonNode street =  response.get("address").get("street");
        JsonNode housenumber = response.get("address").get("houseNumber");
        JsonNode city = response.get("address").get("city");
        JsonNode postcode = response.get("address").get("postcode");
        //concat the users address
        userAddress = street.asText() + " " + housenumber.asText()  + ", "  + city.asText() + ", "  + postcode.asText();

        return userAddress;
    }
}



/*
  JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
  return ok(bikes.render(response.get("amount").asInt(), (ArrayNode) response.get("bikes")));
*/