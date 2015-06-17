package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.utility.AST;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        if (Authentication.isUserLoggedIn()) {
            return ok(views.html.member.index.render());
        } else {
            return ok(views.html.guest.index.render());
        }
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
