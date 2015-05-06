package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.ChangePassword;
import models.ChangeUserCredentials;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.member.account;

import java.util.List;

import static play.data.Form.form;

/**
 * Created by Admin on 03.05.2015.
 */
public class Account extends Controller{
    final static Form<ChangePassword> changePasswordForm = form(ChangePassword.class);
    final static Form<ChangeUserCredentials> changeUserCredentialsForm = form(ChangeUserCredentials.class);

    public static Result showAccount() {
        return ok(account.render(changePasswordForm, changeUserCredentialsForm));
    }

    public static Result changePassword(){
        Form<ChangePassword> filledForm = changePasswordForm.bindFromRequest();
        if(filledForm.hasErrors()){
            for(String key : filledForm.errors().keySet()){
                List<ValidationError> currentError = filledForm.errors().get(key);
                for(ValidationError error : currentError){
                    flash("alert", error.message());
                }
            }
            flash("alert_type", "danger");
            return badRequest(account.render(filledForm, changeUserCredentialsForm));
        }else {
            ChangePassword changePassword = filledForm.get();
            JsonNode jsonResponse = Application.doRequest("http://localhost:8080/api/v1/user/password", changePassword.toJson());
            if(jsonResponse != null){
                flash("alert", "Passwort konnte nicht geändert werden");
                flash("alert_type", "danger");
                return ok(jsonResponse.asText());
            }else {
                flash("alert", "Passwort wurde erfolgreich geaendert");
                flash("alert_type", "success");
                return ok(account.render(filledForm, changeUserCredentialsForm));
            }

        }
    }

    public static Result changeUserCredentials(){
        Form<ChangeUserCredentials> filledForm = changeUserCredentialsForm.bindFromRequest();
        if(filledForm.hasErrors()){
            flash("alert", "Daten konnten nicht gespeichert werden");
            flash("alert_type", "danger");
            return badRequest(account.render(changePasswordForm, filledForm));
        }else {
            return ok(account.render(changePasswordForm, filledForm));
        }
    }
}
