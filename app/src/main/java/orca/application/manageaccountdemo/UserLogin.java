package orca.application.manageaccountdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;

public class UserLogin extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button btn_login_Google;
    Button btn_login_Facebook;
    CallbackManager callbackManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        btn_login_Google = findViewById(R.id.btn_login_Google);
        btn_login_Facebook = findViewById(R.id.btn_login_Facebook);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(UserLogin.this, MainActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                goToMainActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "can not get result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}