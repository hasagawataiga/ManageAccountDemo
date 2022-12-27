package orca.application.manageaccountdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

public class UserLogin extends AppCompatActivity {
    Button btn_login_Google;
    Button btn_login_Facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }
}