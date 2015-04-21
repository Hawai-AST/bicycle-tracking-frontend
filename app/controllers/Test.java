package controllers;

import models.Login;
import org.springframework.stereotype.Controller;
import play.Logger;
import play.data.Form;
import play.mvc.Result;

import static play.mvc.Results.ok;

@Controller
public class Test {

    public Result test() {
        return ok(views.html.test.render(Form.form(Login.class)));
    }

}
