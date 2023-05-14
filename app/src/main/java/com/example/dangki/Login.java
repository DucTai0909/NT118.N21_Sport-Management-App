package com.example.dangki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button loginBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
        }
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPass);

        loginBtn = findViewById(R.id.btDN);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmpty()){
                    String uEmail = email.getText().toString().trim();
                    String uPass = password.getText().toString().trim();

                    ProgressDialog progressDialog = new ProgressDialog(Login.this);

                    progressDialog.setTitle("Loading");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(true); // Cho phép hủy
                    progressDialog.show();
                    fAuth.signInWithEmailAndPassword(uEmail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Dangnhapthanhcong.class));
                                finish();
                            }else{
                                Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public boolean checkEmpty(){
        String mEmail = email.getText().toString().trim();
        String mPass = password.getText().toString().trim();
        if(TextUtils.isEmpty(mEmail)) {
            email.setError("Email không được trống");
            return false;
        }else if(TextUtils.isEmpty(mPass)){
            password.setError("Mật khẩu không được trống");
            return false;
        }
        return true;
    }
}
