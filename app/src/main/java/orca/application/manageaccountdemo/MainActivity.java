package orca.application.manageaccountdemo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout_Menu;
    TextView tv_userName;
    Button btn_signIn;
    Button btn_signOut;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    UserModel userModel;
    boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Email SignIn
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            isLoggedIn = true;
        }

        // Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();
        googleSignInClient=GoogleSignIn.getClient(getApplicationContext(),gso);

        linearLayout_Menu = findViewById(R.id.linearLayout_Menu);
        tv_userName = findViewById(R.id.tv_userName);
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signOut = findViewById(R.id.btn_signOut);
        Bundle bundle = getIntent().getExtras();
        try{
            isLoggedIn = bundle.getBoolean(Constants.LOGGED_IN);
            userModel = bundle.getParcelable(Constants.ACCOUNT_INFO);
            takeAccountInfoGoogle();
        }catch (Exception e){
            e.getMessage();
        }
        updateUI();
    }

    private void updateUI() {
        if(isLoggedIn){
            tv_userName.setVisibility(View.VISIBLE);
            btn_signOut.setVisibility(View.VISIBLE);
            btn_signIn.setVisibility(View.GONE);
            displayUserName();
        }else{
            tv_userName.setVisibility(View.GONE);
            btn_signOut.setVisibility(View.GONE);
            btn_signIn.setVisibility(View.VISIBLE);
        }
    }

    private void takeAccountInfoGoogle(){
        String personName = userModel.getPersonName();
        String personGivenName = userModel.getPersonGivenName();
        String personFamilyName = userModel.getPersonFamilyName();
        String personEmail = userModel.getPersonEmail();
        String personId = userModel.getPersonId();
        Uri personPhoto = userModel.getPersonPhoto();
        userModel = new UserModel(personName, personGivenName, personFamilyName, personEmail, personId, personPhoto);
    }

    private void displayUserName(){
        try{
            String personName = userModel.getPersonName();
            Log.d("DisplayUserName", personName);
            tv_userName.setText(personName);
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void goToSignIn (View view){
        Intent signInIntent = new Intent(this, UserLogin.class);
        startActivity(signInIntent);
    }

    public void signOut (View view){
        firebaseAuth.signOut();
        logoutFromFacebook();
        logoutFromGoogle();
        isLoggedIn = false;
        updateUI();
    }

    private void logoutFromGoogle(){
        googleSignInClient.signOut().addOnCompleteListener(this, task -> Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show());
    }

    private void logoutFromFacebook(){
        LoginManager.getInstance().logOut();
    }
}