package com.example.dangki.KhachHang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Login;
import com.example.dangki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ThongTinUser extends AppCompatActivity {
    Button btn_logout;
    BottomNavigationView bottomNavigationView;
    String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.khachhang_userinfo);

        FindViewByIds();
        BottomNavigation();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinUser.this);

                builder.setTitle("Xác nhận");
                builder.setMessage("Xác nhận đăng xuất?");
                builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void FindViewByIds() {
        btn_logout = findViewById(R.id.btn_khachhang_userinfo_logout);
        bottomNavigationView = findViewById(R.id.bottom_khachhang_info);

        userID = getIntent().getStringExtra("userID");
    }
    private void BottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_khachhang_menu_info);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_khachhang_menu_info:
                    return true;
                case R.id.bottom_khachhang_home:
                    Intent intent= new Intent(getApplicationContext(), ChonSan.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_khachhang_history:
                    Intent intent1= new Intent(getApplicationContext(), ThongTinDatLich.class);
                    intent1.putExtra("userID", userID);
                    startActivity(intent1);
                    finish();
                    return true;
            }
            return false;
        });
    }
}
