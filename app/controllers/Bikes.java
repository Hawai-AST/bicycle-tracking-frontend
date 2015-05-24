package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Bike;
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
    public static Result index() {
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().map(WSResponse::asJson).get(10000);
        List<Bike> bikeList = new ArrayList<Bike>();
        for(JsonNode bikeNode : response.get("bikes")) {
            bikeList.add(Bike.fromJson(bikeNode));
        }
        return ok(bikes.render(response.get("amount").asInt(), bikeList, form(Bike.class)));
    }

    public static Result add() {
        return ok(bike.render(form(Bike.class)));
    }

    public static Result update() {
        Form<Bike> result = form(Bike.class).bindFromRequest();
        Bike b = result.get();
        if(b.id == null || b.id.isEmpty() || b.id.equals("-1")) {
            JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bike").post(b.toJson()).get(10000);
        } else {
            JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bike/" + b.id).post(b.toJson()).get(10000);
        }

        return redirect("/bikes");
    }
}
