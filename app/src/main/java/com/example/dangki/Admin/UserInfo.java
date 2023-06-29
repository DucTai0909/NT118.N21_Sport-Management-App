package com.example.dangki.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.Admin.KhachHang.Menu;
import com.example.dangki.KhachHang.ThongTinDatLich;
import com.example.dangki.KhachHang.ThongTinUser;
import com.example.dangki.Login;
import com.example.dangki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class UserInfo extends AppCompatActivity {

    Button btn_logout;
    String userID;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_userinfo_admin);

        FindViewByIds();
        BottomNavigation();
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });
    }

    private void BottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_admin_profile:
                    return true;
                case R.id.bottom_admin_khachhang:
                    Intent intent= new Intent(getApplicationContext(), Menu.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_admin_san:
                    Intent intent1= new Intent(getApplicationContext(), com.example.dangki.Admin.San.Menu.class);
                    intent1.putExtra("userID", userID);
                    startActivity(intent1);
                    finish();
                    return true;
                case R.id.bottom_admin_douong:
                    Intent intent2= new Intent(getApplicationContext(), com.example.dangki.Admin.Douong.Menu.class);
                    intent2.putExtra("userID", userID);
                    startActivity(intent2);
                    finish();
                    return true;
            }
            return false;
        });
    }

    private void LogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Xác nhận đăng xuất");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void FindViewByIds() {
        btn_logout = findViewById(R.id.btn_admin_userinfo_logOut);
        bottomNavigationView = findViewById(R.id.bottom_admin_info);
        userID = getIntent().getStringExtra("userID");
    }
}
