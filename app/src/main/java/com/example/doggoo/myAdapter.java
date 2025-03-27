package com.example.doggoo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    public List<model> menuItemList;
    private Context context;
    private final RecyclerViewInterface recyclerViewInterface;

    public myAdapter(List<model> menuItemList, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.menuItemList = menuItemList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new MyViewHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        model menuItem = menuItemList.get(position);

        // Validate and set the image
        byte[] imageData = menuItem.getImage();
        if (imageData != null && imageData.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            holder.dogFoodImage.setImageBitmap(bitmap);
        } else {
            // Set a placeholder image or handle the missing image scenario
            holder.dogFoodImage.setImageResource(R.drawable.baseline_broken_image_24);
        }

        // Validate and set the name
        String name = menuItem.getName();
        if (name != null && !name.isEmpty()) {
            holder.dogFoodName.setText(name);
        } else {
            holder.dogFoodName.setText("Unknown Product");
        }

        // Validate and set the price
        String price = menuItem.getPrice();
        if (price != null && !price.isEmpty()) {
            holder.dogFoodPrice.setText("Rs " + price);
        } else {
            holder.dogFoodPrice.setText("Price not available");
        }
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public void updateItems(List<model> newItems) {
        this.menuItemList = newItems;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView dogFoodImage;
        public TextView dogFoodName;
        public TextView dogFoodDescription;
        public TextView dogFoodPrice;
        public Button btnMoreDetails;
        public ImageButton btnCart;

        public MyViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            dogFoodImage = itemView.findViewById(R.id.dog_food_image);
            dogFoodName = itemView.findViewById(R.id.dog_food_name);
            dogFoodDescription = itemView.findViewById(R.id.dog_food_description);
            dogFoodPrice = itemView.findViewById(R.id.dog_food_price);
            btnMoreDetails = itemView.findViewById(R.id.btn_more_details);
            btnCart = itemView.findViewById(R.id.btn_add_to_cart);

            // OnClickListener for detail button
            btnMoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            model selectedItem = menuItemList.get(pos);

                            // Validate the selected item before starting the activity
                            if (selectedItem != null && selectedItem.getName() != null && !selectedItem.getName().isEmpty()) {
                                Intent intent = new Intent(context, viewDetail.class);
                                intent.putExtra("menuItem", selectedItem);
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "Invalid product data", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                }
            });

            // OnClickListener for cart button
            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onAddToCartClick(pos);
                        }
                    }
                }
            });
        }
    }
}

