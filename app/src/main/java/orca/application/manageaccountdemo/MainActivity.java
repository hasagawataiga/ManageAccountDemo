package orca.application.manageaccountdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSignIn (View view){
        Intent signInIntent = new Intent(this, UserLogin.class);
        startActivity(signInIntent);
    }

    public void goToSignUp (View view){
        Intent signUpIntent = new Intent(this, UserRegister.class);
        startActivity(signUpIntent);
    }
}