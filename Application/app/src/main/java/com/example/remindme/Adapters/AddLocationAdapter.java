package com.example.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;
import com.example.remindme.model.Location;

import java.util.LinkedList;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.AddLocationHolder> {

    protected static class AddLocationHolder extends RecyclerView.ViewHolder{

        public final TextView locationItemView;
        public final ImageButton choseFromMapView;
        public final ImageButton removeLocationView;
        public final ImageView locationImageView;


        public AddLocationHolder(@NonNull View itemView, Context context) {
            super(itemView);
            locationItemView = itemView.findViewById(R.id.location_chosen);
            locationImageView = itemView.findViewById(R.id.location_image_view);
            choseFromMapView = itemView.findViewById(R.id.location_chose_map);
            removeLocationView = itemView.findViewById(R.id.location_remove);
        }
    }


    private final LinkedList<Location> addLocationList;
    private LayoutInflater mInflater;
    private Context context;

    public AddLocationAdapter(Context context, LinkedList<Location> addLocationList){
        this.addLocationList = addLocationList;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AddLocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.add_location_item, parent, false);
        return new AddLocationHolder(mItemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AddLocationHolder holder, int position) {
        if(addLocationList != null){
            Location current = addLocationList.get(holder.getAdapterPosition());
            holder.locationItemView.setText(current.getName());
        }else {
            holder.locationItemView.setHint("Chose Location...");
        }

        holder.removeLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLocationList.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(addLocationList != null){
            System.out.println("SIZE");
            return addLocationList.size();
        }
        return 0;
    }
}
