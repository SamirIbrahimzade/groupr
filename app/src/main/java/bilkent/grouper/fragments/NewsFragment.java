package bilkent.grouper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bilkent.grouper.classes.RecyclerViewAdapter;
import com.groupr.groupr.R;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mContents = new ArrayList<>();
    private ArrayList<String> mgGroupNames = new ArrayList<>();
    private ArrayList<String> groupIDs = new ArrayList<>();
    Button button1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_fragment,container,false);


      return view;


    }

    private void initImageBitmaps(){


    }
    private void initRecyclerView(){
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(),mNames,mContents,mgGroupNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setGroupIDs(ArrayList<String> groupIDs){
        this.groupIDs.addAll(groupIDs);
    }

}
