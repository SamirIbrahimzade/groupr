package bilkent.grouper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.groupr.groupr.R;

import java.util.ArrayList;

import bilkent.grouper.classes.Group;
import bilkent.grouper.classes.User;

public class MyProfileGroupsAdapter extends RecyclerView.Adapter<MyProfileGroupsAdapter.GroupHolder> {


    // variables
    private ArrayList<Group> groups;
    private Context mContext;
    public MyProfileGroupsAdapter(ArrayList<Group> groups, Context context){
        this.groups = groups;
        this.mContext = context;
    }
    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list,parent,false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        Group currentGroup = groups.get(position);
        if (currentGroup.getPhoto() != null)
            Glide.with(mContext).asBitmap().load(currentGroup.getPhoto()).into(holder.groupPhoto);
        holder.groupName.setText(currentGroup.getGroupName());
        StringBuilder members = new StringBuilder("(");
        for (User user: currentGroup.getUsers()){
            members.append(user.getDisplayName()).append(",");
        }
        members.replace(members.length() - 1,members.length(),")");
        holder.groupMembers.setText(members);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void addGroup(Group group){
        groups.add(group);
        notifyDataSetChanged();
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        ImageView groupPhoto;
        TextView groupName;
        TextView groupMembers;

        GroupHolder(View itemView) {
            super(itemView);
            groupPhoto = itemView.findViewById(R.id.list_groupPhoto);
            groupName = itemView.findViewById(R.id.list_groupName);
            groupMembers = itemView.findViewById(R.id.list_groupMembers);

        }
    }
}
