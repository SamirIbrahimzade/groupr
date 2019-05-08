package bilkent.grouper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groupr.groupr.R;

import bilkent.grouper.classes.User;
import bilkent.grouper.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity {


    // variables
    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButton;
    private FirebaseAuth mAuth;

    // public
    public static User currentUser;
    public static final String USER_PREFERENCES = "USER_DETAILS";
    public static final String USERNAME = "USERNAME";
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
        registerButton = findViewById(R.id.registerButton);




        // login button click handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        // register button click handler
        registerButton.setOnClickListener(new View.OnClickListener() {
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
        if (mAuth.getCurrentUser() != null){
            currentUser = new User(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getUid());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException e){
                            Toast.makeText(getApplicationContext(),getString(R.string.error_user_not_found),Toast.LENGTH_SHORT).show();
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(getApplicationContext(), getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference userRef = db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                User user = documentSnapshot.toObject(User.class);
                                SharedPreferences.Editor editor = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE).edit().putString(USERNAME, user.getDisplayName());
                                editor.apply();
                            }
                        });
                    }
                }
            });
        }
    }

}

