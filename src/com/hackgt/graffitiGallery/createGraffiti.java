package com.hackgt.graffitiGallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Joshua on 9/20/2014.
 */
public class createGraffiti extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgraffiti);

        Intent intent = getIntent();
        Bundle pictureBundle = intent.getExtras();

        Bitmap imageBitmap = (Bitmap) pictureBundle.get("data");
        ImageView preview = (ImageView) findViewById(R.id.imgPreview);
        preview.setImageBitmap(imageBitmap);
    }
}