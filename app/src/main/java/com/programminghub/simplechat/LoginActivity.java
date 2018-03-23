package com.programminghub.simplechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt,passwordEt;
    Button loginBtn,signUpBtn;
    String email,password;
    FirebaseAuth loginAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if((loginAuth.getCurrentUser()==null)) {
            setContentView(R.layout.activity_login);
            defineView();
            addClickListener();
        }else{
            Intent mainIntent=new Intent(this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
    private void initView(){
        loginAuth=FirebaseAuth.getInstance();

    }
    private void defineView(){
        emailEt=findViewById(R.id.email_et);
        passwordEt=findViewById(R.id.password_et);
        loginBtn=findViewById(R.id.login_btn);
        signUpBtn=findViewById(R.id.signup_btn);
    }
    private void addClickListener(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid())
                    login();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }
    private boolean isValid(){
        boolean isValid=false;
        email=emailEt.getText().toString();
        password=passwordEt.getText().toString();
        if(TextUtils.isEmpty(email))
            emailEt.setError("Required");
        else if (TextUtils.isEmpty(password))
            passwordEt.setError("Required");
        else
            isValid=true;
        return isValid;
    }

    private void login(){
        loginAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                    Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(LoginActivity.this, "error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}
