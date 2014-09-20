package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Joshua on 9/20/2014.
 */
public class createGraffiti extends Activity {

    private Location gLocation;
    private String imgLoc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgraffiti);

        Intent intent = getIntent();
        Bundle pictureBundle = intent.getExtras();
        gLocation = intent.getParcelableExtra(mainScreen.LOC_MESSAGE);
        ImageView preview = (ImageView) findViewById(R.id.imgPreview);
        imgLoc = pictureBundle.getString(mainScreen.SRC_MESSAGE);
        preview.setImageURI(Uri.parse(imgLoc));
    }

    public void addGraffiti(View view) {
        EditText aName = (EditText) findViewById(R.id.name);
        EditText aArtist = (EditText) findViewById(R.id.artistName);
        RatingBar rate = (RatingBar) findViewById(R.id.gRating);
        EditText tags = (EditText) findViewById(R.id.gTags);

        String[] arr = tags.getText().toString().split("/s");
        HashSet<String> tagSet = new HashSet<String>();
        for(int x = 0; x< arr.length; x++) {
            tagSet.add(arr[x]);
        }
        Graffiti addedGraffiti =
                new Graffiti(aName.getText().toString(),
                        aArtist.getText().toString(),
                        tagSet,
                        rate.getNumStars(),
                        gLocation,
                        imgLoc);

        System.out.println(addedGraffiti.toString());
        //addedGraffiti.toJSON();

    }

}