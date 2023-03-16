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

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 100;
    EditText email, password;
    Button signin, signinGoogle;
    TextView toSignup;

    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser mUser;

    EditText emailLogin, passwordLogin;
    Button login;
    TextView newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailLogin = findViewById(R.id.login_username);
        passwordLogin = findViewById(R.id.login_password);
        login = findViewById(R.id.login_sign_in);
        newUser = findViewById(R.id.login_not_user_create);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> performLogin());

        newUser.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });
    }

    private void performLogin() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
            SendUserToNextActivity();
            finish();

        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(Login.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


