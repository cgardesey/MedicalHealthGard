package com.prepeez.medicalhealthguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.prepeez.medicalhealthguard.R;


import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.PICTURE_TYPE;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.TYPE_PROFILE_PIC;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1.profilePicBitmap;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment5.TYPE_NHIS;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment5.nhisBitmap;

import androidx.appcompat.app.AppCompatActivity;

public class PictureActivity extends AppCompatActivity {
    private ImageView photoImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoImageView = findViewById(R.id.photoImageView);
        photoImageView.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
        photoImageView.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        photoImageView.setAdjustViewBounds(true);
        //photoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoImageView);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (!extras.containsKey(PICTURE_TYPE)) {
        } else {
            String picture_type = extras.getString(PICTURE_TYPE);
            if (picture_type.equals(TYPE_PROFILE_PIC)) {
                photoImageView.setImageBitmap(profilePicBitmap);
            }
            else if (picture_type.equals(TYPE_NHIS)) {
                photoImageView.setImageBitmap(nhisBitmap);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
