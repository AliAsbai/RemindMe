package com.example.remindme.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.Adapters.FavoritesAdapter;
import com.example.remindme.HttpsTrustManager;
import com.example.remindme.R;
import com.example.remindme.model.Favorite;
import com.example.remindme.model.NearByPlaces;
import com.example.remindme.model.Reminder;
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
import static com.example.remindme.model.parseJSON.parseReminder;

public class FavoritesActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private FusedLocationProviderClient locationProvider;
    private String userID;
    private List<Favorite> favorites;
    private FavoritesAdapter adapter;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favorites = new ArrayList<Favorite>();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
        locationProvider.getLastLocation();
        Intent intent = getIntent();
        try {
            userID = getDeviceID(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.favouriteRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FavoritesAdapter(getApplicationContext(), favorites);
        recyclerView.setAdapter(adapter);
        getLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    HttpsTrustManager.allowAllSSL();
                    StringBuilder sb = new StringBuilder();
                    sb.append("https://192.168.10.245:8443/Server_war_exploded/RemindMe/Favorite/get/");
                    sb.append(userID);
                    sb.append("/" + location.getLongitude());
                    sb.append("/" + location.getLatitude());
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
            }
        });
    }

    private void updateAdapter() {
        adapter.setMyReminders(favorites);
        adapter.notifyDataSetChanged();
    }

    public void addFavorite(View view) {
        Intent intent = new Intent(this, addFavoritesActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            boolean added = data.getBooleanExtra("added", false);
            if(added) {
                getLocation();
            }
        }
    }

}