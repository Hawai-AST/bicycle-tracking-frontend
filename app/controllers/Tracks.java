package controllers;

import models.Registration;
import models.TrackRegistration;
import models.utility.AST;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;


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


}

