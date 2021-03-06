package bilkent.grouper.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupr.groupr.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.activities.MainActivity;
import bilkent.grouper.adapters.MyProfileGroupsAdapter;
import bilkent.grouper.classes.Group;
import bilkent.grouper.classes.Meeting;
import bilkent.grouper.classes.User;
import bilkent.grouper.dialogs.LoadingDialog;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    // constants
    private final int PICK_IMAGE_REQUEST = 124;
    // variables
    private ArrayList<String> groupIDs = new ArrayList<>();
    private ImageView profilePic;
    private TextView username;
    private RecyclerView groups;
    private CardView signOut;
    private Uri filePath;
    private LoadingDialog dialog = new LoadingDialog();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialog.show(getActivity().getSupportFragmentManager(),"Loading Dialog");
        // initialize current view
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        // Initialize views
        profilePic = view.findViewById(R.id.userProfilePhoto);
        username = view.findViewById(R.id.profileUsername);
        groups = view.findViewById(R.id.profileGroups);
        signOut = view.findViewById(R.id.signOut);
        // set credentials
        setUserCredentials();

        // on click listener
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return view;
    }

    private void setUserCredentials() {
        username.setText(getContext().getSharedPreferences(LoginActivity.USER_PREFERENCES, Context.MODE_PRIVATE).getString(LoginActivity.USERNAME, "Username"));
//        getGroups(getContext());
         setGroups(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setGroups(final Context context) {
        final MyProfileGroupsAdapter adapter = new MyProfileGroupsAdapter(new ArrayList<Group>(), context);
        groups.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        groups.setLayoutManager(linearLayoutManager);
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference groupDetail = firebaseFirestore.collection("Groups");
        if (groupIDs.size() > 0) {
            for (final String id : groupIDs) {
                Log.d("Tag", id);
                groupDetail.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final DocumentSnapshot documentSnapshot = task.getResult();
                        ArrayList<String> coordinatorIDs = (ArrayList<String>) documentSnapshot.get("coordinatorIDS");
                        String groupName = (String) documentSnapshot.get("groupName");
                        ArrayList<Meeting> meetings = (ArrayList<Meeting>) documentSnapshot.get("meetings");
                        String photoUri = (String) documentSnapshot.get("photo");
                        ArrayList<String> postIDs = (ArrayList<String>) documentSnapshot.get("postIDs");
                        ArrayList<User> users = (ArrayList<User>) documentSnapshot.get("users");
                        ArrayList<Map<String, String>> users1 = (ArrayList<Map<String, String>>) documentSnapshot.get("users");
                        final Group group = new Group(coordinatorIDs, groupName, new ArrayList<User>(), postIDs, meetings, photoUri);
                        if (users != null) {
                            for (Map userMap : users1) {
                                DocumentReference userRef = firebaseFirestore.collection("Users").document((String) userMap.get("id"));
                                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        String username = (String) documentSnapshot1.get("Username");
                                        String userID = (String) documentSnapshot1.get("ID");
                                        ArrayList<String> meetings = (ArrayList<String>) documentSnapshot1.get("Upcoming Meetings");
                                        ArrayList<String> groupIDs = (ArrayList<String>) documentSnapshot1.get("Groups");
                                        User user = new User(username, userID, meetings, groupIDs);
                                        group.setGroupID(id);
                                        group.addUser(user);
                                        adapter.addGroup(group);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }else{
            dialog.dismiss();
        }
    }

    public void getGroups(final Context context) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference groupsRef = firebaseFirestore.collection("Users").document(LoginActivity.currentUser.getID());
        groupsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot != null && documentSnapshot.contains("Groups")) {
                    ArrayList<String> results = (ArrayList<String>) documentSnapshot.get("Groups");
                    if (results != null) {
                        setGroups(context);
                    }
                }else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGroupIDs(ArrayList<String> groupIDs) {
        this.groupIDs.addAll(groupIDs);
    }
}
