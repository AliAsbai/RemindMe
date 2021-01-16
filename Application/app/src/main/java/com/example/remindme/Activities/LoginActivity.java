package com.example.remindme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.IO.FileIO;
import com.example.remindme.IO.UserInternalStorage;
import com.example.remindme.R;
import com.example.remindme.UserRequest;
import com.example.remindme.model.CurrentUserSingleton;
import com.example.remindme.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private final static int SIGN_IN_REQUEST = 1;
    private GoogleSignInClient googleSignInClient;
    private TextView email;
    private TextView password;
    private UserRequest userRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .requestId()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButtonGoogle = findViewById(R.id.sign_in_button_google);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);
        signInButtonGoogle.setOnClickListener(signInWithGoogle);

        userRequest = new UserRequest(getApplicationContext());
        email = findViewById(R.id.Login_email);
        password = findViewById(R.id.Login_password);
        Button signInButton = findViewById(R.id.Login_signIn);
        signInButton.setOnClickListener(signIn);
    }

    View.OnClickListener signIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(email.getText().equals("")){
                email.setError("Enter a email address");
                return;
            }
            if(password.getText().equals("")){
                password.setError("Enter a password");
                return;
            }
            signIn();
        }
    };

    private void signIn(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.29:8070/RemindMe/User/signIn";
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email.getText());
            postData.put("password", password.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseResponse(response);
                UserInternalStorage user = new UserInternalStorage(CurrentUserSingleton.getInstance().getName(), CurrentUserSingleton.getInstance().getId());
                FileIO fileIO = new FileIO();
                try {
                    fileIO.writeFileToInternalStorage(getApplicationContext(), user, "user");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(Activity.RESULT_OK, new Intent());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Could not log in", Toast.LENGTH_LONG).show();
                System.out.println("ERROR: " + error.toString());
            }
        });
        requestQueue.add(request);
    }

    private static void parseResponse(JSONObject res){
        try {
            CurrentUserSingleton.getInstance().setName(res.getString("message"));
            CurrentUserSingleton.getInstance().setId(res.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener signInWithGoogle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent =  googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, SIGN_IN_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            CurrentUserSingleton.getInstance().setName(account.getDisplayName());
            CurrentUserSingleton.getInstance().setId(account.getIdToken());

            setResult(Activity.RESULT_OK, new Intent());
            finish();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivity(intent);
    }
}