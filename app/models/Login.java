package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Login {

    public String email;
    public String password;

    public String validate() {
        if (email.isEmpty()) {
            return "E-Mail muss angegeben werden";
        }
        if (password.isEmpty()) {
            return "Passwort muss angegeben werden";
        }
        return null;
    }

    public JsonNode toJson() {

        return Json.newObject()
                .put("email", email)
                .put("password", password);
    }
}