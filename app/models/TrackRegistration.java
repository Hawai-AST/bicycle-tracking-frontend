package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by Louisa on 11.05.2015.
 */
public class TrackRegistration {

    public String name;

    public JsonNode toJson(){
        return Json.toJson(this);
    }
}
