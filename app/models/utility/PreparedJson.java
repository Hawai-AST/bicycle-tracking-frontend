package models.utility;

import akka.japi.Pair;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

public interface PreparedJson {
    WSRequestHolder url(String url);

    WSRequestHolder setHeader(String name, String value);

    WSRequestHolder setContentType(String contentType);

    F.Promise<Pair<JsonNode,Integer>> post(JsonNode body);

    F.Promise<Pair<JsonNode,Integer>> post(String parameters);

    F.Promise<WSResponse> get();
}
