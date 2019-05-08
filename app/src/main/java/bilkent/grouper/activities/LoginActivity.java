package bilkent.grouper.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.groupr.groupr.R;

import bilkent.grouper.classes.User;
import bilkent.grouper.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {


    // variables
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButtono;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // public
    public static User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        emailField = findViewById(R.id.emailEt);
        passwordField = findViewById(R.id.passwordEt);
        loginButton = findViewById(R.id.loginButton);
        registerButtono = findViewById(R.id.registerButton);


        // Firebase Authenticator Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUser = new User(user.getDisplayName());
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };


        // login button click handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        // register button click handler
        registerButtono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterDialog();
            }
        });
    }

    private void openRegisterDialog() {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment prev = mFragmentManager.findFragmentByTag("Register Dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        RegisterFragment dialogFragment = new RegisterFragment ();
        dialogFragment.show(ft,"Register Dialog");

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
            Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //Toast.makeText(LoginActivity.this, "Signin problem", Toast.LENGTH_LONG).show();
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Signin problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}

