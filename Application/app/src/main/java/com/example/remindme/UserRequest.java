package com.example.remindme;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.Interface.VolleyResponse;
import com.example.remindme.model.CurrentUserSingleton;
import com.example.remindme.model.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserRequest {

    private static RequestQueue requestQueue;
    private User newUser;

    public UserRequest(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        newUser = new User();
    }
    public boolean registerUser(User user) {
        String url ="http://192.168.10.245:8070/RemindMe/User/signUp";
        JSONObject postData = new JSONObject();
        String requestBody = "";
        try{
            postData.put("name", user.getName());
            postData.put("email", user.getEmail());
            postData.put("password", user.getPassword());
            requestBody = postData.toString();
        } catch (JSONException e) {
            return false;
        }
        String finalRequestBody = requestBody;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    newUser.setEmail(response.getString("email"));
                    newUser.setName(response.getString("name"));
                    newUser.setId(response.getString("userID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: " + error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    return finalRequestBody == null ? null : finalRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalRequestBody, "utf-8");
                    return null;
                }
            }
        };
        requestQueue.add(request);
        return true;
    }

    public static void getGoogleToken(Context context, String id, VolleyResponse volleyResponse){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        String url ="http://192.168.0.29:8070/RemindMe/User/SignInWithGoogle";
        JSONObject postData = new JSONObject();
        String requestBody = "";
        try{
            postData.put("id", id);
            requestBody = postData.toString();
        } catch (JSONException e) {
            volleyResponse.onFail();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                CurrentUserSingleton.getInstance().setId(response.toString());
                volleyResponse.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: " + error.toString());
                volleyResponse.onFail();
            }
        });
        requestQueue.add(request);
    }

    public static void validateToken(Context context, String token, VolleyResponse volleyResponse){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        String url ="http://192.168.0.29:8070/RemindMe/User/isLoggedIn";
        JSONObject postData = new JSONObject();
        try{
            postData.put("token", token);
        } catch (JSONException e) {
            volleyResponse.onFail();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    CurrentUserSingleton.getInstance().setId(response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    volleyResponse.onFail();
                }
                volleyResponse.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: " + error.toString());
                volleyResponse.onFail();
            }
        });
        requestQueue.add(request);
    }

    public static void getDeviceToken(Context context, String id, VolleyResponse volleyResponse){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        String url ="http://192.168.0.29:8070/RemindMe/User/Device";
        JSONObject postData = new JSONObject();
        try{
            postData.put("id", id);
        } catch (JSONException e) {
            volleyResponse.onFail();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                CurrentUserSingleton.getInstance().setId(response.toString());
                volleyResponse.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR: " + error.toString());
                volleyResponse.onFail();
            }
        });
        requestQueue.add(request);
    }

}