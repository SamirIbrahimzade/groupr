package bilkent.grouper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupr.groupr.R;

public class ProfileFragment extends Fragment {

    // variables
    private ImageView profilePic;
    private TextView username;
    private RecyclerView groups;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // initialize current view
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        // Initialize views
        profilePic = view.findViewById(R.id.personalProfilePicture);
        username = view.findViewById(R.id.profileUsername);
        groups = view.findViewById(R.id.profileGroups);

        // profile picture click handler
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

}
