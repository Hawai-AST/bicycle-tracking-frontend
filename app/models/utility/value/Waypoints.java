package models.utility.value;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

/**
 * Created by Louisa on 20.05.2015.
 */
public class Waypoints {
    public double latitude = 23.234234;
    public double longitude = 45.36345634;
    public String name = "huhu";

    public JsonNode toJson() {
        return Json.toJson(this);
    }

}
