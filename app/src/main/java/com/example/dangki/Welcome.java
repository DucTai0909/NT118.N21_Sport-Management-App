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

import com.github.ybq.android.spinkit.SpinKitView;

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
        currentTime = findViewById(R.id.textViewCurrentTime);
        loadingBar = findViewById(R.id.progressBar);
        loadingBar.setVisibility(View.VISIBLE);

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
                startActivity(new Intent(Welcome.this, Login.class));
                finish();
            }
        },2250);
    }
}
