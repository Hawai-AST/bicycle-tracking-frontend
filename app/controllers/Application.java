package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Registration;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import views.html.index;
import views.html.maptest;
import views.html.signin;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

@Controller
public class Application {

    // ID needed for access to the backend API
    final String clientId = "DEV-101";

    public Result index() { return ok(index.render()); }

    public Result signin() {
        return ok(signin.render(
                "Anmelden",
                Form.form(Login.class),
                Form.form(Registration.class)
        ));
    }

    public Result register() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();
        Registration registration = form.get();

        // TODO create json request for registration (and perform a login?)
        // TODO refactor technical login code maybe to allow passing in login credentials after registration

        return ok(registration.toJson());
    }

    public Result authenticate() {
        final String loginURL = "http://localhost:8080/api/v1/login";

        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();

        // TODO maybe create a utility method for creating json requests to refactor stnadard code
        JsonNode json = Json.newObject()
                .put("grant-type", "password")
                .put("email", login.email)
                .put("code", login.password);

        // todo dito
        F.Promise<JsonNode> jsonPromise = WS.url(loginURL)
                .setHeader("Client-Id", clientId)
                .setContentType("application/json")
                .post(json)
                .map(WSResponse::asJson);

        // 'unwrap' JSON node from promise to obtain reponse content
        JsonNode jsonResponse = jsonPromise.get(10000);
        session("email", jsonResponse.get("email").asText());
        session("token", jsonResponse.get("token").asText());

        return ok(jsonResponse
                        + "\n\n"
                        + "SK-email: " + session("email")
                        + "\n"
                        + "SK-token: " + session("token")
        );
    }

    public Result maptest() { return ok(maptest.render()); }
}