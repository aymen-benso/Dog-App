package com.example.doggoo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class settingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Retrieve the email from UserSession singleton
        String userEmail = UserSession.getInstance(getActivity()).getEmail();

        // Log the retrieved email for debugging
        Log.d(TAG, "Retrieved email: " + userEmail);

        // Query the database for user data
        Database db = new Database(getActivity());
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM userData WHERE email = ?", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
            @SuppressLint("Range") String contact = cursor.getString(cursor.getColumnIndex("contact"));

            // Update the UI with the retrieved data
            TextView nameTextView = view.findViewById(R.id.nameTextView);
            TextView addressTextView = view.findViewById(R.id.addressTextView);
            TextView contactTextView = view.findViewById(R.id.contactTextView);

            nameTextView.setText(name);
            addressTextView.setText(address);
            contactTextView.setText(contact);

            // Log the retrieved data for debugging
            Log.d(TAG, "Retrieved user data: Name = " + name + ", Address = " + address + ", Contact = " + contact);
        } else {
            // Log a message if no data is found
            Log.e(TAG, "No user data found for email: " + userEmail);
        }

        cursor.close();
        // Handle the logout button click
        ImageButton logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the user session using SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Clear all saved data
                editor.apply();

                // Optionally, clear the UserSession singleton
                UserSession.getInstance(getActivity()).clearSession();

                // Redirect to the home page
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish(); // Close the current activity
            }
        });

        Button editBtn =  view.findViewById(R.id.editButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start EditProfileActivity to allow user to edit their profile
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("email", userEmail); // Pass the user's email to EditProfileActivity
                startActivity(intent);
            }
        });


        return view;

    }
}
