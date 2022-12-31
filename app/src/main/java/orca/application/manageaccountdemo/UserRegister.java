package orca.application.manageaccountdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserRegister extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    Button btn_register;
    String email;
    String password;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_register);

        // Email SignIn
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        email = et_email.getText().toString();
        password = et_password.getText().toString();

        if(TextUtils.isEmpty(email)){
            et_email.setError("Email is required");
            et_email.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            et_password.setError("Password is required");
            et_password.requestFocus();
        }else if(password.length() < 7){
            et_password.setError("Password must be longer than 6 characters");
            et_password.requestFocus();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserRegister.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UserRegister.this, MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(UserRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        Log.d("Registration", task.getException().getMessage());
                    }
                }
            });
        }
    }

    public void goToLogin (View view){
        Intent intent = new Intent(UserRegister.this, UserLogin.class);
        startActivity(intent);
    }

    public void returnMainActivity (View view){
        Intent intent = new Intent(UserRegister.this, MainActivity.class);
        startActivity(intent);
    }
}