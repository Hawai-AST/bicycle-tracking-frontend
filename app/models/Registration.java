package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Address;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

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
        List<ValidationError> errors = new ArrayList<>();
        if (firstname.isEmpty()) {
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
        if (address.state.isEmpty()) {
            errors.add(new ValidationError("address.state", "Bitte geben Sie Ihr Bundesland an"));
        }
        if (address.street.isEmpty()) {
            errors.add(new ValidationError("address.street", "Bitte geben Sie Ihre Straße an"));
        }
        if (email.isEmpty()) {
            errors.add(new ValidationError("email", "Bitte geben Sie Ihre E-Mail an"));
        }
        if (password.isEmpty()) {
            errors.add(new ValidationError("password", "Bitte geben Sie ein Passwort an"));
        }
        if (!password.equals(passwordCheck) || passwordCheck.isEmpty() /*<-- just to color the input field red */) {
            errors.add(new ValidationError("passwordCheck", "Die Passwörter stimmen nicht überein"));
        }

        return errors.isEmpty() ? null : errors;
    }

}
