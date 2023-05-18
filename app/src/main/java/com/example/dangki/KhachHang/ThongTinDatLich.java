package com.example.dangki.KhachHang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Login;
import com.example.dangki.R;

public class ThongTinDatLich extends AppCompatActivity {
    Button btnXacNhanDatLich;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.thongtindatlich);

        btnXacNhanDatLich = findViewById(R.id.btnXacNhanDatLich);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        btnXacNhanDatLich.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Đang xử lý...", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Kết thúc đếm thời gian 2 giây
                    // Chuyển sang màn hình khác ở đây
                    Toast.makeText(getApplicationContext(), "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DatSan.class));
                    finish();
                }
            }, 2000);
        });
    }
}
