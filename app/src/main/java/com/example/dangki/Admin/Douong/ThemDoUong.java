package com.example.dangki.Admin.Douong;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dangki.R;

public class ThemDoUong extends AppCompatActivity {
    ImageView btn_goback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.admin_douong_add);
        findViewByIds();

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViewByIds() {
        btn_goback = findViewById(R.id.btn_admin_douong_goback);
    }
}
