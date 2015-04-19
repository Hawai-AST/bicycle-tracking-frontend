package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Address;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

// TODO(Timmay):validate email and other validations

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

    public JsonNode toJson() {
        return Json.toJson(this);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (firstname.isEmpty()) {
            errors.add(new ValidationError("firstname", "Vorname ungültig"));
        }
        if (name.isEmpty()) {
            errors.add(new ValidationError("name", "Nachname ungültig"));
        }
        if (birthday.isEmpty()) {
            errors.add(new ValidationError("birthday", "Geburtstag ungültig"));
        }
        if (gender.isEmpty()) {
            errors.add(new ValidationError("gender", "Geschlecht nicht ausgewählt"));
        }
        if (address.city.isEmpty()) {
            errors.add(new ValidationError("address.city", "Stadt ungültig"));
        }
        if (address.country.isEmpty()) {
            errors.add(new ValidationError("address.country", "Land ungültig"));
        }
        if (address.houseNumber.isEmpty()) {
            errors.add(new ValidationError("address.houseNumber", "Hausnummer ungültig"));
        }
        if (address.postcode.isEmpty()) {
            errors.add(new ValidationError("address.postcode", "Postleitzahl ungültig"));
        }
        if (address.state.isEmpty()) {
            errors.add(new ValidationError("address.state", "Bundesland ungültig"));
        }
        if (address.street.isEmpty()) {
            errors.add(new ValidationError("address.street", "Straße ungültig"));
        }
        if (email.isEmpty()) {
            errors.add(new ValidationError("email", "E-Mail Adresse ungültig"));
        }
        if (customerid.isEmpty()) {
            errors.add(new ValidationError("customerid", "Kundennummer ungültig"));
        }
        if (password.isEmpty()) {
            errors.add(new ValidationError("password", "Password ungültig"));
        }
        if (!password.equals(passwordCheck)) {
            errors.add(new ValidationError("passwordCheck", "Die Passwörter stimmen nicht überein"));
        }

        return errors.isEmpty() ? null : errors;
    }

}
