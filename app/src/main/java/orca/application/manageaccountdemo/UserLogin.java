package orca.application.manageaccountdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.common.SignInButton;

public class UserLogin extends AppCompatActivity {
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        signInButton = findViewById(R.id.sign_in_button);
    }
}