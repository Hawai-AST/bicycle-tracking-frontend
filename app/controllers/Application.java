package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Registration;
import models.utility.AST;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.F;
import play.mvc.Result;
import views.html.index;
import views.html.maptest;
import views.html.signin;

import java.util.Iterator;

import static play.mvc.Controller.session;
import static play.mvc.Results.ok;

@Controller
public class Application {

    public Result index() { return ok(index.render()); }

    public Result signin() {
        return ok(signin.render(
                Form.form(Login.class),
                Form.form(Registration.class)
        ));
    }

    public Result register() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();
        Registration registration = form.get();

        JsonNode jsonResponse = doRequest("http://localhost:8080/api/v1/register", registration.toJson());

        return ok(jsonResponse);
    }

    public Result authenticate() {
        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();

        JsonNode jsonResponse = doRequest("http://localhost:8080/api/v1/login", login.toJson());
        storeValuesInSessionFrom(jsonResponse);

        return ok(jsonResponse);
    }

    public Result maptest() {
        return ok(maptest.render());
    }

    /**
     * Stores key value pairs from the json request in the session
     *
     * @param jsonNode json node from which to extract the values
     */
    private void storeValuesInSessionFrom(JsonNode jsonNode) {
        Iterator<String> nodeIt = jsonNode.fieldNames();

        while (nodeIt.hasNext()) {
            String field = nodeIt.next();
            // store key value pairs from request in session
            session(field, jsonNode.get(field).asText());
        }
    }

    /**
     * Runs procedures neccessary to perform a request and returns the response
     *
     * @param url      URL to call
     * @param jsonNode Acutal request content
     * @return Response of the request
     */
    private JsonNode doRequest(String url, JsonNode jsonNode) {
        int responseTimeoutInMs = 10000;

        F.Promise<JsonNode> jsonPromise = AST.preparedJson(url).post(jsonNode);

        // TODO(Timmay): Create general pages for no-OK (200) responses and implement proper handling
        return jsonPromise.get(responseTimeoutInMs);
    }

    /**
     * Detemines wheter the user is logged in or not
     *
     * @return true, if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return session("token") != null;
    }
}
