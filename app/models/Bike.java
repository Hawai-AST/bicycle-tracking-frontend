package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

public class Bike {
    @JsonIgnore
    public String id;
    @JsonIgnore
    public String name;
    public String type;
    public String purchaseDate;
    public String nextMaintenance;
    public double mileage;
    public String salesLocation;
    public int frameNumber;

    // Have to explicitly declare setter to un-ignore id when reading from server.
    @JsonProperty
    public void setId(String inId) {
        this.id = inId;
    }

    public JsonNode toJson() {
        return Json.toJson(this);
    }

    public static Bike fromJson(JsonNode node){
        return Json.fromJson(node, Bike.class);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (type == null || type.isEmpty()) {
            errors.add(new ValidationError("type", "Bitte geben sie den Fahrradtyp an"));
        }
        if (purchaseDate == null) {
            errors.add(new ValidationError("purchaseDate", "Bitte geben Sie das Kaufdatum an"));
        }
        if (nextMaintenance == null) {
            errors.add(new ValidationError("nextMaintenance", "Bitte geben Sie das Datum der nächsten Wartung an"));
        }
        if (frameNumber < 1) {
            errors.add(new ValidationError("frameNumber", "Bitte geben Sie eine Ramennummer an"));
        }

        return errors.isEmpty() ? null : errors;
    }
}
