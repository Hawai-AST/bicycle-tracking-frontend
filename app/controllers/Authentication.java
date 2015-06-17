package controllers;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import models.Login;
import models.Registration;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static controllers.Application.storeValuesInSessionFrom;
import static models.utility.AST.doPostRequest;
import static models.utility.AST.performLogin;

public class Authentication extends Controller {

    public static Result signup() {
        return ok(views.html.guest.signup.render(Form.form(Registration.class)));
    }

    @RequiresLogin
    public static Result signout() {
        // TODO(Timmay): Inform backend about user log off
        session().clear();
        flash("alert", "Sie haben sich erfolgreich ausgeloggt!");
        flash("alert_type", "success");
        return redirect("/");
    }

    /**
     * WARNING: Contains temporary solutions in order to supply a rudimental error handling
     */
    public static Result register() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();

        // TODO(Timmay): signup Form bei fehlgeschlagender Registrierung nochmal rendern!
        // TODO(Timmay): ACHTUNG Binding des Models bei erneutem Versuch, damit bisherige Eingaben nicht geloescht werden

        if (form.hasErrors()) {
            return badRequest(views.html.guest.signup.render(form));
        }

        Registration registration = form.get();

        Pair<JsonNode, Integer> response = doPostRequest(BackendConfig.registerURL(), registration.toJson());

        // TODO(Timmay): error handling
        switch (response.second()) {
            case 400: flash("danger", response.second() + ": " + "Eines der Inputs war nicht im richtigen Format"); break;
            case 404: flash("danger", response.second() + ": " + "Kundennr existiert nicht"); break;
            case 409: flash("danger", response.second() + ": " + "Benutzer existiert bereits"); break;
            case 500: flash("danger", response.second() + ": " + "Server Error"); break;
            case 200: flash("success", "200: Registrierung erfolgreich. Sie können sich nun eingeloggen!"); break;
        }

        if (flash("danger") != null) {
            return badRequest(views.html.guest.signup.render(form));
        }

//        JsonNode jsonResponse = response.first();
//
//        if (jsonResponse.get("email") != null) {
//            if (jsonResponse.get("email").asText().equals(registration.email)) {
//                flash("success", "Sie haben sich erfolgreich registriert!");
//                storeValuesInSessionFrom(jsonResponse);
//            }
//        }

        return redirect("/");
    }

    public static Result authenticate() {
        // Determine which button of the form was pressed
        String[] postAction = request().body().asFormUrlEncoded().get("action");
        if (postAction == null || postAction.length == 0) {
            return badRequest("You must provide a valid action");
        } else {
            String action = postAction[0];
            switch (action) {
                case "signup":
                    return redirect("/signup");
                case "auth":
                    // Logger.info("Action auth received");
                    break;
                default:
                    return badRequest("Bad Action: " + action);
            }
        }

        Form<Login> form = Form.form(Login.class).bindFromRequest();

        if (form.hasErrors()) {
            flash("danger", "Anmeldung fehlgeschlagen. Bitte geben Sie E-Mail UND Passwort an!");
            return redirect("/");
        }

        Login login = form.get();

        Pair<JsonNode, Integer> response = null;

        try {
            response = performLogin(
                    "username=" + URLEncoder.encode(login.email, "UTF-8") + "&" +
                    "grant_type=password&" +
                    "password=" + URLEncoder.encode(login.code, "UTF-8") + "&" +
                    "scope=read write");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // TODO(Timmay): Error handling
        String danger = null;

        switch (response.second()) {
            case 400: danger = "Eines der Inputs war nicht im richtigen Format"; break;
            case 401: danger = "Falscher Autorisierungscode"; break;
            case 404: danger = "Benutzer existiert nicht" ; break;
            case 500: danger = "Server Error" ; break;
            case 200: flash("success", "200: Anmeldung erfolgreich! Herzlich willkommen!"); break;
        }

        if (danger != null) {
            flash("danger", response.second() + ": " + danger);
        }

        JsonNode jsonResponse = response.first();

        if (jsonResponse.get("access_token") != null) {
            storeValuesInSessionFrom(jsonResponse);
            session("access_token", session("access_token"));
            session("email", login.email);
        }

        return redirect("/");
    }

    /**
     * Checks whether the user is logged in or not
     *
     * @return true, if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return session("access_token") != null;
    }
}
