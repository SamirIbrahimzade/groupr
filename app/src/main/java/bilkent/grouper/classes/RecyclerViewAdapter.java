package bilkent.grouper.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.groupr.groupr.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mContent = new ArrayList<>();
    private ArrayList<String> mGroupNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context,ArrayList<String> imageNames, ArrayList<String> contents,ArrayList<String> gnames){
        mImageNames = imageNames;
        mContent = contents ;
        mGroupNames = gnames;
        mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG,"onBindViewHolder: called.");

       // Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.image);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //CircleImageView image;
        TextView username;
        TextView contentt;
        TextView grnames;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.usname);
            contentt = itemView.findViewById(R.id.content);
            grnames = itemView.findViewById(R.id.grname);
            parentLayout = itemView.findViewById(R.id.items_layout);
        }
    }
}
