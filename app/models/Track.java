package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Waypoints;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louisa on 11.05.2015.
 */
public class Track {

    @JsonIgnore
    private String id;
    public String name;
    public String bikeID;
    public String startAt;
    public String finishedAt;
    public Double lengthInKm;
    public List<Waypoints> waypoints;
    @JsonIgnore
    private String createdAt;
    @JsonIgnore
    private String updatedAt;

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public JsonNode toJson(){
        return Json.toJson(this);
    }

    public static Track fromJson(JsonNode node){
        return Json.fromJson(node, Track.class);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (name.isEmpty()) {
            errors.add(new ValidationError("name", "Bitte geben Sie den Namen der Strecke ein"));
        }
//        if (bike == null) {
//            errors.add(new ValidationError("bike", "Bitte geben Sie ihr Fahrrad ein"));
//        }
//        if (startAt == null) {
//            errors.add(new ValidationError("startAt", "Bitte geben Sie den Startzeitpunkt an"));
//        }
//        if (finishedAt == null) {
//            errors.add(new ValidationError("finishedAt", "Bitte geben Sie den Endzeitpunkt an"));
//        }
//        if (length == null) {
//            errors.add(new ValidationError("length", "Die Laenge konnte nicht berechnet werden"));
//        }
        return null; // errors.isEmpty() ? null : errors;
    }

}

