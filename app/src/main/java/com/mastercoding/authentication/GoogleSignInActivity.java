package com.mastercoding.authentication;//package com.mastercoding.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeFormatException;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends MainActivity {
//    private static final int REQ_ONE_TAP = 101;
//    GoogleSignInClient mGoogleSignInClient;
//    FirebaseAuth mAuth;
//    FirebaseUser mUser;
//    ProgressDialog progressDialog;
//
//    public GoogleSignInActivity() throws ApiException {
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Google Sign In...");
//        progressDialog.show();
//
//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//
//        final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
//        boolean showOneTapUI = true;
////
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_ONE_TAP:
//                try {
//                    SignInClient oneTapClient = null;
//                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                    String idToken = credential.getGoogleIdToken();
//                    if (idToken != null) {
//                        // Got an ID token from Google. Use it to authenticate
//                        // with Firebase.
//                        Log.d(TAG, "Got ID token.");
//                    }
//                } catch (ApiException e) {
//
//                    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                    finish();
//                }
//              //  break;
//
//        }
//    }

   // private SignInClient oneTapClient;


    // chatgpt
    private static final int REQ_ONE_TAP = 101;
    private SignInClient oneTapClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google Sign In...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

        // Initialize oneTapClient here or according to your implementation

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP && data != null) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }
    }



//    SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//    String idToken = googleCredential.getGoogleIdToken();
//        if (idToken !=  null) {
//        // Got an ID token from Google. Use it to authenticate
//        // with Firebase.

//        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(firebaseCredential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            progressDialog.dismiss();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            progressDialog.dismiss();
//                            Toast.makeText(GoogleSignInActivity.this, ""+task.getException(),
//                                    Toast.LENGTH_SHORT).show();
//                           // updateUI(null);
//                            finish();
//                        }
//                    }
//                });
//    }


        private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
            Intent intent = new Intent(GoogleSignInActivity.this , HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
    }

}

//    import static android.content.ContentValues.TAG;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.auth.api.identity.BeginSignInRequest;
//import com.google.android.gms.auth.api.identity.SignInClient;
//import com.google.android.gms.auth.api.identity.SignInCredential;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.mastercoding.authentication.HomeActivity;
//import com.mastercoding.authentication.MainActivity;
//
//public class GoogleSignInActivity extends MainActivity {
//    private static final int REQ_ONE_TAP = 101;
//    private GoogleSignInClient mGoogleSignInClient;
//    private FirebaseAuth mAuth;
//    private FirebaseUser mUser;
//    private ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Google Sign In...");
//        progressDialog.show();
//
//        mAuth = FirebaseAuth.getInstance();
//        mUser = mAuth.getCurrentUser();
//
//        // Create a BeginSignInRequest
//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();
//
//        // Start the One Tap sign-in flow
//        // You need to implement this part according to your app's requirements
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQ_ONE_TAP && data != null) {
//            try {
//                SignInClient SignInCredential = null;
//                SignInClient oneTapClient = // Initialize this properly based on your implementation;
//                        SignInCredential;
//                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                String idToken = credential.getGoogleIdToken();
//                if (idToken != null) {
//                    // Got an ID token from Google. Use it to authenticate with Firebase.
//                    firebaseAuthWithGoogle(idToken);
//                }
//            } catch (ApiException e) {
//                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//                finish();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(firebaseCredential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            progressDialog.dismiss();
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            progressDialog.dismiss();
//                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//                    }
//                });
//    }
//
//    private void updateUI(FirebaseUser user) {
//        Intent intent = new Intent(GoogleSignInActivity.this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//}
