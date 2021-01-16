package com.example.remindme.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remindme.IO.FileIO;
import com.example.remindme.IO.UserInternalStorage;
import com.example.remindme.Interface.VolleyResponse;
import com.example.remindme.NotificationService;
import com.example.remindme.R;

import com.example.remindme.UserRequest;
import com.example.remindme.model.CurrentUserSingleton;
import com.example.remindme.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import static com.example.remindme.model.DeviceID.getDeviceID;


public class MainActivity extends AppCompatActivity {

    private ImageButton loginButton;
    private TextView welcomeMessage;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileIO fileIO = new FileIO();

        if(!fileIO.fileExists(getApplicationContext(), "deviceID")){
            try {
                fileIO.writeFileToInternalStorage(getApplicationContext(), getDeviceID(this), "user");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);

        loginButton = findViewById(R.id.main_button_login);
        logOut = findViewById(R.id.logOut);
        welcomeMessage = findViewById(R.id.main_welcome_message);
        welcomeMessage.setVisibility(View.INVISIBLE);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            CurrentUserSingleton.getInstance().setName(account.getDisplayName());
            googleToken(account);
            loginButton.setClickable(false);
            loginButton.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Test 1", Toast.LENGTH_SHORT).show();
            welcomeMessage.setText("Welcome " + account.getGivenName());
            welcomeMessage.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.VISIBLE);
            logOut.setClickable(true);
        }else if(fileIO.fileExists(getApplicationContext(), "user")){
            try {
                UserInternalStorage user = (UserInternalStorage) fileIO.readFileInInternalStorage(this, "user");
                CurrentUserSingleton.getInstance().setName(user.getName());
                CurrentUserSingleton.getInstance().setId(user.getId());
                if(!CurrentUserSingleton.getInstance().getName().equals("DeviceId")) {
                    loginButton.setClickable(false);
                    loginButton.setVisibility(View.INVISIBLE);
                    welcomeMessage.setText(String.format("%s %s", getString(R.string.welcome), CurrentUserSingleton.getInstance().getName()));
                    welcomeMessage.setVisibility(View.VISIBLE);
                    logOut.setVisibility(View.VISIBLE);
                    logOut.setClickable(true);
                    validateToken(user.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                CurrentUserSingleton.getInstance().setId((String) fileIO.readFileInInternalStorage(getApplicationContext(), "deviceID"));
                CurrentUserSingleton.getInstance().setName("DeviceID");
                deviceToken();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(loginButton.isClickable()){
            logOut.setVisibility(View.INVISIBLE);
        }
    }

    private void deviceToken(){
        UserRequest.getDeviceToken(getApplicationContext(), CurrentUserSingleton.getInstance().getId(), new VolleyResponse() {
            @Override
            public void onSuccess() {
                logOut.setVisibility(View.VISIBLE);
                validateToken(CurrentUserSingleton.getInstance().getId());
            }

            @Override
            public void onFail() {

            }
        });

    }

    private void googleToken(GoogleSignInAccount account){
        UserRequest.getGoogleToken(getApplicationContext(),account.getId(), new VolleyResponse() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                logOut.setVisibility(View.VISIBLE);
                storeActiveUser();
            }
            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), "Could not auto login, log in again", Toast.LENGTH_SHORT).show();
                logOut(null);
            }
        });
    }

    private void storeActiveUser(){
        FileIO fileIO = new FileIO();
        UserInternalStorage user = new UserInternalStorage(CurrentUserSingleton.getInstance().getName(), CurrentUserSingleton.getInstance().getId());
        try {
            fileIO.writeFileToInternalStorage(getApplicationContext(), user, "user");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void validateToken(String token){
        UserRequest.validateToken(getApplicationContext(),token, new VolleyResponse() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                logOut.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), "Could not auto login, log in again", Toast.LENGTH_SHORT).show();
                logOut(null);
            }
        });

    }

    /** On Click handler for Create Reminder button **/
    public void addAlert(View view) {
        Intent intent = new Intent(getApplicationContext(), AddAlertActivity.class);
        startActivity(intent);
    }

    /** On Click handler for My Favorites button **/
    public void goToMyFavorites(View view) {
        Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
        startActivity(intent);
    }

    /** On Click handler for My Reminders button **/
    public void goToMyReminders(View view) {
        Intent intent = new Intent(getApplicationContext(), MyRemindersActivity.class);
        startActivity(intent);

    }

    /** On Click handler for Login button **/
    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, 2);
    }

    public void logOut(View view) {
        loginButton.setClickable(true);
        loginButton.setVisibility(View.VISIBLE);;
        welcomeMessage.setVisibility(View.INVISIBLE);

        FileIO fileIO = new FileIO();
        try {
            CurrentUserSingleton.getInstance().setId(getDeviceID(getApplicationContext()));
            UserInternalStorage user = new UserInternalStorage("DeviceId", CurrentUserSingleton.getInstance().getId());
            fileIO.writeFileToInternalStorage(getApplicationContext(), user, "user");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        CurrentUserSingleton.getInstance().setName("DeviceId");
        logOut.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(CurrentUserSingleton.getInstance().getName() != null && !CurrentUserSingleton.getInstance().getName().equals("DeviceId")) {
                loginButton.setClickable(false);
                loginButton.setVisibility(View.INVISIBLE);
                welcomeMessage.setText("Welcome " + CurrentUserSingleton.getInstance().getName());
                welcomeMessage.setVisibility(View.VISIBLE);
                logOut.setVisibility(View.VISIBLE);
            }
        }
    }
}