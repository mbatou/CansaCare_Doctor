package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        //will have to figure out how to make the splashscreen temporary
        // or i can just add the logo on the embed splashscreen so i'll delete this splashscreen

        ImageView logo = findViewById(R.id.cansacare_logo);

        logo.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreen.this, GetStarted.class);
            startActivity(intent);
            finish();
        });
    }
}