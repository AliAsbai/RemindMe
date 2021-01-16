package com.example.remindme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remindme.R;
import com.example.remindme.model.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    protected static class LocationHolder extends RecyclerView.ViewHolder {

        public final TextView nameView;
        public final TextView addressView;
        public final TextView distanceView;
        public final TextView tagsView;

        public LocationHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            addressView = itemView.findViewById(R.id.address);
            distanceView = itemView.findViewById(R.id.distance);
            tagsView = itemView.findViewById(R.id.tags);
        }
    }

    private final List<Location> locations;
    private final LayoutInflater inflater;

    public LocationAdapter(Context context, List<Location> locations) {
        this.locations = locations;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.location_item, parent, false);
        return new LocationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {
        Location current = locations.get(holder.getAdapterPosition());
        holder.nameView.setText(current.getName());
        holder.addressView.setText(current.getAddress());
        if(current.getDistance() >= 1000) {
            holder.distanceView.setText("(" + String.format("%.1f", current.getDistance()/1000) + " km)");
        } else {
            holder.distanceView.setText("(" + String.format("%.1f", current.getDistance()) + " m)");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Tags: ");
        for(String s : current.getCategories()) {
            sb.append(s);
            if(current.getCategories().indexOf(s) != current.getCategories().size()-1) sb.append(", ");
        }
        holder.tagsView.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
