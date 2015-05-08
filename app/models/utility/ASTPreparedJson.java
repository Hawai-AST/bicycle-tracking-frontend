package models.utility;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import static controllers.Application.session;

public class ASTPreparedJson implements PreparedJson {

    private WSRequestHolder wsRequestHolder;

    protected ASTPreparedJson(String url) {
        wsRequestHolder = WS.url(url)
                .setHeader("Authorization", "Basic REVWLTEwMTpERVZTRUNSRVQ=")
                .setContentType("application/json");
        if (session("access_token") != null) {
            wsRequestHolder.setHeader("Authorization", "Bearer " + session("access_token"));
        }
    }

    protected ASTPreparedJson(String url, String token) {
        wsRequestHolder = WS.url(url)
                .setHeader("Authorization")
                .setHeader("Authorization", "Bearer " + token)
                .setContentType("application/json");
    }

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
     * @return Promised response from post call
     */
    @Override
    public F.Promise<JsonNode> post(JsonNode body) {
        try {
            return wsRequestHolder.post(body).map(WSResponse::asJson).recover(new F.Function<Throwable, JsonNode>() {
                // if null response is returned, recover it and return 'java' null properly
                @Override
                public JsonNode apply(Throwable throwable) throws Throwable {
                    return null;
                }
            });
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Calls GET method on the request
     *
     * @return Promised response from get call
     */
    @Override
    public F.Promise<JsonNode> get() {
        return wsRequestHolder.get().map(WSResponse::asJson);
    }
}
