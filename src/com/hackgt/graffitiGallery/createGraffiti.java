package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by Joshua on 9/20/2014.
 */
public class createGraffiti extends Activity {

    private Location gLocation;
    private String imgLoc;
    private MobileServiceClient mClient;
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;"
                    + "AccountName=graffitigallery;"
                    + "AccountKey=apCqsRis2BZT/5Go+lEhJRrSuMQ3bTPI48nkwtRs/NIVFGrZjnK1LOAx96FaDa9YYORnZpDDsJHezKB19sJK7A==";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgraffiti);

        Intent intent = getIntent();
        Bundle pictureBundle = intent.getExtras();
        gLocation = new Location("mainScreen");
        gLocation.setLatitude(intent.getDoubleExtra(mainScreen.LAT_MESSAGE, 0));
        gLocation.setLongitude(intent.getDoubleExtra(mainScreen.LONG_MESSAGE, 0));
        ImageView preview = (ImageView) findViewById(R.id.imgPreview);
        imgLoc = pictureBundle.getString(mainScreen.SRC_MESSAGE);
        preview.setImageURI(Uri.parse(imgLoc));
        try {
            mClient = new MobileServiceClient(
                    "https://graffiti-gallery-service.azure-mobile.net/",
                    "wOUyOPzaDgnNKFOtQPoWDAQxrVEGoO76",
                    this
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    public void addGraffiti(View view) {
        EditText aName = (EditText) findViewById(R.id.name);
        EditText aArtist = (EditText) findViewById(R.id.artistName);
        RatingBar rate = (RatingBar) findViewById(R.id.gRating);

        Graffiti addedGraffiti =
                new Graffiti(aName.getText().toString(),
                        aArtist.getText().toString(),
                        rate.getRating(),
                        gLocation,
                        imgLoc);

        System.out.println(addedGraffiti.toString());
        new uploadGraffiti().execute(addedGraffiti);

    }

    private class uploadGraffiti extends AsyncTask<Graffiti, Void, String> {

        @Override
        protected String doInBackground(Graffiti... params) {
            File pFile = new File(imgLoc);
            String fName = pFile.getName();
            Graffiti graffiti = params[0];
            graffiti.setPhotoLoc(fName);
            CloudStorageAccount account;
            MobileServiceTable<Graffiti> table = mClient.getTable(Graffiti.class);
            try {
                account = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient serviceClient = account.createCloudBlobClient();
                CloudBlobContainer container = serviceClient.getContainerReference("graffiti-photos");
                CloudBlockBlob blob = container.getBlockBlobReference(fName);
                blob.upload(new java.io.FileInputStream(pFile), pFile.length());

                table.insert(graffiti,new TableOperationCallback<Graffiti>() {
                    @Override
                    public void onCompleted(Graffiti graffiti, Exception e, ServiceFilterResponse serviceFilterResponse) {
                        if (e == null) {
                            System.out.println(graffiti.toString());
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (StorageException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            return "Great Success!";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Graffiti Added!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }





}