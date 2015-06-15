package models.utility;

import com.fasterxml.jackson.databind.JsonNode;
import models.Bike;
import models.Track;
import play.libs.ws.WSResponse;

import java.util.*;
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
        for (Map.Entry<String, String> entry : genderMap().entrySet()) {
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
        for (JsonNode bikeNode : response.get("bikes")) {
            bikeList.add(Bike.fromJson(bikeNode));
        }
        return bikeList;
    }

    /**
     * Gets the saved tracks
     * @return a list of the object Track. If no route was saved before
     * return list is empty.
     */
    public static List<Track> trackNameMap() {
        List<Track> trackList = new ArrayList<>();
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/route").get().map(WSResponse::asJson).get(10000);
        System.err.println("-------AST Z56 : this is the track response: " + response.toString());

        for (int i = 0; i < response.size(); i++){
            trackList.add(Track.fromJson(response.get(i)));
        }

        return trackList;
    }

    /**
     * Gets the track by id.
     * @param id is a UUID as a String.
     * @return a object of Track
     */
    public static JsonNode getTrack(String id){
        // TODO(Louisa / Marjan) use/get correct id
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/route/" + id).get().map(WSResponse::asJson).get(10000);
        System.out.println("-------AST Z73 : this is the route" + response);
        Track retTrack = Track.fromJson(response);
        return response;
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
