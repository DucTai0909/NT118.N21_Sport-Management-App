package com.example.dangki.KhachHang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Calendar.DailyCalendarActivity;
import com.example.dangki.Dangnhapthanhcong;
import com.example.dangki.R;

public class DatLichThanhCong extends AppCompatActivity {
    Button btn_tieptuc, btn_sankhac;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.success_screen_themlichdatsan);

        FindViewByIds();

        btn_tieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Dangnhapthanhcong.class));
                finish();
            }
        });

        btn_sankhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChonSan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void FindViewByIds() {
        btn_tieptuc = findViewById(R.id.btn_khachang_datsanthanhcong_tieptuc);
        btn_sankhac = findViewById(R.id.btn_khachang_datsanthanhcong_sankhac);
    }
}
