package com.example.doggoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doggoo.databinding.ActivityUserDataBinding;

public class userData extends AppCompatActivity {
    ActivityUserDataBinding binding;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDataBinding.inflate(getLayoutInflater());  // Initialize binding
        setContentView(binding.getRoot());  // Use binding.getRoot() after initialization

        database = new Database(this);

        binding.dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.pName.getText().toString();
                String address = binding.pAddress.getText().toString();
                String contact = binding.pContact.getText().toString();

                UserSession userSession = UserSession.getInstance(userData.this);
                String email = userSession.getEmail();

                if (name.equals("") || address.equals("") || contact.equals("")) {
                    Toast.makeText(userData.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean insert = database.insertUserdata(email, name, address, contact);

                    if (insert) {
                        Toast.makeText(userData.this, "Data inserted successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(userData.this, "Error inserting data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
