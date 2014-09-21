package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by Joshua on 9/21/2014.
 */
public class graffitiView extends Activity {

    private MobileServiceClient mClient;
    private Graffiti displayedGraffiti;
    private CloudBlobContainer container;
    private ImageView imgView;
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;"
                    + "AccountName=graffitigallery;"
                    + "AccountKey=apCqsRis2BZT/5Go+lEhJRrSuMQ3bTPI48nkwtRs/NIVFGrZjnK1LOAx96FaDa9YYORnZpDDsJHezKB19sJK7A==";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graffiti_view);
        Intent intent = getIntent();
        imgView = (ImageView) findViewById(R.id.gView);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.gRating);
        ratingBar.setEnabled(false);
        final TextView nameView = (TextView) findViewById(R.id.gName);
        final TextView AnameView = (TextView) findViewById(R.id.gAName);

        try {
            mClient = new MobileServiceClient(
                    "https://graffiti-gallery-service.azure-mobile.net/",
                    "wOUyOPzaDgnNKFOtQPoWDAQxrVEGoO76",
                    this
            );
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient client = account.createCloudBlobClient();
            container = client.getContainerReference("graffiti-photos");


            MobileServiceTable<Graffiti> table = mClient.getTable(Graffiti.class);
            table.lookUp(intent.getStringExtra("RANDOM_GRAFFITI_ID"), new TableOperationCallback<Graffiti>() {
                @Override
                public void onCompleted(Graffiti graffiti, Exception e, ServiceFilterResponse serviceFilterResponse) {
                    displayedGraffiti = graffiti;
                    try {
                        displayGraffiti(ratingBar, nameView, AnameView);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    private void displayGraffiti( RatingBar ratingBar, TextView nameView, TextView anameView) throws IOException {
        ratingBar.setRating(displayedGraffiti.getRating());
        nameView.setText(displayedGraffiti.getName().subSequence(0, displayedGraffiti.getName().length()));
        anameView.setText(displayedGraffiti.getArtistName().subSequence(0, displayedGraffiti.getArtistName().length()));

        new DownloadImageTask(imgView).execute("https://graffitigallery.blob.core.windows.net/graffiti-photos/" + displayedGraffiti.getPhotoLoc());



    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}