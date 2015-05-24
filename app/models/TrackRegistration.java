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
    public long bike;
    public Date startAt;
    public Date finishedAt;
    public long length;
    public String waypoints;

    public JsonNode toJson(){
        return Json.toJson(this);
    }



}

