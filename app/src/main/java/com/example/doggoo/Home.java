package com.example.doggoo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.doggoo.databinding.ActivityHomeBinding;
import com.example.doggoo.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {
    ActivityHomeBinding binding;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cart = new Cart();
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int itemId = item.getItemId();
            if (itemId == R.id.m_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.cart) {
                fragment = new CartFragment();
            } else if (itemId == R.id.profile) {
                fragment = new settingsFragment();
            } else if(itemId == R.id.Edu){
                fragment = new educationalFragment();
            }else{
                fragment = new HomeFragment();
            }
            replaceFragment(fragment);
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public Cart getCart() {
        return cart;
    }
}
