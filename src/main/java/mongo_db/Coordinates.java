package mongo_db;

public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(){}
    public Coordinates(double lat, double longt)
    {
        latitude = lat;
        longitude = longt;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
