package com.eagle.cansacaredoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 100;
    EditText firstName, lastName, emailNewDoctor, passwordNewDoctor;
    Button register, registerGoogle, registerFacebook;
    TextView alreadyUser;

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firstName = findViewById(R.id.signup_first_name);
        lastName = findViewById(R.id.signup_last_name);
        emailNewDoctor = findViewById(R.id.signup_email);
        passwordNewDoctor = findViewById(R.id.signup_password);
        register = findViewById(R.id.signup_create_account);
        registerFacebook = findViewById(R.id.signup_with_facebook);
        alreadyUser = findViewById(R.id.signup_to_signin);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register.setOnClickListener(v -> performAuth());

        alreadyUser.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, Login.class);
            startActivity(intent);
            startActivity(intent);
            finish();
        });

        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        registerGoogle = findViewById(R.id.signup_with_google);
        registerGoogle.setOnClickListener(v -> {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            SendUserToNextActivity();
        });
    }

    private void performAuth() {

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
            lastName.setError("This field has to be provided");
        }
        progressDialog.setMessage("Please wait while registration");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                String uid = user.getUid();

                // Create a new document with the user's data in the "doctors" collection
                Map<String, Object> userData = new HashMap<>();
                userData.put("firstName", firstname);
                userData.put("lastName", lastname);
                userData.put("email", email);
                FirebaseFirestore.getInstance().collection("doctors").document(uid).set(userData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(CreateAccount.this, "Registration done", Toast.LENGTH_LONG).show();
                            SendUserToNextActivity();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(CreateAccount.this, "Registration not done" + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } else {
                progressDialog.dismiss();
                Toast.makeText(CreateAccount.this, "Registration not done" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }}
