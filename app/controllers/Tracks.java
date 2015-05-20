package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.TrackRegistration;
import models.utility.AST;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.ws.WSResponse;

import java.util.HashMap;
import java.util.Map;

import static play.data.Form.form;


/**
 * Created by Louisa on 11.05.2015.
 */
public class Tracks extends Controller {

    public static Result tracks() {
        return ok(views.html.member.tracks.render());
    }

    public static Result newtracks(){
        Form<TrackRegistration> form = Form.form(TrackRegistration.class).bindFromRequest();
        return ok(views.html.member.newtrack.render(form));
    }

    /**
     *
     * @return a list of the users' bikes
     */
    public static Map<String, String> getBikeOptions() {
        return AST.bikeMap();
    }



    public static Result saveTracks(){
        Form<TrackRegistration> form = Form.form(TrackRegistration.class).bindFromRequest();
        System.out.println(form.toString());
//        if (Application.doRequest("http://localhost:8080/api/v1/route", form.get().toJson()) != null) {
//            flash("alert", "Strecke konnte nicht gespeichert werden");
//            flash("alert_type", "danger");
//            return badRequest(Application.doRequest("http://localhost:8080/api/v1/route", form.get().toJson()));
//        } else {
//            flash("alert", "Strecke wurde gespeichert");
//            flash("alert_type", "success");
//            return ok(views.html.member.newtrack.render(form));
//        }
        return ok(views.html.member.newtrack.render(form));
    }
}
