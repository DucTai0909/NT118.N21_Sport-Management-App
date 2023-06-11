package com.example.dangki.Admin.San;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dangki.Model.DoUong;
import com.example.dangki.Model.San;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Menu extends AppCompatActivity {
    FloatingActionButton btn_themSan;
    BottomNavigationView bottomNavigationView;
    SearchView searchView;
    static final long DOUBLE_BACK_PRESS_DURATION = 2000; // Thời gian giới hạn giữa 2 lần nhấn "Back" (2 giây trong trường hợp này)
    long backPressedTime; // Thời gian người dùng nhấn nút "Back" lần cuối
    RecyclerView recyclerView;
    List<San> sanList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
