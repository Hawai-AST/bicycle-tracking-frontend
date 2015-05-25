package models;

import play.api.libs.json.Json;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Louisa on 23.05.2015.
 */
public class BikeDTO {

    private UUID id;
    private String type;
    private long frameNumber;
    private Date purchaseDate;
    private Date nextMaintenance;
    private String salesLocation;


}