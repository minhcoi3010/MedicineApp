package com.example.btlandroidapi.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.btlandroidapi.singleton.CustomerSingleton;
import com.example.btlandroidapi.R;
import com.example.btlandroidapi.fragment.AboutMeFragment;
import com.example.btlandroidapi.fragment.CartFragment;
import com.example.btlandroidapi.fragment.HomeFragment;
import com.example.btlandroidapi.fragment.ProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CustomerSingleton customerSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customerSingleton = CustomerSingleton.getInstance();

        InitWidget();
        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    replaceFragment(new HomeFragment());
                    return true;
                }
                if(item.getItemId() == R.id.product){
                    replaceFragment(new ProductFragment());
                    return true;
                }
                if(item.getItemId() == R.id.cart){
                    replaceFragment(new CartFragment());
                    return true;
                }
                if(item.getItemId() == R.id.aboutme){
                    replaceFragment(new AboutMeFragment());
                    return true;
                }
                return false;
            }
        });

    }

    private void InitWidget() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.commit();
    }
}