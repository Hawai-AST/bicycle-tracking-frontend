package models;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Waypoints;
import play.libs.Json;

import java.util.Date;

/**
 * Created by Louisa on 11.05.2015.
 */
public class TrackRegistration {

    public String name;
    public long bikeId;
    public int lengthInKm;
    public Date startAt;
    public Date finishedAt;
    public Waypoints waypoints;


    public JsonNode toJson(){
        return Json.toJson(this);
    }



}

