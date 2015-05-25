package models;

import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Waypoints;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Louisa on 11.05.2015.
 */
public class TrackRegistration {

    public String name;
    public Long bike;
    public Date startAt;
    public Date finishedAt;
    public Double length;
    public List<Waypoints> waypoints;

    public JsonNode toJson(){
        return Json.toJson(this);
    }

    public static TrackRegistration fromJson(JsonNode node){
        return Json.fromJson(node, TrackRegistration.class);
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

