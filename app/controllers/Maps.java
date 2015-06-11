package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

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
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        String result = request.get("data")[0];
        System.out.println(request);
        System.out.println(result);
        System.out.println(Json.parse(result));

        JsonNode jsonResponse = Application.doRequest("http://localhost:8080/api/v1/route", Json.parse(result));
//        System.out.println("----Map Z 46: this is what we are sending: " + jsonResponse.toString());
        return ok(jsonResponse.asText());
    }

    /**
     * Updates specific route
     * @param id
     * @return
     */
    public static Result update(String id) {
        System.err.println("----Map Z. 51 you are in the route update methode with this trackId " + id);
        Map<String, String[]> request = request().body().asFormUrlEncoded();
        String result = request.get("data")[0];
        System.out.println("----Maps Z. 54 this is what we send after update track: " + Json.parse(result).toString());

        JsonNode jsonResponse = Application.doRequest("http://localhost:8080/api/v1/route/" + id, Json.parse(result));
//
        return ok(jsonResponse.toString());
    }

    /**
     * Shows all routes of the user
     * @return
     */
    public static Result index() {
        return ok("'test_index'");
    }

    /**
     * Shows one specific route
     * @param id
     * @return
     */
    public static Result show(String id) {
        return ok("'test_show'");
    }

    /**
     * Deletes one specific route
     * @param id
     * @return
     */
    public static Result delete(String id) {
        return ok("'test_delete'");
    }
}
