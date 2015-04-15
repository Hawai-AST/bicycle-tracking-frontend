package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.util.ASTPreparedJson;
import models.Login;
import models.Registration;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Result;
import views.html.index;
import views.html.maptest;
import views.html.signin;

import java.util.Iterator;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

@Controller
public class Application {

    public Result index() {
        return ok(index.render());
    }

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
        final int responseTimeoutInMs = 10000;

        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();

        // TODO maybe create a utility method for creating json requests to refactor standard code
        JsonNode json = Json.newObject()
                .put("grant-type", "password")
                .put("email", login.email)
                .put("code", login.password);

        F.Promise<JsonNode> jsonPromise = new ASTPreparedJson(loginURL).post(json);

        // 'unwrap' JSON node from promise to obtain response content
        JsonNode jsonResponse = jsonPromise.get(responseTimeoutInMs);
        storeValuesInSessionFrom(jsonResponse);

        return ok(jsonResponse);

//        return ok(jsonResponse
//                        + "\n\n"
//                        + "SK-email: " + session("email")
//                        + "\n"
//                        + "SK-token: " + session("token")
//        );
    }

    public Result maptest() {
        return ok(maptest.render());
    }

    private void storeValuesInSessionFrom(JsonNode jsonNode) {
        Iterator<String> nodeIt = jsonNode.fieldNames();

        while (nodeIt.hasNext()) {
            String field = nodeIt.next();
            session(field, jsonNode.get(field).asText());
        }
    }
}