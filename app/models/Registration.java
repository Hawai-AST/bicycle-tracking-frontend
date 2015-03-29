package models;

public class Registration {

    public String   firstName;
    public String   lastName;
    public String   birthdate;
    public String   sex;
    public String   addrStreet;
    public String   addrStreetNo;
    public String   addrZipCode;
    public String   addrLocation;
    public String   email;
    public String   password;
    public String   passwordCheck;

    public String validate() {
        if (firstName.isEmpty()) {
            return "Vorname muss angegeben werden";
        }
        if (lastName.isEmpty()) {
            return "Nachname muss angegeben werden";
        }
        if (birthdate.isEmpty()) {
            return "Geburtsdatum muss angegeben werden";
        }
        if (addrStreet.isEmpty()) {
            return "Straße muss angegeben werden";
        }
        if (addrStreetNo.isEmpty()) {
            return "Hausnummer muss angegeben werden";
        }
        if (addrZipCode.isEmpty()) {
            return "Postleitzahl muss angegeben werden";
        }
        if (addrLocation.isEmpty()) {
            return "Ort muss angegeben werden";
        }
        if (email.isEmpty()) {
            return "E-Mail muss angegeben werden";
        }
        if (password.isEmpty()) {
            return "Passwort muss angegeben werden";
        }
        if (passwordCheck.isEmpty()) {
            return "Passwort muss erneut angegeben werden";
        }

        if (!password.equals(passwordCheck)) {
            return "Die Passwörter stimmen nicht überein!";
        }

        return null;
    }

}