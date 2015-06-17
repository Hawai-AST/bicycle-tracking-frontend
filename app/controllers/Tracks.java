package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Bike;
import models.Track;
import models.utility.AST;
import models.utility.value.Waypoints;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Louisa on 11.05.2015.
 */
public class Tracks extends Controller {
    public static Form<Track> form = Form.form(Track.class);

    @RequiresLogin
    public static Result tracks() {
        Form<Track> form = Form.form(Track.class);
        String currentUserAddress = AST.getUserAddress();
        return ok(views.html.member.tracks.render(form, currentUserAddress));
    }

    @RequiresLogin
    public static Result newtracks() {

        System.out.println(form.toString());
        String currentUserAddress = AST.getUserAddress();
        return ok(views.html.member.newtrack.render(form, currentUserAddress));
    }

    /**
     *
     * @return a list of the users bikes
     */
    @RequiresLogin
    public static Map<String, String> getBikeOptions() {
        Map<String, String> option = new HashMap<>();

        List<Bike> bikes = AST.bikeMap();
        for (Bike bikeElem : bikes) {
             String id = bikeElem.id;
             String name = bikeElem.name;
             option.put(id, name);
        }
        return option;
    }

    @RequiresLogin
    public static Map<String, String> getTrackName() {
        Map<String, String> trackList = new HashMap<>();

        List<Track> tracks = AST.trackNameMap();

        for (Track trackElem : tracks) {
            String id = trackElem.getId();
            String trackname = trackElem.name;
            trackList.put(id, trackname);
        }

        return trackList;
    }

    @RequiresLogin
    public static Result getTrack(String id) {

        System.err.println("-------Tracks Controller Z.70 http post id: " + id);
//        Track retTrack = AST.getTrack(trackId);
//        System.out.println("-------Tracks Controller Z.72 this is the track: " + retTrack.toString());
        // TODO (Marjan) send Json-Object to Frontend
        JsonNode newJson = AST.getTrack(id);
        System.out.println("-------Tracks Controller Z.72 this is the track: " + newJson.toString());
        return ok(newJson);
    }

    @RequiresLogin
    public static Result saveTracks() {
        Form<Track> formSaved = form.bindFromRequest();
        System.out.println("this is the form " + formSaved.toString());

        Map<String, String[]> map = request().body().asFormUrlEncoded();
        String[] checkedVal = map.get("waypointsList"); // get selected topics
        List<Waypoints> waypoints = new ArrayList<>();
        try {
            waypoints = new ObjectMapper().readValue(checkedVal[0], new TypeReference<List<Waypoints>>(){});
            System.out.println("This are the waypoints: " + waypoints.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Track registration = formSaved.get();
        registration.waypoints = waypoints;

        //System.out.println("this is the bike " + checkedVal.length);
//        System.out.println("this is the form " + form.get().waypoints);

        String currentUserAddress = AST.getUserAddress();
//        if (Application.doRequest("http://localhost:8080/api/v1/route", form.get().toJson()) != null) {
//            flash("alert", "Strecke konnte nicht gespeichert werden");
//            flash("alert_type", "danger");
//            return badRequest(Application.doRequest("http://localhost:8080/api/v1/route", form.get().toJson()));
//        } else {
//            flash("alert", "Strecke wurde gespeichert");
//            flash("alert_type", "success");
//            return ok(views.html.member.newtrack.render(form, currentUserAddress));
//        }
        //return ok(registration.toJson());
        return ok(views.html.member.newtrack.render(form, currentUserAddress));
    }
}
