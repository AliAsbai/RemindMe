package com.example.remindme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.remindme.R;
import com.example.remindme.UserRequest;
import com.example.remindme.model.User;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText nameInput;
    private EditText passWordInput;
    private EditText repeatPasswordInput;

    private Button registerButton;

    private User registeredUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailInput = findViewById(R.id.register_email);
        nameInput = findViewById(R.id.register_name);
        passWordInput = findViewById(R.id.register_password);
        repeatPasswordInput = findViewById(R.id.register_repeatPassword);

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(registerUser);

        registeredUser = new User("","","");
        registeredUser.setId("");
    }

    View.OnClickListener registerUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(createUser()){
                Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean createUser(){
        String email = String.valueOf(emailInput.getText());
        if(email.isEmpty()){
            emailInput.clearComposingText();
            emailInput.setError("Please enter a valid email address");
            return false;
        }
        String name = String.valueOf(nameInput.getText());
        if(name.isEmpty()){
            nameInput.setError("Please enter a name");
            return false;
        }
        String password = String.valueOf(passWordInput.getText());
        if(password.isEmpty()){
            passWordInput.setError("Please enter a password");
            return false;
        }
        String repeatPassword = String.valueOf(repeatPasswordInput.getText());
        if(repeatPassword.isEmpty()){
            repeatPasswordInput.setError("Please confirm your password");
            return false;
        }
        if(!repeatPassword.equals(password)){
            repeatPasswordInput.setError("Password did not match");
            return false;
        }
        User user = new User(name, email, password);
        UserRequest userRequest = new UserRequest(getApplicationContext());
        return userRequest.registerUser(user);
    }
}