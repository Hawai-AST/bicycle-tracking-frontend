package models.utility.value;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Address {
    public String street;
    public String houseNumber;
    public String city;
    public String state;
    public String postcode;
    public String country;

    public JsonNode toJson() {
        return Json.toJson(this);
    }
}
