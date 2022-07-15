package com.example.prototype10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class Screenshot extends AppCompatActivity {
    ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);
        init();
    }

    private void init() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        String completePath = Environment.getExternalStorageDirectory() + "/" + "screenshotdemo.jpg";
        Glide.with(Screenshot.this).load(completePath).error(R.drawable.ic_hidden).into(iv_image);

    }


}