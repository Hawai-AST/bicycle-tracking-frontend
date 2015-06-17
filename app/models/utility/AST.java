package models.utility;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import models.Bike;
import models.Track;
import play.libs.F;
import play.libs.ws.WSResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Factory Class
 */
public class AST {

    private final static int timeoutInSec = 30;

    public static PreparedJson preparedJson(String url) {
        return new ASTPreparedJson(url);
    }

    public static PreparedJson loginJson() {
        return new ASTPreparedJson();
    }

//    /**
//     * Runs procedures neccessary to perform a request and returns the response
//     *
//     * @param url      URL to call
//     * @param jsonNode Actual request content
//     * @return A pair consisting of the response and a status code of the request
//     */

    public static Pair<JsonNode, Integer> doPostRequest(String url, JsonNode jsonNode) {
        F.Promise<Pair<JsonNode, Integer>> promise = AST.preparedJson(url).post(jsonNode);
        return  promise.get(timeoutInSec, TimeUnit.SECONDS);
    }

    public static WSResponse doGetRequest(String url) {
        F.Promise<WSResponse> promise = AST.preparedJson(url).get();
        return  promise.get(timeoutInSec, TimeUnit.SECONDS);
    }

    public static Pair<JsonNode, Integer> performLogin(String paramters) {
        F.Promise<Pair<JsonNode, Integer>> promise = AST.loginJson().post(paramters);
        return  promise.get(timeoutInSec, TimeUnit.SECONDS);
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
        JsonNode response = doGetRequest(BackendConfig.bikesURL()).asJson();
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

        JsonNode response = doGetRequest(BackendConfig.routeURL()).asJson();
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
        JsonNode response = doGetRequest(BackendConfig.routeURL(id)).asJson();
        System.out.println("-------AST Z73 : this is the route" + response);
        Track retTrack = Track.fromJson(response);
        return response;
    }

    public static String getUserAddress() {
        String userAddress;

        WSResponse response = doGetRequest(BackendConfig.userURL());

        // TODO(Timmay): Error handling an dieser Stelle nur eingeschränkt sinnvoll, da nicht direkt im Controller-Kontext
        switch (response.getStatus()) {
            case 403: break; // Nicht eingeloggt
            case 500: break; // Server Error
            case 200: break; // Aktion erfolgreich
        }

        JsonNode jsonResponse = response.asJson();

        JsonNode street =  jsonResponse.get("address").get("street");
        JsonNode housenumber = jsonResponse.get("address").get("houseNumber");
        JsonNode city = jsonResponse.get("address").get("city");
        JsonNode postcode = jsonResponse.get("address").get("postcode");
        //concat the users address
        userAddress = street.asText() + " " + housenumber.asText()  + ", "  + city.asText() + ", "  + postcode.asText();

        return userAddress;
    }
}



/*
  JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
  return ok(bikes.render(response.get("amount").asInt(), (ArrayNode) response.get("bikes")));
*/
