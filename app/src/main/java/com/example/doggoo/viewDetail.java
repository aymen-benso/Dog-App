package com.example.doggoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class viewDetail extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        // Set up the toolbar and enable back navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the views
        titleTextView = findViewById(R.id.title_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        imageView = findViewById(R.id.image_view);

        // Retrieve the passed model object from the intent
        Intent intent = getIntent();
        model menuItem = intent.getParcelableExtra("menuItem");

        // Populate the views with the data from the model object
        if (menuItem != null) {
            titleTextView.setText(menuItem.getName() != null ? menuItem.getName() : "Unknown Product");
            descriptionTextView.setText(menuItem.getDescription() != null ? menuItem.getDescription() : "No description available");
            priceTextView.setText("Price: Rs " + (menuItem.getPrice() != null ? menuItem.getPrice() : "N/A"));

            // Validate and set the image
            byte[] imageBytes = menuItem.getImage();
            if (imageBytes != null && imageBytes.length > 0) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(imageBitmap);
            } else {
                // Set a placeholder image or handle the missing image scenario
                imageView.setImageResource(R.drawable.baseline_broken_image_24);
            }
        } else {
            // Handle the case where the menuItem is null
            titleTextView.setText("Product not found");
            descriptionTextView.setText("");
            priceTextView.setText("");
            imageView.setImageResource(R.drawable.baseline_broken_image_24);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity and return to the previous one
        return true;
    }
}
