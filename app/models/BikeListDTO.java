package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.List;

/**
 * Created by Louisa on 23.05.2015.
 */
public class BikeListDTO {

    public int amount;
    public List<BikeDTO> bikes;

    public static BikeListDTO fromJson(JsonNode node){
        return Json.fromJson(node, BikeListDTO.class);
    }

}
