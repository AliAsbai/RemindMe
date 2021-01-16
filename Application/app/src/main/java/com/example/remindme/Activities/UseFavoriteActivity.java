package com.example.remindme.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.Adapters.FavoritesAdapter;
import com.example.remindme.Adapters.UseFavoriteAdapter;
import com.example.remindme.HttpsTrustManager;
import com.example.remindme.R;
import com.example.remindme.model.Favorite;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.remindme.model.DeviceID.getDeviceID;
import static com.example.remindme.model.parseJSON.parseFavorite;

public class UseFavoriteActivity extends AppCompatActivity {

    private String userID;
    private List<Favorite> favorites;
    private UseFavoriteAdapter adapter;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefavorite);
        favorites = new ArrayList<Favorite>();
        try {
            userID = getDeviceID(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.userFavRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UseFavoriteAdapter(favorites, this);
        recyclerView.setAdapter(adapter);
        getFavorites();
    }

    @SuppressLint("MissingPermission")
    private void getFavorites() {
        HttpsTrustManager.allowAllSSL();
        StringBuilder sb = new StringBuilder();
        sb.append("https://192.168.10.245:8443/Server_war_exploded/RemindMe/Favorite/get/");
        sb.append(userID);
        sb.append("/" + 0);
        sb.append("/" + 0);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, sb.toString(), new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.i("LOCATION", "server works");
                String str = response;
                try {
                    str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray.length() > 0) {
                    favorites = null;
                    try {
                        favorites = parseFavorite(jsonArray);
                        Collections.sort(favorites);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateAdapter();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LOCATION", "server failed");
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateAdapter() {
        adapter.setMyReminders(favorites);
        adapter.notifyDataSetChanged();
    }


}
