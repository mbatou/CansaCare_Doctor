package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class GetStarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        //Set fullScreen and remove top actionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button createAccount = findViewById(R.id.getstarted_create_account);
        Button signIn = findViewById(R.id.getstarted_signin);

        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(GetStarted.this,CreateAccount.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(GetStarted.this,Login.class);
            startActivity(intent);
            finish();
        });
    }
}