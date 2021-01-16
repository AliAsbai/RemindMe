package com.example.remindme.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.remindme.model.NearByPlaces;
import com.example.remindme.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private final String apiKey = "AIzaSyCbu1eFV-FZ1--RQ5hxpCshwJzuEn9sGuk";
    private static String TAG = "TESTING";
    private FusedLocationProviderClient locationProvider;
    private double latitude, longitude;
    private LatLng userPosition;
    private final int proxRadius = 15000;
    private View searchBar;
    private FloatingActionButton floatingAddAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        floatingAddAlert = findViewById(R.id.fab);
        floatingAddAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showCreateAlertPopUp();
            }
        });

        FloatingActionButton floatingFindSupermarkets = findViewById(R.id.supermarkets);
        floatingFindSupermarkets.setOnClickListener(searchListener);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = LocationServices.getFusedLocationProviderClient(this);


        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        //initiateAutoCompleteSearchBar();

       searchBar = (EditText) findViewById(R.id.search_input);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationAccess();
        if(userPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 17));
        }
    }


    private void showCreateAlertPopUp(){
        if(NearByPlaces.getPlacesNearBy().size() != 0) {
            setResult(Activity.RESULT_OK, new Intent().putExtra("SEARCHED", String.valueOf(((EditText) searchBar).getText())));
            finish();
        }else {
            createToast("No search result");
        }
    }

    private void initiateAutoCompleteSearchBar(){
        AutocompleteSupportFragment supportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.search_input);
        if (supportFragment != null) {
            supportFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.TYPES));
        }
        supportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName());
                LatLng locationSelected = place.getLatLng();
                if (locationSelected != null) {
                    Log.i(TAG, place.getTypes().toString());
                    mMap.addMarker(new MarkerOptions().position(locationSelected).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationSelected, 17));
                }
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    private void getLocationAccess() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        userPosition = new LatLng(latitude, longitude);
                    }
                }
            }) ;
        } else {
            createToast("No location access");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }else{
                createToast("Location access not granted");
                finish();
            }
        }
    }

    private void createToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 0 = map, 1 = url
            Object[] dataTransfer = new Object[2];
            NearByPlaces nearByPlaces = new NearByPlaces();

            String searchWords = String.valueOf(((EditText) searchBar).getText());

            if(searchWords.isEmpty()){
                createToast("Please search for something..");
            }else {
                dataTransfer[0] = mMap;
                dataTransfer[1] = getUrl(latitude, longitude, searchWords);
                nearByPlaces.execute(dataTransfer);
                createToast("Searching nearby..");
                mMap.clear();
            }
        }
    };

    private String getUrl(double latitude, double longitude, String type){

        type = type.trim();
        type = type.replaceAll(" ", "+");

        StringBuilder apiURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        apiURL.append("query=" + type);
        apiURL.append("&location=" + latitude + "," + longitude);
        apiURL.append("&radius=" + proxRadius);
        //apiURL.append("&type=" + );

        apiURL.append("&key=" + apiKey);

        Log.i(TAG, apiURL.toString());

        return apiURL.toString();
    }


    public void submitAlert(View view) {
        createToast("Hello. " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}