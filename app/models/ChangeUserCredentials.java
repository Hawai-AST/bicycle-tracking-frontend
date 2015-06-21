package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.EMail;
import models.utility.value.Address;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeUserCredentials {
    public String firstName;
    public String name;
    public String birthdate;
    public String gender;
    public Address address;
    public EMail mailAddress;
    public String id;
    public int version;

    public JsonNode toJson() {
        return Json.toJson(this);
    }

    public static ChangeUserCredentials fromJson(JsonNode node){
        ChangeUserCredentials tmp = Json.fromJson(node, ChangeUserCredentials.class);
        DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        tmp.birthdate = DEFAULT_FORMAT.format(new Date(Long.valueOf(tmp.birthdate)));
        return tmp;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (firstName.isEmpty()) {
            errors.add(new ValidationError("firstname", "Bitte geben Sie Ihren Vornamen an"));
        }
        if (name.isEmpty()) {
            errors.add(new ValidationError("name", "Bitte geben Sie Ihren Nachnamen an"));
        }
        if (address.city.isEmpty()) {
            errors.add(new ValidationError("address.city", "Bitte geben Sie Ihre Stadt an"));
        }
        if (address.country.isEmpty()) {
            errors.add(new ValidationError("address.country", "Bitte geben Sie Ihr Land an"));
        }
        if (address.houseNumber.isEmpty()) {
            errors.add(new ValidationError("address.houseNumber", "Bitte geben Sie Ihre Hausnummer an"));
        }
        if (address.postcode.isEmpty()) {
            errors.add(new ValidationError("address.postcode", "Bitte geben Sie Ihre Postleitzahl an"));
        }
        if (!address.postcode.matches("(([a-z]|[A-Z])([a-z]|[A-Z]))?[0-9]+")) {
            errors.add(new ValidationError("address.postcode", "Es sind nur Zahlen erlaubt"));
        }
        if (address.state.isEmpty()) {
            errors.add(new ValidationError("address.state", "Bitte geben Sie Ihr Bundesland an"));
        }
        if (address.street.isEmpty()) {
            errors.add(new ValidationError("address.street", "Bitte geben Sie Ihre Straße an"));
        }
        if (mailAddress.mailAddress.isEmpty()) {
            errors.add(new ValidationError("email", "Bitte geben Sie Ihre E-Mail an"));
        }

        return errors.isEmpty() ? null : errors;
    }

}
