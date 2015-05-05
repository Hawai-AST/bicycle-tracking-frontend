package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Login;
import models.Registration;
import models.utility.AST;
import models.utility.PreparedJson;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.F;
import play.mvc.Result;

import java.util.Iterator;

import static play.mvc.Controller.session;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

@Controller
public class Application {

    public Result index() {
        if (isUserLoggedIn()) {
            return ok(views.html.member.index.render());
        } else {
            return ok(views.html.guest.index.render());
        }
    }

    public Result signin() {
        return ok(views.html.guest.signin.render(
                Form.form(Login.class),
                Form.form(Registration.class)
        ));
    }

    public Result signout() {
        session().clear();
        return ok(views.html.guest.index.render());
    }

    public Result register() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(views.html.guest.signin.render(
                    Form.form(Login.class),
                    form
            ));
        }

        Registration registration = form.get();

        JsonNode jsonResponse = doRequest("http://localhost:8080/api/v1/register", registration.toJson());
        storeValuesInSessionFrom(jsonResponse);

        return redirect("/");
    }

    public Result authenticate() {
        Form<Login> form = Form.form(Login.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(views.html.guest.signin.render(
                    form,
                    Form.form(Registration.class)
            ));
        }

        Login login = form.get();

        //JsonNode jsonResponse = doRequest("http://localhost:8080/api/v1/login", login.toJson());

        int responseTimeoutInMs = 10000;
        PreparedJson preparedJson = AST.preparedJson("http://localhost:8080/api/oauth/token");
        preparedJson.setContentType("application/x-www-form-urlencoded");
        F.Promise<JsonNode> jsonPromise = preparedJson.post(login.toJson());

        // TODO(Timmay): Create general pages for no-OK (200) responses and implement proper handling
        JsonNode jsonResponse = jsonPromise.get(responseTimeoutInMs);
        storeValuesInSessionFrom(jsonResponse);

        return redirect("/");
    }

    public Result maptest() {
        return ok(views.html.member.maptest.render());
    }

    /**
     * Profile view temporarily uses Registration model
     */
    public Result profile() {
        return ok(views.html.member.profile.render(
                Form.form(Registration.class)
        ));
    }

    /**
     * Updates profile according to form input
     * WARNING: Just a prototype which is subject to change
     *          Functionallity not tested !!!
     */
    public Result updateProfile() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();
        Registration registration = form.get();

        JsonNode jsonResponse = doRequest("http://localhost:8080/api/v1/updateProfile", registration.toJson());

        // temporary handling of response - dirty
        if (jsonResponse.get("status").equals("200")) {
            return redirect("/");
        } else {
            return badRequest(jsonResponse);
        }
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
     * Detemines whether the user is logged in or not
     *
     * @return true, if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return session("token") != null;
    }
}
