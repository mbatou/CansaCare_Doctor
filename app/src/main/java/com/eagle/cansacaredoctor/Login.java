package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button login = findViewById(R.id.login_sign_in);
    EditText emailEditText = findViewById(R.id.login_username);
    EditText passwordEditText = findViewById(R.id.login_password);
    TextView alreadyUser = findViewById(R.id.signup_already_user);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mAuth.getCurrentUser();
                            // Do something with the user information, e.g. start the next activity
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


    });
        alreadyUser.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this,CreateAccount.class);
            startActivity(intent);
            finish();
        });
}}