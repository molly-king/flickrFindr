package com.mollyking.flickrfindr;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.drawee.view.SimpleDraweeView;

public class PhotoViewerActivity extends AppCompatActivity {

    public static String PHOTO_EXTRA = "photoExtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        String photoUrl = getIntent().getStringExtra(PHOTO_EXTRA);
        SimpleDraweeView photoView = findViewById(R.id.photo);
        photoView.setImageURI(photoUrl);
    }
}
