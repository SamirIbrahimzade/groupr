package com.groupr.groupr;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;

public class FirstPage extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button logButton;
    private Button btn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        mAuth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailEt);
        passwordField = findViewById(R.id.passwordEt);
        logButton = findViewById(R.id.loginButton);
        btn = findViewById(R.id.btn222);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Log.d("mail of user",user.getEmail().toString());
                if(user != null){
                    startActivity(new Intent(getApplicationContext(),MainPage.class));
                    Toast.makeText(FirstPage.this,"Succesful login",Toast.LENGTH_LONG).show();
                }
            }
        };

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(FirstPage.this,"Field's are empty",Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Toast.makeText(FirstPage.this, "Signin problem", Toast.LENGTH_LONG).show();
                    if (!task.isSuccessful()) {
                        Toast.makeText(FirstPage.this, "Signin problem", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }

}

