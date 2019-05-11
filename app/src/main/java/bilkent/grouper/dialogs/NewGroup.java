package bilkent.grouper.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupr.groupr.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.classes.Group;

import static android.app.Activity.RESULT_OK;

public class NewGroup extends DialogFragment {


    // variables
    private TextView choosePhoto;
    private ImageView photo;
    private EditText groupName;
    private Button  createGroup;
    private String id;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            id = args.getString("GroupID","NULL");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_group,container,false);

        // initialize views
        choosePhoto = view.findViewById(R.id.newGroupDialogChoosePhoto);
        photo = view.findViewById(R.id.createGroupImage);
        groupName = view.findViewById(R.id.createGroupName);
        createGroup = view.findViewById(R.id.createGroupButton);


        // photo clicked
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        // create group clicked
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = new ProgressBar(getContext());
                progressBar.setProgress(0);
                if (TextUtils.isEmpty(groupName.getText().toString()))
                    groupName.setError(getString(R.string.complete));
                else {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    final DocumentReference documentReference = firebaseFirestore.collection("Groups").document(id);
                    final DocumentReference userGroupsRef = firebaseFirestore.collection("Users").document(LoginActivity.currentUser.getID());
                    if (filePath != null){
                        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Groups").child(id).child("Group Photo").child(id);
                        storageReference.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                                userGroupsRef.update("Groups", FieldValue.arrayUnion(id));
                                final Group newGroup = new Group();
                                newGroup.setGroupName(groupName.getText().toString());
                                newGroup.setCoordinatorIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                                newGroup.setMeetings(null);
                                newGroup.setUserIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                                newGroup.setPostIDs(null);
                                final Uri[] downloadUri = new Uri[1];
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        downloadUri[0] = task.getResult();
                                        newGroup.setPhoto(downloadUri[0].toString());
                                        Log.d("Tag",newGroup.getPhoto());
                                        documentReference.set(newGroup);
                                    }
                                });
                                Toast.makeText(getContext(),getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();;
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) ((100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                                progressBar.setProgress(progress);
                            }
                        });
                    }else {
                        userGroupsRef.update("Groups", FieldValue.arrayUnion(id));
                        Group newGroup = new Group();
                        newGroup.setGroupName(groupName.getText().toString());
                        newGroup.setCoordinatorIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                        newGroup.setMeetings(null);
                        newGroup.setUserIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                        newGroup.setPostIDs(null);
                        newGroup.setPhoto(null);
                        documentReference.set(newGroup);
                        Toast.makeText(getContext(),getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
            }
        });
        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(Intent.createChooser(intent, "Select Group Photo"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
