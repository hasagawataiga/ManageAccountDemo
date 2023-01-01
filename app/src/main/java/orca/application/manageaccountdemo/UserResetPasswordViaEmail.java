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
import com.google.firebase.auth.FirebaseAuth;

public class UserResetPasswordViaEmail extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button btn_resetEmail;
    EditText et_resetEmail;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password_via_email);
        et_resetEmail = findViewById(R.id.et_resetEmail);
        btn_resetEmail = findViewById(R.id.btn_resetEmail);

    }
    public void resetPassword (View view){
        email = et_resetEmail.getText().toString();
        if(TextUtils.isEmpty(email)){
            et_resetEmail.setError("Email is required");
        }else{
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserResetPasswordViaEmail.this, "Sent an email to " + email, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserResetPasswordViaEmail.this, UserLogin.class);
                                startActivity(intent);
                                Log.d("Reset email", "Email sent to " + email);
                            }else{
                                Toast.makeText(UserResetPasswordViaEmail.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                                Log.d("Reset email", "Email is wrong");
                            }
                        }
                    });
        }
    }
}