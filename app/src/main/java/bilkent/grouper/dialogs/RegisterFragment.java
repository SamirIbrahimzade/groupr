package bilkent.grouper.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupr.groupr.R;

import java.util.HashMap;
import java.util.Map;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.activities.MainActivity;
import bilkent.grouper.classes.User;

public class RegisterFragment extends DialogFragment {


    // variables
    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText confirmPasswordText;
    private Button register;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize fragment view
        View view =  inflater.inflate(R.layout.fragment_register,container,false);

        // Initialize view
        emailText = view.findViewById(R.id.registerEmail);
        usernameText = view.findViewById(R.id.registerUsername);
        passwordText = view.findViewById(R.id.registerPassword);
        confirmPasswordText = view.findViewById(R.id.registerConfirmPassword);
        register = view.findViewById(R.id.completeRegister);

        // register clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilled()){
                    if(!passwordText.getText().toString().equals(confirmPasswordText.getText().toString())){
                        // TODO Show Error Toast
                        passwordText.setError(getResources().getString(R.string.passwords_not_match));
                        confirmPasswordText.setError(getResources().getString(R.string.passwords_not_match));
                    }
                    else
                        completeRegistration(emailText.getText().toString(), passwordText.getText().toString(), usernameText.getText().toString());
                }
            }
        });
        return view;
    }

    // This method is used to check all fields are completed
    private boolean isFilled() {
        if (TextUtils.isEmpty(emailText.getText().toString())){
            emailText.setError(getResources().getString(R.string.complete));
            return false;
        }
        if (TextUtils.isEmpty(usernameText.getText().toString())) {
            usernameText.setError(getResources().getString(R.string.complete));
            return false;
        }
        if (TextUtils.isEmpty(passwordText.getText().toString())) {
            passwordText.setError(getResources().getString(R.string.complete));
            return false;
        }
        if (TextUtils.isEmpty(confirmPasswordText.getText().toString())) {
            confirmPasswordText.setError(getResources().getString(R.string.complete));
            return false;
        }
        return true;
    }

    private void completeRegistration(final String email, final String password, final String username) {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mAuth.signInWithEmailAndPassword(email,password);
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        user.updateProfile(profileUpdates);

                    }
                    User newUser = new User(username, user.getUid());
                    saveUserCredentials(newUser);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        passwordText.setError(getString(R.string.error_weak_password));
                        passwordText.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        emailText.setError(getString(R.string.error_invalid_email));
                        emailText.requestFocus();
                    } catch(FirebaseAuthUserCollisionException e) {
                        emailText.setError(getString(R.string.error_user_exists));
                        emailText.requestFocus();
                    } catch(Exception e) {
                        Toast.makeText(getContext(), e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveUserCredentials(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("Username", user.getDisplayName());
        newUser.put("ID", user.getID());
        newUser.put("Groups", null);
        newUser.put("Upcoming Meetings", null);
        DocumentReference userRef = db.collection("Users").document(user.getID());
        userRef.set(newUser);
        SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE).edit();
        sharedPreferences.putString(LoginActivity.USERNAME,user.getDisplayName()).apply();
    }


}
