package com.example.doggoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doggoo.databinding.ActivitySignupBinding;

public class signupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new Database(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPass.getText().toString();
                String confirmpassword = binding.signupConfirmpass.getText().toString();

                if (email.equals("") || password.equals("") || confirmpassword.equals(""))
                    Toast.makeText(signupActivity.this, "All fields are mandatory" , Toast.LENGTH_SHORT).show();
                else {
                    if(password.equals(confirmpassword)){
                        Boolean checkuserEmail = database.checkEmail(email);

                        if(checkuserEmail == false){
                            Boolean insert =  database.insertData(email, password);
                            if(insert == true){
                                // Example in SignupActivity.java
                                UserSession userSession = UserSession.getInstance(signupActivity.this);
                                userSession.setEmail(email); // Save email in session after successful signup

                                Toast.makeText(signupActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                                //after sign-up user should add data
                                Intent intent = new Intent(getApplicationContext(), userData.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(signupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Toast.makeText(signupActivity.this, "User Already Exist please login", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(signupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}