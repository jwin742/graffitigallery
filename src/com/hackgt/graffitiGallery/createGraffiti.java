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
import com.microsoft.azure;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Joshua on 9/20/2014.
 */
public class createGraffiti extends Activity {

    private Location gLocation;
    private String imgLoc;
    private MobileServiceClient mClient;
    private MobileServiceTable<Graffiti> mGraffitiTable;
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;"
                    + "AccountName=graffitigaller;"
                    + "AccountKey= apCqsRis2BZT/5Go+lEhJRrSuMQ3bTPI48nkwtRs/NIVFGrZjnK1LOAx96FaDa9YYORnZpDDsJHezKB19sJK7A==";
    private CloudStorageAccout account;

    @Override
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
            mGraffitiTable = mClient.getTable(Graffiti.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void addGraffiti(View view) {
        EditText aName = (EditText) findViewById(R.id.name);
        EditText aArtist = (EditText) findViewById(R.id.artistName);
        RatingBar rate = (RatingBar) findViewById(R.id.gRating);
        EditText tags = (EditText) findViewById(R.id.gTags);

        String[] arr = tags.getText().toString().split("/s");
        HashSet<String> tagSet = new HashSet<>();
        Collections.addAll(tagSet, arr);
        Graffiti addedGraffiti =
                new Graffiti(aName.getText().toString(),
                        aArtist.getText().toString(),
                        tagSet,
                        rate.getRating(),
                        gLocation,
                        imgLoc);

        System.out.println(addedGraffiti.toString());
        new uploadGraffiti().execute(addedGraffiti);

    }

    private class uploadGraffiti extends AsyncTask<Graffiti, Void, String> {

        @Override
        protected String doInBackground(Graffiti... params) {
            //ProgressBar progressBar = (ProgressBar) findViewById(R.id.networkProg);
            //progressBar.setVisibility(View.VISIBLE);

            mGraffitiTable.insert(params[0], (entity, exception, response) -> {
                if (exception == null) {
                    if (!entity.isComplete()) {
                        mAdapter.add(entity);
                    }
                } else {
                    createAndShowDialog(exception, "Error");
                }
            });

            try {
                storeGraffiti(params[0]);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return "Great Success!";
        }

        @Override
        protected void onPostExecute(String result) {
            //ProgressBar progressBar = (ProgressBar) findViewById(R.id.networkProg);
            //progressBar.setVisibility(View.INVISIBLE);
            //CharSequence sequence = result.subSequence(0, result.length());
            //Toast.makeText(getApplicationContext() ,sequence, Toast.LENGTH_SHORT).show();
            //System.out.println(result);
        }

        private void storeGraffiti(Graffiti graffiti) throws IOException {
            String requestURI = "https://graffitigallery.table.core.windows.net/graffiti";
            String photoURL = getPhotoURL();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestURI);
            List<NameValuePair> valuePairs = graffiti.generateNameValuePairs();
            valuePairs.add(new BasicNameValuePair("photo_url", photoURL));


            httpPost.setEntity(new UrlEncodedFormEntity(valuePairs));
            httpClient.execute(httpPost);
        }

        /**
         * this also puts the photo in the blob
         * @return the URL of the photo
         */
        private String getPhotoURL() throws IOException{
            File pFile = new File(imgLoc);
            String fName = pFile.getName();

            String putRequest = "https://graffitigallery.blob.core.windows.net/graffiti-photos/" + fName;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(putRequest);
            Date now = new Date();
            SimpleDateFormat utcFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            SimpleDateFormat versionformat = new SimpleDateFormat("yyyy-MM-dd");


            httpPut.addHeader(new BasicHeader("Authorization", "SharedKey " + "graffitigallery:" + "apCqsRis2BZT/5Go+lEhJRrSuMQ3bTPI48nkwtRs/NIVFGrZjnK1LOAx96FaDa9YYORnZpDDsJHezKB19sJK7A=="));
            httpPut.addHeader(new BasicHeader("x-ms-version", now.toString()));
            httpPut.addHeader(new BasicHeader("x-ms-date", utcFormat.format(now)));
            httpPut.addHeader(new BasicHeader("Content-Length", String.valueOf(pFile.getTotalSpace())));
            httpPut.addHeader(new BasicHeader("x-ms-blob-type", "BlockBlob"));

            httpPut.setEntity(new FileEntity(pFile,"image/jpeg"));
            System.out.println(httpPut.toString());
            try {
                httpClient.execute(httpPut);
            } catch (Exception e){
                e.printStackTrace();
            }

            return putRequest;
        }

    }





}