package com.example.dangki.KhachHang;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.R;

public class ChiTietSan extends AppCompatActivity {
    Button btnDatSan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.chitietsan);

        btnDatSan = findViewById(R.id.btnDatSan);
        btnDatSan.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChonDoUong.class));
            finish();
        });
    }
}
