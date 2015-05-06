package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 03.05.2015.
 */
public class ChangePassword {

    public String password;
    public String passwordCheck;

    public JsonNode toJson(){
        return Json.newObject().put("code", password);
    }

    public List<ValidationError> validate() {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (password.isEmpty()){
            validationErrors.add(new ValidationError("password", "Bitte geben sie ein Passwort ein"));
        }
        if(!password.equals(passwordCheck)){
            validationErrors.add(new ValidationError("password", "Die Passwoerter stimmen nicht ueberein"));
        }
        return validationErrors.isEmpty() ? null : validationErrors;
    }
}
