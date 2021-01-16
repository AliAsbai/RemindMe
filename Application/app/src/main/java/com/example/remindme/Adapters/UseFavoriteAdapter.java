package com.example.remindme.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.R;
import com.example.remindme.model.Favorite;
import com.example.remindme.model.Reminder;

import java.util.Collections;
import java.util.List;

public class UseFavoriteAdapter extends RecyclerView.Adapter<UseFavoriteAdapter.UseFavoriteHolder> {

    protected static class UseFavoriteHolder extends RecyclerView.ViewHolder {

        public final TextView nameView;
        public final Button useView;

        public UseFavoriteHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.usefavoriteName);
            useView = itemView.findViewById(R.id.usefavoriteUse);
        }
    }

    private List<Favorite> favorites;
    private final LayoutInflater inflater;
    private final Activity activity;

    public UseFavoriteAdapter(List<Favorite> favorites, Activity activity) {
        this.favorites = favorites;
        inflater = LayoutInflater.from(activity.getApplicationContext());
        this.activity = activity;
    }

    @NonNull
    @Override
    public UseFavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.usefavorite_item, parent, false);
        return new UseFavoriteHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull UseFavoriteHolder holder, int position) {
        Favorite current = favorites.get(holder.getAdapterPosition());
        Collections.sort(current.getLocations());
        holder.nameView.setText(current.getName());

        holder.useView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_OK, new Intent().putExtra("favorite", current));
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public List<Favorite> getMyReminders() {
        return favorites;
    }

    public void setMyReminders(List<Favorite> favorites) {
        this.favorites = favorites;
    }

}
