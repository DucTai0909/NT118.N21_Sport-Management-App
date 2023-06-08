package com.example.dangki.Admin.KhachHang;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Dangnhapthanhcong;
import com.example.dangki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Menu extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_khachhang);

        bottomNavigationView = findViewById(R.id.admin_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_khachhang);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_admin_khachhang:
                    return true;
                case R.id.bottom_admin_datlich:
                    startActivity(new Intent(getApplicationContext(), Dangnhapthanhcong.class));
                    return true;
            }
            return false;
        });
    }
}
