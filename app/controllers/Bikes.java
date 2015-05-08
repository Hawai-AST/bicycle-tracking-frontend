package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.utility.AST;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.bikes;

public class Bikes extends Controller {
    public static Result index() {
        JsonNode response = AST.preparedJson("http://localhost:8080/api/v1/bikes").get().get(10000);
        return ok(bikes.render(response.get("amount").asInt(), (ArrayNode) response.get("bikes")));
    }
}
