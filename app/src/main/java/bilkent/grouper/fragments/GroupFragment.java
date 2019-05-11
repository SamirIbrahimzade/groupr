package bilkent.grouper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.Document;
import com.groupr.groupr.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.classes.RandomString;
import bilkent.grouper.classes.User;
import bilkent.grouper.dialogs.NewGroup;
import bilkent.grouper.dialogs.RegisterFragment;

public class GroupFragment extends Fragment {


    // variables
    private Button myGroupsButton;
    private Button joinGroupButton;
    private Button newGroupButton;
    private FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,container,false);
        // initialize views
        myGroupsButton = view.findViewById(R.id.groupsFragmentMyGroups);
        joinGroupButton = view.findViewById(R.id.groupsFragmentJoin);
        newGroupButton = view.findViewById(R.id.groupsFragmentNewGroup);

        // initialize database ref
        db = FirebaseFirestore.getInstance();

        // my groups clicked

        // joinGroupButton

        // create groups clicked
        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });

        return view;
    }

    private void createGroup() {
        final String id = RandomString.generateRandomString(6);
        final DocumentReference newGroupReference = db.collection("Groups").document(id);
        newGroupReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        if (doc.exists()){
                            createGroup();
                        }
                        else {
                            FragmentManager mFragmentManager = getChildFragmentManager();
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            Fragment prev = mFragmentManager.findFragmentByTag("New Group Dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);
                            NewGroup groupDialog = new NewGroup();
                            Bundle args = new Bundle();
                            args.putString("GroupID",id);
                            groupDialog.setArguments(args);
                            groupDialog.show(ft,"Create Group");
                        }
                    }
                }
            }
        });
    }
}
