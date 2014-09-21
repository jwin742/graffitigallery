package com.hackgt.graffitiGallery;

import android.location.Location;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.*;

/**
 * Created by Joshua on 9/20/2014.
 */
public class Graffiti {

    private String name;
    private String artistName;
    private HashSet<String> tags;
    private float rating;
    private Location gLocation;
    private File photo;
    private Date dayMade;

    public Graffiti(String name, String artistName, HashSet<String> tags, float rating, Location gLocation, String photoLoc) {
        if (artistName == null) {
            this.artistName = "UNKNOWN";
        }
        else {
            this.artistName = artistName;
        }
        this.name = name;
        this.tags = tags;
        this.rating = rating;
        this.gLocation = gLocation;
        this.photo = new File(photoLoc);
        this.dayMade = new Date();
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

    public float getRating() {
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

    public Date getDayMade() {
        return dayMade;
    }

    @Override
    public String toString() {
        return "Graffiti{" +
                "name='" + name + '\'' +
                ", artistName='" + artistName + '\'' +
                ", tags=" + tags +
                ", DateMade=" + dayMade.toString() +
                ", rating=" + rating +
                ", gLocation=" + gLocation +
                ", photo=" + photo +
                '}';
    }

    public List<NameValuePair> generateNameValuePairs() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("artist_name", artistName));
        nameValuePairs.add(new BasicNameValuePair("date_made", dayMade.toString()));
        nameValuePairs.add(new BasicNameValuePair("rating", String.valueOf(rating)));
        nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(gLocation.getLatitude())));
        nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(gLocation.getLongitude())));

        return nameValuePairs;
    }

}
