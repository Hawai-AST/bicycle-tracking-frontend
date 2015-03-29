package controllers;


import models.Login;
import models.Registration;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import views.html.index;
import views.html.signin;

import static play.mvc.Results.ok;

@Controller
public class Application {

    public Result index() {
        return ok(index.render());
    }

    public Result signin() {
        return ok(signin.render(
                "Anmelden",
                Form.form(Login.class),
                Form.form(Registration.class)
        ));
    }

    public Result register() {
        Form<Registration> form = Form.form(Registration.class).bindFromRequest();
        Registration registration = form.get();
        return ok("Hallo " + registration.firstName + "! \n" + "Du hast Dich erfolgreich registriert." +
                "\n" + Json.toJson(registration));

        // TODO Exception Handling bzgl. validate()

        // TODO Send JSON authentication to backend
    }

    public Result authenticate() {
        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();
        return ok("You entered the following email: " + login.email + "\n" + Json.toJson(login));

        // TODO Exception Handling bzgl. validate()

        // TODO Send JSON registration to backend
    }

}