package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Maps extends Controller {
    // TODO (Marjan) impl correct return

    /**
     * Creates and saves route
     * @return
     */
    public static Result create() {
        return ok("'test'");
    }

    /**
     * Updates specific route
     * @param id
     * @return
     */
    public static Result update(Integer id) {
        return ok("'test'");
    }

    /**
     * Shows all routes of the user
     * @return
     */
    public static Result index() {
        return ok("'test'");
    }

    /**
     * Shows one specific route
     * @param id
     * @return
     */
    public static Result show(Integer id) {
        return ok("'test'");
    }

    /**
     * Deletes one specific route
     * @param id
     * @return
     */
    public static Result delete(Integer id) {
        return ok("'test'");
    }
}