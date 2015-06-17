package controllers;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

import static models.utility.AST.doPostRequest;

public class Maps extends Controller {
    // TODO(Marjan) impl correct return

    /**
     * Creates and saves route
     * @return
     */
//    public static Result create(JsonNode newRoute) {
//        // TODO(Marjan) post to BE properly
//        // TODO(blub)statt form.get() ajax json rein
//        Form<Track> form = Form.form(Track.class).bindFromRequest();
//        String currentUserAddress = AST.getUserAddress();
//
//        if (doRequest("http://localhost:8080/api/v1/route", newRoute) != null) {
//            flash("alert", "Strecke konnte nicht gespeichert werden");
//            flash("alert_type", "danger");
//            return badRequest(doRequest("http://localhost:8080/api/v1/route", newRoute));
//        } else {
//            flash("alert", "Strecke wurde gespeichert");
//            flash("alert_type", "success");
//            return ok(views.html.member.newtrack.render(form, currentUserAddress));
//        }
//    }
    @RequiresLogin
    public static Result create() {
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        String result = request.get("data")[0];
        System.out.println(request);
        System.out.println(result);
        System.out.println(Json.parse(result));

        Pair<JsonNode, Integer> response = doPostRequest(BackendConfig.routeURL(), Json.parse(result));
//        System.out.println("----Map Z 46: this is what we are sending: " + jsonResponse.toString());

        // TODO(Timmay): Error handling
        switch (response.second()) {
            case 403: flash("danger", response.second() + ": " + "Nicht eingeloggt"); break;
            case 500: flash("danger", response.second() + ": " + "Server Error"); break;
            case 200: flash("success", "200: Strecke erfolgreich angelegt!"); break;
        }

        JsonNode jsonResponse = response.first();

        return ok(jsonResponse.asText());
    }

    /**
     * Updates specific route
     * @param id
     * @return
     */
    @RequiresLogin
    public static Result update(String id) {
        System.err.println("----Map Z. 51 you are in the route update methode with this trackId " + id);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        String result = request.get("data")[0];
        System.out.println("----Maps Z. 54 this is what we send after update track: " + Json.parse(result).toString());

        Pair<JsonNode, Integer> response = doPostRequest(BackendConfig.routeURL(id), Json.parse(result));

        // TODO(Timmay): Error handling
        switch (response.second()) {
            case 403: flash("danger", response.second() + ": " + "Nicht eingeloggt"); break;
            case 404: flash("danger", response.second() + ": " + "ID nicht gefunden"); break;
            case 500: flash("danger", response.second() + ": " + "Server Error"); break;
            case 200: flash("success", "200: Strecke aktualisiert!"); break;
        }

        flash("info", "route/id #update: " + response.second().toString());

        JsonNode jsonResponse = response.first();

        return ok(jsonResponse.toString());
    }

    /**
     * Shows all routes of the user
     * @return
     */
    @RequiresLogin
    public static Result index() {
        return ok("'test_index'");
    }

    /**
     * Shows one specific route
     * @param id
     * @return
     */
    @RequiresLogin
    public static Result show(String id) {
        return ok("'test_show'");
    }

    /**
     * Deletes one specific route
     * @param id
     * @return
     */
    @RequiresLogin
    public static Result delete(String id) {
        return ok("'test_delete'");
    }
}
