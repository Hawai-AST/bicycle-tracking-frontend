package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.TrackRegistration;
import models.utility.AST;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.helper.form;

import java.util.HashMap;

public class Maps extends Controller {
    // TODO (Marjan) impl correct return

    /**
     * Creates and saves route
     * @return
     */
//    public static Result create(JsonNode newRoute) {
//        // TODO (Marjan) post to BE properly
//        // TODO statt form.get() ajax json rein
//        Form<TrackRegistration> form = Form.form(TrackRegistration.class).bindFromRequest();
//        String currentUserAddress = AST.getUserAddress();
//
//        if (Application.doRequest("http://localhost:8080/api/v1/route", newRoute) != null) {
//            flash("alert", "Strecke konnte nicht gespeichert werden");
//            flash("alert_type", "danger");
//            return badRequest(Application.doRequest("http://localhost:8080/api/v1/route", newRoute));
//        } else {
//            flash("alert", "Strecke wurde gespeichert");
//            flash("alert_type", "success");
//            return ok(views.html.member.newtrack.render(form, currentUserAddress));
//        }
//    }
    public static Result create() {
        return ok("'test'");
    }

    /**
     * Updates specific route
     * @param id
     * @return
     */
    public static Result update(Integer id) {
        return ok("'test'");
    }

    /**
     * Shows all routes of the user
     * @return
     */
    public static Result index() {
        return ok("'test'");
    }

    /**
     * Shows one specific route
     * @param id
     * @return
     */
    public static Result show(Integer id) {
        return ok("'test'");
    }

    /**
     * Deletes one specific route
     * @param id
     * @return
     */
    public static Result delete(Integer id) {
        return ok("'test'");
    }
}