package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText emailNewDoctor, passwordNewDoctor;
    Button login, loginGoogle, loginFacebook;
    TextView notUserYet;
    ImageView backButton;

    String emailPattern = "^[A-Z\\d+_.-]+@[A-Z\\d.-]+$";

    ProgressDialog progressDialog;
    FirebaseUser mUser;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailNewDoctor = findViewById(R.id.login_username);
        passwordNewDoctor = findViewById(R.id.login_password);
        login = findViewById(R.id.login_sign_in);
        notUserYet = findViewById(R.id.login_not_user_create); // Initialize notUserYet TextView
        backButton = findViewById(R.id.login_back_ic);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        notUserYet.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, GetStarted.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> perforLogin());
    }

    private void perforLogin() {

        String email = emailNewDoctor.getText().toString().trim();
        String password = passwordNewDoctor.getText().toString().trim();

        if (!email.matches(emailPattern)) {
            emailNewDoctor.setError("Please provide a valid email");
        }
        if (password.length() < 6) {
            passwordNewDoctor.setError("Password empty or less than 6 characters");
        }
        if (email.isEmpty() || password.isEmpty()) {
            emailNewDoctor.setError("This field has to be provided");
            passwordNewDoctor.setError("This field has to be provided");
        }
        progressDialog.setMessage("Please wait while login");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                SendUserToNextActivity();
                Toast.makeText(Login.this, "Login done", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Login not done" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}