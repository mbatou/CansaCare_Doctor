package com.eagle.cansacaredoctor;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton, loginWithGoogleButton, loginWithFacebookButton;
    TextView notUserYetTextView;
    ImageView backButtonImageView;

    String emailPattern = "^[A-Z\\d+_.-]+@[A-Z\\d.-]+$";

    GoogleSignInClient mGoogleSignInClient;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_sign_in);
        loginWithGoogleButton = findViewById(R.id.login_with_google);
        notUserYetTextView = findViewById(R.id.login_not_user_create);
        backButtonImageView = findViewById(R.id.login_back_ic);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        notUserYetTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });

        backButtonImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, GetStarted.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> performLogin());

        loginWithGoogleButton.setOnClickListener(v -> signInWithGoogle());

        createGoogleSignInClient();
    }

    private void performLogin() {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!email.matches(emailPattern)) {
            emailEditText.setError("Please provide a valid email");
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password empty or less than 6 characters");
        }
        if (email.isEmpty() || password.isEmpty()) {
            emailEditText.setError("This field has to be provided");
            passwordEditText.setError("This field has to be provided");
        }
        progressDialog.setMessage("Please wait while login");
        progressDialog.setTitle("Login");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference usersRef = db.collection("doctors").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                usersRef.get().addOnCompleteListener(documentTask -> {
                    if (documentTask.isSuccessful()) {
                        DocumentSnapshot document = documentTask.getResult();
                        if (document.exists()) {
                            progressDialog.dismiss();
                            SendUserToNextActivity();
                            Toast.makeText(Login.this, "Login done", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            mAuth.signOut();
                            Toast.makeText(Login.this, "You are not registered", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        mAuth.signOut();
                        Toast.makeText(Login.this, "Error getting user information", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Login not done" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void createGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = (GoogleSignInAccount) ((Task<?>) task).getResult(ApiException.class);
                        authWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Toast.makeText(Login.this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void authWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        String displayName = user.getDisplayName();
                        String email = user.getEmail();

                        // Save user data to Firestore
                        saveUserDataToFirestore(displayName, email);
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        SendUserToNextActivity();
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDataToFirestore(String displayName, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("displayName", displayName);
        user.put("email", email);
        db.collection("doctors").document(Objects.requireNonNull(mAuth.getUid()))
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User data added"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding user data", e));
    }

    private void SendUserToNextActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }}

