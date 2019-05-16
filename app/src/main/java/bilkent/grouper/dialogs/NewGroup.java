package bilkent.grouper.dialogs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupr.groupr.R;

import java.io.IOException;
import java.util.Arrays;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.activities.MainActivity;
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
    private Uri fileUri;
    private String filePath;
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
        photo = view.findViewById(R.id.createGroupImage);
        groupName = view.findViewById(R.id.createGroupName);
        createGroup = view.findViewById(R.id.createGroupButton);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            filePath = getPath(getActivity().getApplicationContext(), fileUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), fileUri);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(Context applicationContext, Uri fileUri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = applicationContext.getContentResolver().query(fileUri,proj,null,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                int column_index = cursor.getColumnIndex(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null){
            Toast.makeText(applicationContext,getString(R.string.no_photo),Toast.LENGTH_SHORT).show();
            return null;
        }
        return result;
    }

    private void createGroup() {
        if (TextUtils.isEmpty(groupName.getText().toString()))
            groupName.setError(getString(R.string.complete));
        else {
            final LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.show(getActivity().getSupportFragmentManager(),"Loading Dialog");
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final Group group = new Group();
            group.setGroupName(groupName.getText().toString());
            group.setGroupID(id);
            final DocumentReference documentReference = firebaseFirestore.collection("Groups").document(id);
            final DocumentReference userGroupsRef = firebaseFirestore.collection("Users").document(LoginActivity.currentUser.getID());
            if (fileUri != null){
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Groups").child(id).child("Group Photo").child(id);
                storageReference.putFile(fileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                                userGroupsRef.update("Groups", FieldValue.arrayUnion(id));
                                final Group newGroup = new Group();
                                newGroup.setGroupName(groupName.getText().toString());
                                newGroup.setCoordinatorIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                                newGroup.setMeetings(null);
                                newGroup.setUsers(Arrays.asList(LoginActivity.currentUser));
                                newGroup.setPostIDs(null);
                                final Uri[] downloadUri = new Uri[1];
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        downloadUri[0] = task.getResult();
                                        newGroup.setPhoto(downloadUri[0].toString());
                                        documentReference.set(newGroup);
                                        addGroup(newGroup.getGroupID());
                                    }
                                });
                                saveUserGroup(group,filePath);
                                Toast.makeText(getContext(),getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismiss();
                                Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) ((100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                                loadingDialog.setProgress(progress);
                            }
                        });
            }else {
                userGroupsRef.update("Groups", FieldValue.arrayUnion(id));
                final Group newGroup = new Group();
                newGroup.setGroupName(groupName.getText().toString());
                newGroup.setCoordinatorIDs(Arrays.asList(LoginActivity.currentUser.getID()));
                newGroup.setMeetings(null);
                newGroup.setUsers(Arrays.asList(LoginActivity.currentUser));
                newGroup.setPostIDs(null);
                newGroup.setPhoto(null);
                documentReference.set(newGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MainActivity.groupIDs.add(newGroup.getGroupID());
                        Toast.makeText(getContext(),getString(R.string.successfully_created),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        saveUserGroup(group, filePath);
                        dismiss();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getContext(),getString(R.string.cancel_new_group),Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),getString(R.string.error_unexpected) + " \n" + getString(R.string.check_internet),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }

    private void saveUserGroup(Group group, String filePath) {

    }

    private void addGroup(String groupID) {
        MainActivity.groupIDs.add(groupID);
    }
}

