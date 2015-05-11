package controllers;

import play.mvc.Controller;
import play.mvc.Result;


/**
 * Created by Louisa on 11.05.2015.
 */
public class Tracks extends Controller {

    public static Result tracks() {
    return ok(views.html.member.tracks.render());

    }
}
