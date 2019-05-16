package bilkent.grouper.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.groupr.groupr.R;

import java.util.ArrayList;

import bilkent.grouper.activities.LoginActivity;
import bilkent.grouper.activities.MainActivity;
import bilkent.grouper.adapters.NavigationDrawerGroupsAdaper;
import bilkent.grouper.classes.Group;

public class NavigationDrawer extends DialogFragment {


    // variables
    private RecyclerView groups;
    private ArrayList<Group> mGroups = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_drawer,container,false);
        view.findViewById(R.id.transparent).setBackground(new ColorDrawable(Color.TRANSPARENT));
        ImageView imageView = view.findViewById(R.id.close);
        groups = view.findViewById(R.id.drawer_groups);

        NavigationDrawerGroupsAdaper adapter = new NavigationDrawerGroupsAdaper();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter.setGroups(mGroups);
        groups.setLayoutManager(linearLayoutManager);
        groups.setAdapter(adapter);

        // on click listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.NavigationDrawerTheme;
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.NavigationDrawerTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        getActivity().findViewById(R.id.container).setAlpha(1);
    }

    public void setGroups(ArrayList<Group> groupIDs) {
             mGroups.addAll(groupIDs);
         for (Group group: groupIDs){
            Log.d("Group",group.getGroupName());
        }
    }
}
