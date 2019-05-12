package bilkent.grouper.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.Document;
import com.groupr.groupr.R;

import java.util.ArrayList;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.classes.Group;
import bilkent.grouper.dialogs.LoadingDialog;

import static bilkent.grouper.activities.MainActivity.userGroups;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    // variables
    private ImageView profilePic;
    private TextView username;
    private RecyclerView groups;
    private CardView signOut;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LoadingDialog dialog = new LoadingDialog();
        dialog.show(getActivity().getSupportFragmentManager(),"Loading Dialog");
        // initialize current view
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        // Initialize views
        profilePic = view.findViewById(R.id.personalProfilePicture);
        username = view.findViewById(R.id.profileUsername);
        groups = view.findViewById(R.id.profileGroups);
        signOut = view.findViewById(R.id.signOut);
        // set credentials
        setUserCredentials();

        return view;
    }

    private void setUserCredentials() {
        username.setText(getContext().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE).getString(LoginActivity.USERNAME, "Username"));
        setGroups();
    }

    private void setGroups() {
        ArrayList<String>groupIDS = new ArrayList<>();
        groupIDS = getGroups(getContext(), groupIDS);
        Toast.makeText(getContext(),groupIDS.size() + " size", Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> getGroups(final Context context, final ArrayList<String> groups) {
        final ArrayList<String> groupsID = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference groupsRef = firebaseFirestore.collection("Users").document(LoginActivity.currentUser.getID());
        groupsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                ArrayList<String> result = (ArrayList<String>) documentSnapshot.get("Groups");
                for (String string: result){
                    groupsID.add(string);
                    groups.add(string);
                    Toast.makeText(context,string + " String", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return groupsID;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.personalProfilePicture){

        }else if (v.getId() == R.id.signOut){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }
    }
}
