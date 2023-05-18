package com.example.dangki.KhachHang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.R;

public class ChonDoUong extends AppCompatActivity {
    FrameLayout f2;
    Button btnDatlich;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.chondouong);

        f2 = findViewById(R.id.frame2);
        btnDatlich = findViewById(R.id.btnDatLich);
        f2.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChiTietDoUong.class));
            finish();
        });
        btnDatlich.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ThongTinDatLich.class));
            finish();
        });
    }
}
