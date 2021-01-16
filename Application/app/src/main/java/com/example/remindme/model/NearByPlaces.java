package com.example.remindme.model;

import android.os.AsyncTask;

import com.example.remindme.FetchMapsData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class  NearByPlaces extends AsyncTask<Object, String, String> {

    private String googlePlaceData, url;
    private static List<Location> placesNearBy;
    private GoogleMap map;

    @Override
    protected String doInBackground(Object... objects) {

        map = (GoogleMap) objects[0];
        url = (String) objects[1];
        FetchMapsData fetchData = new FetchMapsData();
        try {
            googlePlaceData = fetchData.ReadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        PlacesParser parser = new PlacesParser();
        placesNearBy = new LinkedList<>();
        placesNearBy = parser.parseData(s);
        MarkNearByPlaces(placesNearBy);
    }

    public static List<Location> getPlacesNearBy(){
        if(placesNearBy != null){
            return placesNearBy;
        }
        return null;
    }

    private void MarkNearByPlaces(List<Location> placesNearBy){
        for (int i = 0; i < placesNearBy.size() ; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            Location  pNear = placesNearBy.get(i);
            String placeName = pNear.getName();
            double latitude = pNear.getLat();
            double longitude = pNear.getLong();

            markerOptions.position(new LatLng(latitude, longitude));
            markerOptions.title(placeName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
        }
    }

}
