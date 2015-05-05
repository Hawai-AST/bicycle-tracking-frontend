package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.utility.AST;
import models.utility.PreparedJson;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;

@org.springframework.stereotype.Controller
public class BikeController extends Controller {
    public Result index() {
        JsonNode response = doRequest("http://localhost:8080/api/v1/bikes", null);
        System.out.println(response.toString());
        return ok(views.html.member.bikes.render(response.get("amount").asInt(), (ArrayNode)response.get("bikes")));
    }

    private JsonNode doRequest(String url, JsonNode jsonNode) {
        int responseTimeoutInMs = 10000;

        PreparedJson prepared = AST.preparedJson(url, "<temp_token>");

        F.Promise<JsonNode> jsonPromise = jsonNode != null ? prepared.post(jsonNode) : prepared.get();

        // TODO(Timmay): Create general pages for no-OK (200) responses and implement proper handling
        return jsonPromise.get(responseTimeoutInMs);
    }
}
