package bilkent.grouper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupr.groupr.R;

import java.util.ArrayList;

import javax.xml.transform.Templates;

import bilkent.grouper.classes.Group;

public class NavigationDrawerGroupsAdaper extends RecyclerView.Adapter<NavigationDrawerGroupsAdaper.GroupHolder> {



    // variables
    private ArrayList<Group> mGroups;


    public NavigationDrawerGroupsAdaper(){
        mGroups = new ArrayList<>();
    }
    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_group_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
            holder.groupName.setText(mGroups.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void addGroup(Group group){
        mGroups.add(group);
        notifyDataSetChanged();
    }

    public void setGroups(ArrayList<Group> groups) {
        mGroups.addAll(groups);
    }

    class GroupHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        public GroupHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.drawer_group_name);
        }
    }
}