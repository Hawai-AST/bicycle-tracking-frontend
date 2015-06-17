package controllers;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import models.Bike;
import models.BikeTypes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.bike;
import views.html.member.bikes;

import java.util.ArrayList;
import java.util.List;

import static models.utility.AST.doGetRequest;
import static models.utility.AST.doPostRequest;
import static play.data.Form.form;

public class Bikes extends Controller {
    private static long lastCheck = 0;
    private static BikeTypes.BikeType[] lastResponse;
    private static final long HALF_HOUR_IN_MILLIS = 1800000;

    @RequiresLogin
    public static Result index() {
        JsonNode response = doGetRequest(BackendConfig.bikesURL()).asJson();
        checkBikeTypes();
        List<Bike> bikeList = new ArrayList<>();
        for (JsonNode bikeNode : response.get("bikes")) {
            bikeList.add(Bike.fromJson(bikeNode));
        }
        return ok(bikes.render(response.get("amount").asInt(), bikeList, form(Bike.class), lastResponse));
    }

    @RequiresLogin
    public static Result add() {
        checkBikeTypes();
        return ok(bike.render(form(Bike.class), lastResponse));
    }

    @RequiresLogin
    public static Result update() {
        Form<Bike> result = form(Bike.class).bindFromRequest();
        Bike bike = result.get();
        checkBikeTypes();
        if (bike.type == null || bike.type.isEmpty()) {
            flash("danger", "Der gegebene Typ existiert nicht");
            return index();
        } else {
            boolean found = false;
            for(BikeTypes.BikeType type : lastResponse) {
                if(type.id.equals(bike.type)) {
                    found = true;
                    break;
                }
            }

            if(!found) {
//            	You are using status code '200' with flashing, which should only be used with a redirect status!
                flash("danger", "Der gegebene Typ existiert nicht");
                return index();
            }
        }

        // TODO(Timmay): WTF? Und was soll jetzt noch mit der response passieren?
        Pair<JsonNode, Integer> response;
        if (bike.id == null || bike.id.isEmpty() || bike.id.equals("-1")) {
            response = doPostRequest(BackendConfig.bikeURL(), bike.toJson());
        } else {
            response = doPostRequest(BackendConfig.bikeURL(bike.id), bike.toJson());
        }
        return redirect("/bikes");
    }

    @RequiresLogin
    private static void checkBikeTypes() {
        if(System.currentTimeMillis() - lastCheck > HALF_HOUR_IN_MILLIS) {
            JsonNode response = doGetRequest(BackendConfig.bikeTypes()).asJson();
            lastResponse = BikeTypes.fromJson(response).bikeTypes;
            lastCheck = System.currentTimeMillis();
        }
    }

    @RequiresLogin
    public static String getBikeNameFromId(String inID) {
        for (BikeTypes.BikeType type : lastResponse) {
            if(type.id.equals(inID)) {
                return type.name;
            }
        }
        return "";
    }
}
