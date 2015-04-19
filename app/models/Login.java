package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

public class Login {

    public String email;
    public String code;

    public JsonNode toJson() {
        return Json.newObject()
                .put("grant-type", "password")
                .put("email", this.email)
                .put("code", this.code);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (email.isEmpty()) {
            errors.add(new ValidationError("email", "E-Mail ungültig"));
        }
        if (code.isEmpty()) {
            errors.add(new ValidationError("code", "Passwort ungültig"));
        }

        return errors.isEmpty() ? null : errors;
    }
}
