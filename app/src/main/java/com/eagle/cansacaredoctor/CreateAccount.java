package com.eagle.cansacaredoctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eagle.cansacaredoctor.ressources.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 100;
    EditText firstNameDoctor, lastNameDoctor, emailNewDoctor, passwordNewDoctor;
    Button register, registerGoogle;
    TextView alreadyUser;

    String userId, displayName;

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firstNameDoctor = findViewById(R.id.signup_first_name);
        lastNameDoctor = findViewById(R.id.signup_last_name);
        emailNewDoctor = findViewById(R.id.signup_email);
        passwordNewDoctor = findViewById(R.id.signup_password);

        register = findViewById(R.id.signup_create_account);
        alreadyUser = findViewById(R.id.signup_to_signin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        ;

        register.setOnClickListener(v -> performAuth());

        alreadyUser.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, Login.class);
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
        });
    }

    private void performAuth() {

        //Collecting all the information's provided
        String email = emailNewDoctor.getText().toString().trim();
        String password = passwordNewDoctor.getText().toString().trim();
        String firstname = firstNameDoctor.getText().toString().trim();
        String lastname = lastNameDoctor.getText().toString().trim();

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {

            //create user in Firebase Firestore
            firebaseFirestore.collection("Doctors")
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .set(new User(firstname, lastname, email, password, userId = FirebaseAuth.getInstance().getUid(), displayName = firstname + " " + lastname))
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(CreateAccount.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateAccount.this, MainActivity.class));
                        SendUserToNextActivity();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(CreateAccount.this, "Error creating user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        }).addOnFailureListener(e -> {
            Toast.makeText(CreateAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
