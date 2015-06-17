package controllers;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import models.ChangePassword;
import models.ChangeUserCredentials;
import models.utility.AST;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.account;

import java.util.List;

import static models.utility.AST.doPostRequest;
import static play.data.Form.form;

public class Account extends Controller {
    final static Form<ChangePassword> changePasswordForm = form(ChangePassword.class);
    final static Form<ChangeUserCredentials> changeUserCredentialsForm = form(ChangeUserCredentials.class);

    @RequiresLogin
    public static Result showAccount() {
        WSResponse response = AST.doGetRequest(BackendConfig.userURL());

        // TODO(Timmay): error handling
        switch (response.getStatus()) {
            case 403: flash("danger", response.getStatus() + ": " + "Nicht eingeloggt"); break;
            case 500: flash("danger", response.getStatus() + ": " + "Server Error"); break;
        }

        JsonNode jsonResponse = response.asJson();
        ChangeUserCredentials changeUserCredentials = ChangeUserCredentials.fromJson(jsonResponse);
        Form<ChangeUserCredentials> prefilledUserCredentialsForm = changeUserCredentialsForm.fill(changeUserCredentials);

        return ok(account.render(changePasswordForm, prefilledUserCredentialsForm));
    }

    @RequiresLogin
    public static Result changePassword() {
        Form<ChangePassword> filledForm = changePasswordForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            for (String key : filledForm.errors().keySet()) {
                List<ValidationError> currentError = filledForm.errors().get(key);
                for (ValidationError error : currentError) {
                    flash("danger", error.message());
                }
            }
            return badRequest(account.render(filledForm, changeUserCredentialsForm));
        } else {
            ChangePassword changePassword = filledForm.get();
            Pair<JsonNode, Integer> response = doPostRequest(BackendConfig.userPasswordURL(), changePassword.toJson());

            // TODO(Timmay): error handling
            switch (response.second()) {
                case 403: flash("danger", response.second() + ": " + "Nicht eingeloggt"); break;
                case 400: flash("danger", response.second() + ": " + "Password kann nicht gesetzt werden"); break;
                case 500: flash("danger", response.second() + ": " + "Server Error"); break;
                case 200: flash("success", "200: Passwort erfolgreich geändert!"); break;
            }

            flash("info", "changePassword " + response.second().toString());

            JsonNode jsonResponse = response.first();

            if (jsonResponse != null) {
//                flash("danger", "Passwort konnte nicht geändert werden");
                return ok(jsonResponse.asText());
            } else {
//                flash("success", "Passwort wurde erfolgreich geändert");
                return ok(account.render(filledForm, changeUserCredentialsForm));
            }

        }
    }

    @RequiresLogin
    public static Result changeUserCredentials() {
        Form<ChangeUserCredentials> filledForm = changeUserCredentialsForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            flash("danger", "Daten konnten nicht gespeichert werden");
            return badRequest(account.render(changePasswordForm, filledForm));
        } else {
            Pair<JsonNode, Integer> response = doPostRequest(BackendConfig.userURL(), filledForm.get().toJson());

            if (response != null) {

                // TODO(Timmay): error handling
                switch (response.second()) {
                    case 400: flash("danger", response.second() + ": " + "Eines der Inputs war nicht im richtigen Format"); break;
                    case 403: flash("danger", response.second() + ": " + "Nicht eingeloggt"); break;
                    case 409: flash("danger", response.second() + ": " + "Customer ID existiert bereits"); break;
                    case 500: flash("danger", response.second() + ": " + "Server Error"); break;

                    case 200: flash("success", "200: Benutzerdaten erfolgreich geändert!"); break;
                }

                flash("info", "changeUserCredentials: " + response.second().toString());

                JsonNode jsonResponse = response.first();

//                flash("danger", "Daten konnten nicht gespeichert werden");
                return badRequest(account.render(changePasswordForm, filledForm));//account.render(changePasswordForm, filledForm));
            } else {
//                flash("success", "Daten wurden geändert");
                return ok(account.render(changePasswordForm, filledForm));
            }
        }
    }
}
