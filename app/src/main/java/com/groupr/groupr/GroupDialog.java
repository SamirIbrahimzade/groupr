package com.groupr.groupr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.KeyStore;

public class GroupDialog extends DialogFragment{
    private  static final String TAG = "GroupDialog";

    EditText et1;
    Button join;
    Button joinButton;
    RelativeLayout rel2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Groups");
        View view = inflater.inflate(R.layout.fragment_group_dialog,container,false);

        et1 = view.findViewById(R.id.editTextJoinId);
        join = view.findViewById(R.id.button2);
        rel2 = view.findViewById(R.id.rel2);
        joinButton = view.findViewById(R.id.button4);


        et1.setVisibility(View.INVISIBLE);
        joinButton.setVisibility(View.INVISIBLE);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setVisibility(View.VISIBLE);
                joinButton.setVisibility(View.VISIBLE);
            }
        });





        return view;
    }

}
