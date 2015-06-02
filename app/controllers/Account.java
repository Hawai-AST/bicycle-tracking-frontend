package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import models.ChangePassword;
import models.ChangeUserCredentials;
import models.utility.AST;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.F;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.account;

import java.util.List;

import static play.data.Form.form;

public class Account extends Controller {
    final static Form<ChangePassword> changePasswordForm = form(ChangePassword.class);
    final static Form<ChangeUserCredentials> changeUserCredentialsForm = form(ChangeUserCredentials.class);

    @RequiresLogin
    public static Result showAccount() {
        F.Promise<JsonNode> response = AST.preparedJson(BackendConfig.backendURL() + "/api/v1/user").get().map(WSResponse::asJson);
        JsonNode node = response.get(10000);
        ChangeUserCredentials changeUserCredentials = ChangeUserCredentials.fromJson(node);
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
                    flash("alert", error.message());
                }
            }
            flash("alert_type", "danger");
            return badRequest(account.render(filledForm, changeUserCredentialsForm));
        } else {
            ChangePassword changePassword = filledForm.get();
            JsonNode jsonResponse = Application.doRequest(BackendConfig.backendURL() + "/api/v1/user/password", changePassword.toJson());
            if (jsonResponse != null) {
                flash("alert", "Passwort konnte nicht geändert werden");
                flash("alert_type", "danger");
                return ok(jsonResponse.asText());
            } else {
                flash("alert", "Passwort wurde erfolgreich geändert");
                flash("alert_type", "success");
                return ok(account.render(filledForm, changeUserCredentialsForm));
            }

        }
    }

    @RequiresLogin
    public static Result changeUserCredentials() {
        Form<ChangeUserCredentials> filledForm = changeUserCredentialsForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            flash("alert", "Daten konnten nicht gespeichert werden");
            flash("alert_type", "danger");
            return badRequest(account.render(changePasswordForm, filledForm));
        } else {
            if (Application.doRequest(BackendConfig.backendURL() + "/api/v1/user", filledForm.get().toJson()) != null) {
                flash("alert", "Daten konnten nicht gespeichert werden");
                flash("alert_type", "danger");
                return badRequest(Application.doRequest(BackendConfig.backendURL() + "/api/v1/user", filledForm.get().toJson()));//account.render(changePasswordForm, filledForm));
            } else {
                flash("alert", "Daten wurden geändert");
                flash("alert_type", "success");
                return ok(account.render(changePasswordForm, filledForm));
            }
        }
    }
}
