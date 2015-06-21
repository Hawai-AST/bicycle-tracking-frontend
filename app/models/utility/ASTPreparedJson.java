package models.utility;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import config.BackendConfig;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import static controllers.Application.session;

public class ASTPreparedJson implements PreparedJson {

    private WSRequestHolder wsRequestHolder;

    // base function
    protected ASTPreparedJson(String url, ContentType contentType) {
        wsRequestHolder = WS.url(url)
                .setContentType(contentType.value());

        // check for client-sided authentication state
        if (session("access_token") != null) { // logged in state
            wsRequestHolder.setHeader(BackendConfig.authHeader().first(), "Bearer " + session("access_token"));
        } else { // logged off state
            wsRequestHolder.setHeader(BackendConfig.authHeader().first(), BackendConfig.authHeader().second());
        }
    }

    // standard function for logged in state
    protected ASTPreparedJson(String url) {
        this(url, ContentType.APP_JSON);
    }

    // used for login
    protected ASTPreparedJson() {
        this(BackendConfig.oAuthToken(), ContentType.APP_FORM_ENC);
    }

//    protected ASTPreparedJson(String url, String token) {
//        wsRequestHolder = WS.url(url)
//                .setHeader(BackendConfig.authHeader().first(), "Bearer " + token)
//                .setContentType(ContentType.APP_JSON.value());
//    }

    /**
     * Sets the url for the request
     *
     * @param url The url to invoke later
     * @return Modified wsRequestHolder
     */
    @Override
    public WSRequestHolder url(String url) {
        wsRequestHolder = WS.url(url);
        return wsRequestHolder;
    }

    /**
     * Sets the content type for the request
     *
     * @param name  Name of the key which shall be added
     * @param value Value of the key
     * @return Modified wsRequestHolder
     */
    @Override
    public WSRequestHolder setHeader(String name, String value) {
        return wsRequestHolder.setHeader(name, value);
    }

    /**
     * Sets the content type for the request
     *
     * @param contentType Content type of the request
     * @return Modified wsRequestHolder
     */
    @Override
    public WSRequestHolder setContentType(String contentType) {
        return wsRequestHolder.setContentType(contentType);
    }

    /**
     * Posts the given json body to the specified url
     *
     * @param body Body of JSON node to post
     * @return Promised response from post call and a status code
     */
    @Override
    public F.Promise<Pair<JsonNode, Integer>> post(JsonNode body) {
        return wsRequestHolder.post(body).map(
                response -> {
                    JsonNode json = null;
                    try {
                        json = response.asJson();
                    } catch (RuntimeException e) {
                        //e.printStackTrace();
                    }
                    Integer status = response.getStatus();
                    return new Pair(json, status);
                }
        );
    }

    @Override
    public F.Promise<Pair<JsonNode, Integer>> post(String paramters) {
        return wsRequestHolder.post(paramters).map(
                response -> {
                    JsonNode json = null;
                    try {
                        json = response.asJson();
                    } catch (RuntimeException e) {
                        //e.printStackTrace();
                    }
                    Integer status = response.getStatus();
                    return new Pair(json, status);
                }
        );
    }

    /**
     * Calls GET method on the request
     *
     * @return Promised response from get call
     */
    @Override
    public F.Promise<WSResponse> get() {
        return wsRequestHolder.get();
    }
}
