package com.journey.geography;

public class GeographicInformation {
    private String addressName;
    private double longitude;
    private double latitude;
    public GeographicInformation(String addressName, double longitude, double latitude)
    {
        this.addressName = addressName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddressName() {
        return addressName;
    }
}
