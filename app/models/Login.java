package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

public class Login {

    public String email;
    public String code;

    public String validate() {
        if (email.isEmpty()) {
            return "E-Mail muss angegeben werden";
        }
        if (code.isEmpty()) {
            return "Passwort muss angegeben werden";
        }
        return null;
    }

    public JsonNode toJson() {
        return Json.newObject()
                .put("grant-type", "password")
                .put("email", this.email)
                .put("code", this.code);
    }
}