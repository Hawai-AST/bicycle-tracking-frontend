package models;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.Maps;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Louisa on 11.05.2015.
 */
public class TrackRegistration {

    public String name;
    public long bikeId;
    public int lengthInKm = 12;
    public Date startAt;
    public Date finishedAt;
    public ArrayList<Maps> waypoints = new ArrayList<>(2);


    public JsonNode toJson(){
        return Json.toJson(this);
    }



}

