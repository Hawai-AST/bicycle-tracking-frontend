package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class BikeTypes {
    public int amount;
    public BikeType[] bikeTypes;

    public JsonNode toJson() {
        return Json.toJson(this);
    }

    public static BikeTypes fromJson(JsonNode node){
        return Json.fromJson(node, BikeTypes.class);
    }

    public static class BikeType {
        public String id;
        public String name;
        public String description;
        public int inspectionIntervalInWeeks;
    }
}
