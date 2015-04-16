package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Address;
import play.libs.Json;

// TODO use Address type as attribute instead of addrX attributes
// TODO validate email and other validations

public class Registration {

    public String firstname;
    public String name;
    public String birthday;
    public String gender;
    public Address address;
    public String email;
    public String customerid;
    public String password;
    @JsonIgnore
    public String passwordCheck;

    public String validate() {
//        if (firstname.isEmpty()) {
//            return "Vorname muss angegeben werden";
//        }
//        if (name.isEmpty()) {
//            return "Nachname muss angegeben werden";
//        }
//        if (birthday.isEmpty()) {
//            return "Geburtsdatum muss angegeben werden";
//        }
//        if (addrStreet.isEmpty()) {
//            return "Straße muss angegeben werden";
//        }
//        if (addrStreetNo.isEmpty()) {
//            return "Hausnummer muss angegeben werden";
//        }
//        if (addrZipCode.isEmpty()) {
//            return "Postleitzahl muss angegeben werden";
//        }
//        if (addrLocation.isEmpty()) {
//            return "Ort muss angegeben werden";
//        }
//        if (email.isEmpty()) {
//            return "E-Mail muss angegeben werden";
//        }
//        if (code.isEmpty()) {
//            return "Passwort muss angegeben werden";
//        }
//        if (passwordCheck.isEmpty()) {
//            return "Passwort muss erneut angegeben werden";
//        }
//
//        if (!code.equals(passwordCheck)) {
//            return "Die Passwörter stimmen nicht überein!";
//        }

        return null;
    }

    public JsonNode toJson() {
        return Json.toJson(this);
    }

}