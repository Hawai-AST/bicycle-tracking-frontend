package controllers;


import org.springframework.stereotype.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;

@Controller
public class Waypoints {

    public Result create() { return ok("'test'"); }

}
