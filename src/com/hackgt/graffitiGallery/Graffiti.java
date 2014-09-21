package com.hackgt.graffitiGallery;

import android.location.Location;
import com.microsoft.azure.storage.table.DynamicTableEntity;

import java.io.File;
import java.util.Date;

/**
 * Created by Joshua on 9/20/2014.
 */
public class Graffiti extends DynamicTableEntity {

    private String id;
    private String name;
    private String artistName;
    private float rating;
    private double latitude;
    private double longitude;
    private boolean Deleted;
    private String photoLoc;
    private double altitude;


    public Graffiti(String name, String artistName, float rating, Location gLocation, String photoLoc) {
        if (artistName == null) {
            this.artistName = "UNKNOWN";
        }
        else {
            this.artistName = artistName;
        }
        this.id = String.valueOf(gLocation.hashCode());
        this.name = name;
        this.rating = rating;
        this.latitude = gLocation.getLatitude();
        this.longitude = gLocation.getLongitude();
        this.altitude = gLocation.getAltitude();
        this.photoLoc = photoLoc;
        this.Deleted = true;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public boolean isUp() {
        return Deleted;
    }

    public void setUp(boolean isUp) {
        this.Deleted = isUp;
    }

    public String getPhotoLoc() {
        return photoLoc;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Location getgLocation() {
        Location location = new Location("android");
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setAltitude(altitude);
        return location;
    }

    public File getPhoto() {
        return new File(photoLoc);
    }

    public Date getDayMade() {
        return timeStamp;
    }

    public void setPhotoLoc(String photoLoc) {
        this.photoLoc = photoLoc;
    }

    @Override
    public String toString() {
        return "Graffiti{" +
                "name='" + name + '\'' +
                ", artistName='" + artistName + '\'' +
                ", rating=" + rating +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isUp=" + Deleted +
                ", photoLoc='" + photoLoc + '\'' +
                ", altitude=" + altitude +
                '}';
    }

    public String getId() {
        return id;
    }
}
