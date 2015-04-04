package controllers;

// TODO REST implementation --> communicate with backend
// TODO store sessions


import models.Login;
import models.Registration;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.mvc.Result;
import views.html.index;
import views.html.signin;

import static play.mvc.Results.ok;

@Controller
public class Application {

    public Result index() { return ok(index.render()); }

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

        return ok(registration.toJson());
    }

    public Result authenticate() {
        Form<Login> form = Form.form(Login.class).bindFromRequest();
        Login login = form.get();

        return ok(login.toJson());
    }

}