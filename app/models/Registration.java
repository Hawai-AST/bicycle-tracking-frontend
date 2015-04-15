package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

// TODO use Address type as attribute instead of addrX attributes
// TODO validate email and other validations

public class Registration {

    public String   firstName;
    public String   lastName;
    public String   birthdate;
    public String   gender;
    public String   addrStreet;
    public String   addrStreetNo;
    public String   addrZipCode;
    public String   addrLocation;
    public String   email;
    public String   password;
    public String   passwordCheck;

    public String validate() {
//        if (firstName.isEmpty()) {
//            return "Vorname muss angegeben werden";
//        }
//        if (lastName.isEmpty()) {
//            return "Nachname muss angegeben werden";
//        }
//        if (birthdate.isEmpty()) {
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
       return Json.newObject()
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("birthdate", birthdate)
                .put("gender", gender)
                .put("addrStreet", addrStreet)
                .put("addrStreetNo", addrStreetNo)
                .put("addrZipCode", addrZipCode)
                .put("addrLocation", addrLocation)
                .put("email", email)
                .put("code", password)
                .put("passwordCheck", passwordCheck);
    }

}