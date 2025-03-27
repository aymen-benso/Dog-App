package com.example.doggoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private myAdapter adapter;
    private Database database;
    private Cart cart;
    private Button filterPuppies, filterAdult, filterSenior;
    private TextView filterLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Home activity = (Home) getActivity();
        cart = activity.getCart();  // Get the shared cart instance

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        filterPuppies = view.findViewById(R.id.filter_puppies);
        filterAdult = view.findViewById(R.id.filter_adult);
        filterSenior = view.findViewById(R.id.filter_senior);
        filterLabel = view.findViewById(R.id.filter_label);  // Reference to the TextView

        database = new Database(getContext());
        List<model> menuItemList = database.getAllMenuItems();

        adapter = new myAdapter(menuItemList, getContext(), this);
        recyclerView.setAdapter(adapter);

        filterPuppies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems("puppies");
                filterLabel.setText("Viewing: Puppies");  // Update the label
            }
        });

        filterAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems("adult");
                filterLabel.setText("Viewing: Adult Dogs");  // Update the label
            }
        });

        filterSenior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterItems("senior");
                filterLabel.setText("Viewing: Senior Dogs");  // Update the label
            }
        });

        return view;
    }

    private void filterItems(String type) {
        List<model> filteredItems = database.getItemsByType(type);
        adapter.updateItems(filteredItems);
    }

    @Override
    public void onItemClick(int position) {
        //Context is the environment or the current state of the application,
        // which is necessary to start new activities.
        Intent intent = new Intent(getContext(), viewDetail.class);
        model Item = adapter.menuItemList.get(position);
        intent.putExtra("menuItem", Item);
        startActivity(intent);
    }

    @Override
    public void onAddToCartClick(int pos) {
        model menuItem = adapter.menuItemList.get(pos);
        cart.addItem(menuItem);
        Toast.makeText(getContext(), "Added to cart successfully", Toast.LENGTH_SHORT).show();

        // Notify CartFragment of the change
        Home activity = (Home) getActivity();
        CartFragment cartFragment = (CartFragment) activity.getSupportFragmentManager().findFragmentById(R.id.cart);
        if (cartFragment != null && cartFragment.isVisible()) {
            cartFragment.updateCart();
        }
    }
}
