package com.example.remindme.Adapters;

import android.content.Context;
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
import com.example.remindme.HttpsTrustManager;
import com.example.remindme.R;
import com.example.remindme.model.Favorite;
import com.example.remindme.model.Reminder;

import java.util.Collections;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {

    protected static class FavoritesHolder extends RecyclerView.ViewHolder {

        public final TextView nameView;
        public final RecyclerView locationView;
        public final Button remove;
        public final ImageButton toggle;
        public final LinearLayout topBar;
        public boolean toggled;

        public FavoritesHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.favoriteName);
            locationView = itemView.findViewById(R.id.favoriteRecycler_location);
            remove = itemView.findViewById(R.id.favoriteRemove);
            toggle = itemView.findViewById(R.id.favoriteToggle);
            topBar = itemView.findViewById(R.id.favoriteTopBar);
            toggled = true;
        }
    }

    private List<Favorite> favorites;
    private final LayoutInflater inflater;
    private final Context context;

    public FavoritesAdapter(Context context, List<Favorite> favorites) {
        this.favorites = favorites;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.favourites_item, parent, false);
        return new FavoritesHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {
        Favorite current = favorites.get(holder.getAdapterPosition());
        Collections.sort(current.getLocations());
        holder.nameView.setText(current.getName());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.locationView.setLayoutManager(layoutManager);
        LocationAdapter adapter = new LocationAdapter(context, current.getLocations());
        holder.locationView.setAdapter(adapter);
        holder.locationView.setVisibility(View.GONE);
        holder.topBar.setZ(100);
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.toggled) {
                    holder.toggle.setImageResource(R.drawable.down_arrow);
                    holder.locationView.setVisibility(View.VISIBLE);
                    holder.locationView.animate().translationY(holder.topBar.getY()).setDuration(1000);
                } else {
                    holder.toggle.setImageResource(R.drawable.left_arrow);
                    holder.locationView.animate().translationY(holder.topBar.getY() - holder.locationView.getHeight()).setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    holder.locationView.setVisibility(View.GONE);
                                }
                            });
                }
                holder.toggled = !holder.toggled;
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                String url = "https://192.168.10.245:8443/Server_war_exploded/RemindMe/Favorite/Remove/" + current.getID();
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest jsonObjectRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOCATION", "remove success");
                        favorites.remove(current);
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOCATION", "remove failed");
                        error.printStackTrace();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                Log.i("LOCATION", "WORKS!!!");
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