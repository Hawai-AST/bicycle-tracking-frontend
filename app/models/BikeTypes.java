package models;

public class BikeTypes {
    public int amount;
    public BikeType[] bikeTypes;

    public static class BikeType {
        public String name;
        public String description;
        public String inspectionInterval;
    }
}
