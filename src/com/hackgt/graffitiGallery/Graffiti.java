package com.hackgt.graffitiGallery;

import android.location.Location;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joshua on 9/20/2014.
 */
public class Graffiti {

    private String name;
    private String artistName;
    private HashSet<String> tags;
    private int rating;
    private Location gLocation;
    private File photo;

    public Graffiti(String name, String artistName, HashSet<String> tags, int rating, Location gLocation, String photoLoc) {
        this.artistName = artistName;
        this.name = name;
        this.tags = tags;
        this.rating = rating;
        this.gLocation = gLocation;
        this.photo = new File(photoLoc);
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

    public HashSet<String> getTags() {
        return tags;
    }

    public void addTags(HashSet<String> tags) {
        this.tags.addAll(tags);
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Location getgLocation() {
        return gLocation;
    }

    public File getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return "Graffiti{" +
                "name='" + name + '\'' +
                ", artistName='" + artistName + '\'' +
                ", tags=" + tags +
                ", rating=" + rating +
                ", gLocation=" + gLocation +
                ", photo=" + photo +
                '}';
    }
}
