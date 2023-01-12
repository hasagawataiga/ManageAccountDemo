package orca.application.manageaccountdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginClient;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class UserLogin extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    EditText et_email2;
    EditText et_password2;
    Button btn_login_Google;
    Button btn_login_Facebook;
    String email;
    String password;

    CallbackManager callbackManager;
    LoginClient loginClient;
    UserModel userModel;
    boolean isLoggedIn = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        et_email2 = findViewById(R.id.et_email2);
        et_password2 = findViewById(R.id.et_password2);
        btn_login_Google = findViewById(R.id.btn_login_Google);
        btn_login_Facebook = findViewById(R.id.btn_login_Facebook);

        // Email SignIn
        firebaseAuth = FirebaseAuth.getInstance();

        // Google SignIn
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        isLoggedIn = true;
                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        intent.putExtra(Constants.LOGGED_IN, isLoggedIn);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                        // App code
                    }
                });
        btn_login_Facebook.setOnClickListener(view -> LoginManager.getInstance().logInWithReadPermissions(UserLogin.this, Arrays.asList("public_profile")));
    }

    public void signInGoogle(View view){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    public void signIn (View view){
        email = et_email2.getText().toString();
        password = et_password2.getText().toString();

        if(TextUtils.isEmpty(email)){
            et_email2.setError("Email is required");
            et_email2.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            et_password2.setError("Password is required");
            et_password2.requestFocus();
        }else if(password.length() < 7){
            et_password2.setError("Password must be longer than 6 characters");
            et_password2.requestFocus();
        }else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserLogin.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                        Intent mainActivityIntent = new Intent (UserLogin.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    }else{
                        Toast.makeText(UserLogin.this, "Logged In failed", Toast.LENGTH_SHORT).show();
                        Log.d("Logged In", task.getException().getMessage());
                    }
                }
            });
        }
    }

    public void goToRegister (View view){
        Intent registerIntent = new Intent(UserLogin.this, UserRegister.class);
        startActivity(registerIntent);
    }

    public void goToResetPasswordViaEmail (View view){
        Intent intent = new Intent (UserLogin.this, UserResetPasswordViaEmail.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(UserLogin.this);
                String personName = accountInfo.getDisplayName();
                String personGivenName = accountInfo.getGivenName();
                String personFamilyName = accountInfo.getFamilyName();
                String personEmail = accountInfo.getEmail();
                String personId = accountInfo.getId();
                Uri personPhoto = accountInfo.getPhotoUrl();
                userModel = new UserModel(personName, personGivenName, personFamilyName,
                        personEmail, personId, personPhoto);
                Log.d("User info", userModel.toString());
                isLoggedIn = true;
                goToMainActivityByGoogleAccount();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "can not get result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToMainActivityByGoogleAccount() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.LOGGED_IN, isLoggedIn);
        intent.putExtra(Constants.ACCOUNT_INFO, (Parcelable) userModel);
        startActivity(intent);
    }
}