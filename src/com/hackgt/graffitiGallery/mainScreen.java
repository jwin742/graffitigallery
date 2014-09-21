package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class mainScreen extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private MobileServiceClient mClient;
    public final static String SRC_MESSAGE = "com.hackgt.graffitiGallery.IMAGE_SRC";
    public final static String LONG_MESSAGE = "com.hackgt.graffitiGallery.LONG_DATA";
    public final static String LAT_MESSAGE = "com.hackgt.graffitiGallery.LAT_DATA";
    private String mCurrentPhotoPath;
    private LocationClient mLocationClient;
    private Location loc;
    private Button create;
    private ProgressBar locWait;
    private TextView textlocWait;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mLocationClient = new LocationClient(this, this, this);
        create = (Button) findViewById(R.id.addG);
        locWait = (ProgressBar) findViewById(R.id.locWaiting);
        textlocWait = (TextView) findViewById(R.id.textWait);

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

    public void randomizer(View view) {
        final Random random = new Random();
        final MobileServiceTable<Graffiti> serviceTable = mClient.getTable(Graffiti.class);
        serviceTable.select("Id").execute(new TableQueryCallback<Graffiti>() {
            @Override
            public void onCompleted(List<Graffiti> graffitis, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                Intent intent = new Intent(getApplicationContext(), graffitiView.class);
                String randId = graffitis.get(random.nextInt(graffitis.size())).getId();
                intent.putExtra("RANDOM_GRAFFITI_ID", randId);
                startActivity(intent);
            }
        });
    }

    public void takePicture(View view) {

        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, createGraffiti.class);
            intent.putExtra(SRC_MESSAGE,mCurrentPhotoPath);

            intent.putExtra(LAT_MESSAGE, loc.getLatitude());
            intent.putExtra(LONG_MESSAGE, loc.getLongitude());
            startActivity(intent);
        }
    }

    //Google why?

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
    * Called by Location Services when the request to connect the
    * client finishes successfully. At this point, you can
    * request the current location or start periodic updates
    */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        loc = mLocationClient.getLastLocation();
        locWait.setVisibility(View.INVISIBLE);
        textlocWait.setVisibility(View.INVISIBLE);
        create.setEnabled(true);
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.print("WHYYYYYY");
    }

}
