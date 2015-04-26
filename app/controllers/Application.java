package controllers;

// TODO REST implementation --> communicate with backend
// TODO store sessions


import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Registration;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.mvc.Result;
import views.html.index;
import views.html.maptest;
import views.html.signin;

import static play.mvc.Results.ok;

@Controller
public class Application {

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

        return ok(registration.toJson());
    }

    public Result authenticate() {
        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();

        //
        JsonNode json = Json.newObject()
                .put("grant-type", "password")
                .put("email", login.email)
                .put("code",  login.password);

        F.Promise<JsonNode> jsonPromise = WS.url("http://localhost:8080/api/v1/login")
                .setHeader("Client-Id", clientId)
                .setContentType("application/json")
                .post(json)
                .map(response -> response.asJson());

        return ok(jsonPromise.get(10000));

//        return WS
//                .url("http://localhost:8080/api/v1/login")
//                .setHeader("Client-Id", clientId)
//                .setContentType("application/json")
//                .post(json)
//                .map(actualResponse -> {
//                    if (actualResponse.getStatus() == 200) {
//                        JsonNode jsonResponse = actualResponse.asJson();
//                        session("email", jsonResponse.get("email").asText());
//                        session("token", jsonResponse.get("token").asText());
//                        return ok("doof");
////                        return ok(actualResponse.asJson());
//                    }
//
//                    return internalServerError();
//                })
//                ;
    }

    public Result maptest() {
        // TODO get current user address
        String currentUserAddress = "Berliner Tor 7, Hamburg";
        return ok(views.html.maptest.render(currentUserAddress));
    }
}