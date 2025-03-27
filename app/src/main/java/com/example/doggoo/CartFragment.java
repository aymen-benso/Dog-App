package com.example.doggoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Cart cart;
    private TextView totalAmountTextView;
    private Button payButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Home activity = (Home) getActivity();
        cart = activity.getCart();  // Get the shared cart instance

        recyclerView = view.findViewById(R.id.recycler_view_cart);
        totalAmountTextView = view.findViewById(R.id.total_amount);
        payButton = view.findViewById(R.id.pay_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cart.getItems(), getContext(), totalAmountTextView);
        recyclerView.setAdapter(cartAdapter);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.getItems().isEmpty()) {
                    Toast.makeText(activity, "Cart is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), payActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    public void updateCart() {
        if (cartAdapter != null) {
            cartAdapter.updateCartItems(cart.getItems());
        }
    }

}
