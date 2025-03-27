package com.example.doggoo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText editName, editAddress, editContact;
    private Button saveButton;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize the views
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editContact = findViewById(R.id.editContact);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve the user's email passed from the settingsFragment
        userEmail = getIntent().getStringExtra("email");

        // Load the user's current details
        loadUserDetails();

        // Handle the save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDetails();
            }
        });
    }

    private void loadUserDetails() {
        Database db = new Database(this);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM userData WHERE email = ?", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") String contact = cursor.getString(cursor.getColumnIndex("contact"));

            // Set the current details in the EditTexts
            editName.setText(name);
            editAddress.setText(address);
            editContact.setText(contact);

            Log.d(TAG, "Loaded user details: Name = " + name + ", Address = " + address + ", Contact = " + contact);
        } else {
            Log.e(TAG, "No user data found for email: " + userEmail);
            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void updateUserDetails() {
        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String contact = editContact.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || contact.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Database db = new Database(this);
        SQLiteDatabase writableDb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("contact", contact);

        int rowsUpdated = writableDb.update("userData", values, "email = ?", new String[]{userEmail});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Profile updated successfully.");
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Failed to update profile.");
        }
    }
}
