package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import models.utility.value.Address;
import play.libs.Json;

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

}
