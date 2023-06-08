package com.example.dangki;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Admin.KhachHang.Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dangnhapthanhcong  extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhapthanhcong);

        bottomNavigationView = findViewById(R.id.admin_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_datlich);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_admin_datlich:
                    return true;
                case R.id.bottom_admin_khachhang:
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                    return true;
            }
            return false;
        });
    }
}
