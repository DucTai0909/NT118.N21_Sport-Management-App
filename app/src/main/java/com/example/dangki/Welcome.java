package com.example.dangki;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.KhachHang.ChonSan;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Welcome extends AppCompatActivity {
    TextView currentTime;
    SpinKitView loadingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        // Khởi tạo các thành phần của Layout
        FindViewByIds();

        /*
        Xử lý ngày giờ để đưa lên màn hình
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String currentTimeString = dateFormat.format(new Date());
        currentTime.setText(currentTimeString);

        /*
        Delay 2 giây trước khi vào màn hình login
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisibility(View.INVISIBLE);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                if(currentUser != null){
                    String userID = currentUser.getUid();

                    Intent intent = new Intent(getApplicationContext(), ChonSan.class);
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }
        },2250);
    }
    void FindViewByIds(){
        currentTime = findViewById(R.id.textViewCurrentTime);
        loadingBar = findViewById(R.id.progressBar);
        loadingBar.setVisibility(View.VISIBLE);

    }
}
