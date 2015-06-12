package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import config.BackendConfig;
import models.Bike;
import models.BikeTypes;
import models.utility.AST;
import play.data.Form;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.bikes;
import views.html.member.bike;

import java.util.ArrayList;
import java.util.List;

import static play.data.Form.form;

public class Bikes extends Controller {
    private static long lastCheck = 0;
    private static BikeTypes.BikeType[] lastResponse;
    private static final long HALF_HOUR_IN_MILLIS = 1800000;

    public static Result index() {
        JsonNode response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
        checkBikeTypes();
        List<Bike> bikeList = new ArrayList<Bike>();
        for(JsonNode bikeNode : response.get("bikes")) {
            bikeList.add(Bike.fromJson(bikeNode));
        }
        return ok(bikes.render(response.get("amount").asInt(), bikeList, form(Bike.class), lastResponse));
    }

    public static Result add() {
        checkBikeTypes();
        return ok(bike.render(form(Bike.class), lastResponse));
    }

    public static Result update() {
        Form<Bike> result = form(Bike.class).bindFromRequest();
        Bike b = result.get();
        checkBikeTypes();
        if(b.type == null || b.type.isEmpty()) {
            flash("alert", "Der gegebene Typ existiert nicht");
            flash("alert_type", "danger");
            return index();
        } else {
            boolean found = false;
            for(BikeTypes.BikeType type : lastResponse) {
                if(type.id.equals(b.type)) {
                    found = true;
                    break;
                }
            }

            if(!found) {
                flash("alert", "Der gegebene Typ existiert nicht");
                flash("alert_type", "danger");
                return index();
            }
        }

        if(b.id == null || b.id.isEmpty() || b.id.equals("-1")) {
            JsonNode response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/bike").post(b.toJson()).get(10000);
        } else {
            JsonNode response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/bike/" + b.id).post(b.toJson()).get(10000);
        }

        return redirect("/bikes");
    }

    private static void checkBikeTypes() {
        if(System.currentTimeMillis() - lastCheck > HALF_HOUR_IN_MILLIS) {
            JsonNode response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/biketypes").get().map(WSResponse::asJson).get(10000);
            lastResponse = BikeTypes.fromJson(response).bikeTypes;
            lastCheck = System.currentTimeMillis();
        }
    }
}
