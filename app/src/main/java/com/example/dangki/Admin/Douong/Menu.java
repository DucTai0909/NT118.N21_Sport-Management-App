package com.example.dangki.Admin.Douong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dangki.Dangnhapthanhcong;
import com.example.dangki.Model.DoUong;
import com.example.dangki.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    static final long DOUBLE_BACK_PRESS_DURATION = 2000; // Thời gian giới hạn giữa 2 lần nhấn "Back" (2 giây trong trường hợp này)
    long backPressedTime; // Thời gian người dùng nhấn nút "Back" lần cuối
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    DoUongMenuAdapter doUongMenuAdapter;
    FloatingActionButton btn_admin_douong_them;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_douong);

        findViewByIds();
        BottomNavigation();

        doUongMenuAdapter = new DoUongMenuAdapter(new ArrayList<>());
        recyclerView.setAdapter(doUongMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadDoUong();

        btn_admin_douong_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ThemDoUong.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + DOUBLE_BACK_PRESS_DURATION > System.currentTimeMillis()){
            super.onBackPressed();
        }else{
            Toast.makeText(this, "Nhấn back một lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis(); // Cập nhật thời gian người dùng nhấn nút "Back" lần cuối
    }

    void loadDoUong(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Drink");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DoUong> doUongList = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String id = documentSnapshot.getId();
                    String tenDoUong = documentSnapshot.getString("name");
                    String img_url = documentSnapshot.getString("img_url");
                    Boolean isDelete = documentSnapshot.getBoolean("isDelete");
                    int soLuong = Math.toIntExact(documentSnapshot.getLong("remain"));
                    double gia = documentSnapshot.getDouble("price");

                    if(isDelete == false){
                        DoUong doUong = new DoUong(id, tenDoUong, img_url, gia, soLuong);
                        doUongList.add(doUong);
                    }
                    doUongMenuAdapter.setDoUongList(doUongList);
                }
            };
        });
    }
    void findViewByIds(){
        bottomNavigationView = findViewById(R.id.admin_navigation);
        btn_admin_douong_them = findViewById(R.id.btn_themDoUong);
        recyclerView = findViewById(R.id.rcv_admin_douong);
    }
    void BottomNavigation(){

        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_douong);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_admin_douong:
                    return true;
                case R.id.bottom_admin_datlich:
                    startActivity(new Intent(getApplicationContext(), Dangnhapthanhcong.class));
                    finish();
                    return true;
                case R.id.bottom_admin_khachhang:
                    startActivity(new Intent(getApplicationContext(), com.example.dangki.Admin.KhachHang.Menu.class));
                    finish();
                    return true;
            }
            return false;
        });
    }

}
