import config.BackendConfig;
import controllers.RequiresLogin;
import play.Application;
import play.GlobalSettings;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        super.onStart(application);
        BackendConfig.load();
    }

    @Override
    public Action onRequest(Http.Request request, Method method) {
        Action original = super.onRequest(request, method);
        RequiresLogin annotation = method.getAnnotation(RequiresLogin.class);
        //if (annotation != null) {
        //    return new LoginCheckAction(method, original);
        //}

        return original;
    }

    public static class LoginCheckAction extends Action {

        private final Method original;
        private final Action other;

        public LoginCheckAction(Method original, Action other) {
            this.original = original;
            this.other = other;
        }

        @Override
        public F.Promise<Result> call(Http.Context context) throws Throwable {
            //System.out.println(context.session());
            if (!context.session().containsKey("access_token")) {
                return F.Promise.pure(redirect("/"));
            } else {
                return this.other.call(context);
            }
        }
    }
}
