package controllers.util;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

public class ASTPreparedJson {

    private WSRequestHolder wsRequestHolder;

    public ASTPreparedJson(String url) {

        wsRequestHolder = WS.url(url)
                .setHeader("Client-Id", "DEV-101")
                .setContentType("application/json");
    }

    public WSRequestHolder url(String url) {
        wsRequestHolder = WS.url(url);
        return wsRequestHolder;
    }

    public WSRequestHolder setHeader(String name, String value) {
        return wsRequestHolder.setHeader(name, value);
    }

    public WSRequestHolder setContentType(String contentType) {
        return wsRequestHolder.setContentType(contentType);
    }

    public F.Promise<JsonNode> post(com.fasterxml.jackson.databind.JsonNode body) {
        return wsRequestHolder.post(body).map(WSResponse::asJson);
    }
}
