package com.example.doggoo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doggoo.databinding.ActivityAddProductsBinding;

import java.io.IOException;

public class add_products extends AppCompatActivity {
    public ActivityAddProductsBinding binding;
    private Bitmap imageToStore;
    private Database database;
    private ImageView imageUpload;
    private Uri imageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = new Database(this);

        binding.btnSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etName.getText().toString().isEmpty() ||
                        binding.etDescription.getText().toString().isEmpty() ||
                        binding.etPrice.getText().toString().isEmpty()) {
                    Toast.makeText(add_products.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else if (imageToStore == null) {
                    Toast.makeText(add_products.this, "Please select an image", Toast.LENGTH_SHORT).show();
                } else {
                    boolean valueInserted = database.insertDetails(
                            imageToStore,

                            binding.etName.getText().toString(),
                            binding.etDescription.getText().toString(),
                            binding.etPrice.getText().toString(),
                            binding.etType.getText().toString()

                    );

                    if (valueInserted) {
                        Toast.makeText(add_products.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(add_products.this, "Error inserting data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
            }
        });
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageFilePath = result.getData().getData();
                    try {
                        imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                        binding.ivImage.setImageBitmap(imageToStore);
                    } catch (IOException e) {
                        Toast.makeText(add_products.this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
