package models;

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

}