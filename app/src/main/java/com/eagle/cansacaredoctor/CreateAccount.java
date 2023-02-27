package com.eagle.cansacaredoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    EditText firstName, lastName, emailNewDoctor, passwordNewDoctor;
    Button register, registerGoogle, registerFacebook;
    TextView alreadyUser;

    String emailPattern = "^[A-Z\\d+_.-]+@[A-Z\\d.-]+$";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstName = findViewById(R.id.signup_first_name);
        lastName = findViewById(R.id.signup_last_name);
        emailNewDoctor = findViewById(R.id.signup_email);
        passwordNewDoctor = findViewById(R.id.signup_password);
        register = findViewById(R.id.signup_create_account);
        registerGoogle = findViewById(R.id.signup_with_google);
        registerFacebook = findViewById(R.id.signup_with_facebook);
        alreadyUser = findViewById(R.id.signup_to_signin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register.setOnClickListener(v -> PerforAuth());


        alreadyUser.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void PerforAuth() {

        String email = emailNewDoctor.getText().toString().trim();
        String password = passwordNewDoctor.getText().toString().trim();
        String firstname = firstName.getText().toString().trim();
        String lastname = lastName.getText().toString().trim();

        if (!email.matches(emailPattern)) {
            emailNewDoctor.setError("Please provide a valid email");
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordNewDoctor.setError("Password empty or less than 6 characters");
        }
        if (lastname.isEmpty() || firstname.isEmpty()) {
            firstName.setError("This field has to be provided");
            firstName.setError("This field has to be provided");
        }
        progressDialog.setMessage("Please wait while registration");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                SendUserToNextActivity();
                Toast.makeText(CreateAccount.this, "Registration done", Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Registration not done" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}