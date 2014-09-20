package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class mainScreen extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void takePicture(View view) {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Intent intent = new Intent(this, createGraffiti.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
