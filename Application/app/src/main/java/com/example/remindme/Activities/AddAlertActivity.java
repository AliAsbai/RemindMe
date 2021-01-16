package com.example.remindme.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.Adapters.AddLocationAdapter;
import com.example.remindme.HttpsTrustManager;
import com.example.remindme.model.Favorite;
import com.example.remindme.model.NearByPlaces;
import com.example.remindme.R;
import com.example.remindme.model.Category;
import com.example.remindme.model.Location;
import com.example.remindme.model.Reminder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.remindme.model.DeviceID.getDeviceID;

public class AddAlertActivity extends AppCompatActivity {

    private AddLocationAdapter locationAdapter;
    private LinkedList<Location> locations;
    private Button submit;
    private EditText alertTitle;
    private EditText alertDescription;
    private PopupWindow userChoice;
    private List<Location> actualLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        locations = new LinkedList<>();

        locationAdapter = new AddLocationAdapter(this, locations);
        recyclerView.setAdapter(locationAdapter);

        submit = findViewById(R.id.add_alert_submit);
        submit.setOnClickListener(submitListener);
        alertTitle = findViewById(R.id.add_alert_edittext);
        alertDescription = findViewById(R.id.editTextTextMultiLine);

        actualLocations = new ArrayList<>();
    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            String message = String.valueOf(alertTitle.getText());
            boolean emptyMessage = message.isEmpty();
            if (emptyMessage || locations.size() == 0) {
                if (emptyMessage && locations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a message and location :)", Toast.LENGTH_SHORT).show();
                } else if (emptyMessage) {
                    Toast.makeText(getApplicationContext(), "Please enter a message :)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please chose at least one location :)", Toast.LENGTH_SHORT).show();
                }
            } else {
                String id = "";
                try {
                    id = getDeviceID(getApplicationContext());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Reminder reminder = new Reminder();
                reminder.setUserID(id);
                reminder.setTask(String.valueOf(alertTitle.getText()));
                reminder.setDescription(String.valueOf(alertDescription.getText()));
                reminder.setLocations(actualLocations);
                reminder.setCategories(new ArrayList<Category>());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = reminder.toJSON();
                } catch (JSONException e) {
                    jsonObject = null;
                }

                if (jsonObject != null) {
                    HttpsTrustManager.allowAllSSL();
                    String postUrl = "https://192.168.10.245:8443/Server_war_exploded/RemindMe/Reminder/add";
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), "Reminder saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Reminder failed!", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        }
    };

    /**
     * Onclick Listener for add another location
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addLocation(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        userChoice = new PopupWindow(layoutInflater.inflate(R.layout.popup_map_or_fav, null, false), 700, 500, true);
        userChoice.setElevation(50);
        userChoice.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void choseFromMap(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(intent, 1);
        userChoice.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            String header = "";
            try {
                header = data.getStringExtra("SEARCHED");
            } catch (RuntimeException e) {
                return;
            }
            Location p = new Location();
            p.setName(header);
            List<Location> places = NearByPlaces.getPlacesNearBy();
            if (places.size() > 0) {
                locations.add(p);
                locationAdapter.notifyDataSetChanged();
                actualLocations.addAll(places);
            }
        } else if (requestCode == 2) {
            Favorite favorite = (Favorite) data.getSerializableExtra("favorite");
            Location p = new Location();
            p.setName(favorite.getName());
            List<Location> places = favorite.getLocations();
            if (places.size() > 0) {
                locations.add(p);
                locationAdapter.notifyDataSetChanged();
                actualLocations.addAll(places);
            }
        }
    }

    public void chooseFromFavorites(View view) {
        Intent intent = new Intent(getApplicationContext(), UseFavoriteActivity.class);
        startActivityForResult(intent, 2);
        userChoice.dismiss();
    }
}