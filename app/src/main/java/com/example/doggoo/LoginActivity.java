package com.example.doggoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doggoo.databinding.ActivityLoginBinding;
import com.example.doggoo.databinding.ActivitySignupBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Database database;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new Database(this);
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString()     ;
                String password = binding.loginpassword.getText().toString();
                if(email.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean checkCredentials = database.checkEmailPassword(email, password);

                    if(checkCredentials == true){
                        //set up the session for the relevant user
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.SignupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });

    }
}