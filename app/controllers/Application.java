package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.utility.AST;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        if (isUserLoggedIn()) {
            return ok(views.html.member.index.render());
        } else {
            return ok(views.html.guest.index.render());
        }
    }

    /**
     * Runs procedures neccessary to perform a request and returns the response
     *
     * @param url      URL to call
     * @param jsonNode Acutal request content
     * @return Response of the request
     */
    public static JsonNode doRequest(String url, JsonNode jsonNode) {
        int responseTimeoutInMs = 10000;

        F.Promise<JsonNode> jsonPromise = AST.preparedJson(url).post(jsonNode);

        // TODO(Timmay): Create general pages for no-OK (200) responses and implement proper handling
        return jsonPromise.get(responseTimeoutInMs);
    }

    /**
     * Stores key value pairs from the json request in the session
     *
     * @param jsonNode json node from which to extract the values
     */
    public static void storeValuesInSessionFrom(JsonNode jsonNode) {
        Iterator<String> nodeIt = jsonNode.fieldNames();

        while (nodeIt.hasNext()) {
            String field = nodeIt.next();
            // store key value pairs from request in session
            session(field, jsonNode.get(field).asText());
        }
    }

    /**
     * Checks whether the user is logged in or not
     *
     * @return true, if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return session("access_token") != null;
    }

    /**
     * @return Map of options for a gender choice field
     */
    public static Map<String, String> getGenderOptions() {
        return AST.genderMap();
    }

    /**
     * @return Inversed version of options for a gender choice field
     */
    public static Map<String, String> getGenderOptionsInverse() {
        return AST.genderMapInverse();
    }

    /**
     * Directly returns the value of the requested key in the flash object
     *
     * @param key key to which the value should be returned
     * @return value of the requested key
     */
    public static String getFlash(String key) {
        return flash(key);
    }
}
