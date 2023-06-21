package com.example.dangki.KhachHang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dangki.Dangnhapthanhcong;
import com.example.dangki.Model.CreateOrder;
import com.example.dangki.Model.PurchasedService;
import com.example.dangki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ThanhToan extends AppCompatActivity {

    ImageView btn_goback;
    TextView tv_tongTien;
    String userID, rentalID;
    RecyclerView recyclerView;
    PurchasedServiceAdapter purchasedServiceAdapter;
    List<PurchasedService> purchasedServices;
    Button btn_pay;
    RadioGroup radioGroup_phuongthuc;
    RadioButton radioButtonCash, radioButtonZalo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.khachhang_thanhtoan);
        purchasedServices = new ArrayList<>();
        FindViewByIds();
        SetupAdapter();

        LoadDanhSach();

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioGroup_phuongthuc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == -1) {
                    // Không có radio nào được chọn
                    btn_pay.setEnabled(false);
                    btn_pay.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),
                            R.color.gray));
                } else {
                    // Có radio được chọn
                    btn_pay.setEnabled(true);
                    btn_pay.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),
                            R.color.green));                }
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThanhToan.this);
                builder.setTitle("Xác nhận thanh toán");
                builder.setMessage("Quý khách vui lòng xác nhận thanh toán?");

                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(radioButtonZalo.isChecked()){
                            ThanhToanZalo();
                        } else if (radioButtonCash.isChecked()) {
                            startActivity(new Intent(getApplicationContext(), Dangnhapthanhcong.class));
                        }
                    }
                });
                builder.setNegativeButton("Húy", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void ThanhToanZalo() {
        CreateOrder orderApi = new CreateOrder();

        try {
            String tongTien = tv_tongTien.getText().toString(); // Chuỗi ban đầu: "650000.0 VND"

// Bỏ chữ "VND" trong chuỗi
            String chiTietTien = tongTien.replace(".0VND", "");

            JSONObject data = orderApi.createOrder(chiTietTien);
            Log.d("Amount", chiTietTien);
            String code = data.getString("returncode");
//            String code = data.getString("returncode");

            String token = data.getString("zptranstoken");

            if (code.equals("1")) {
                ZaloPaySDK.getInstance().payOrder(ThanhToan.this, token, "demozpdk://app",
                        new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                UpdateStatus();
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {

                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                            }
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateStatus() {
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", "Done");

        db.collection("Rental")
                .document(rentalID)
                .update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ThanhToan.this, "Thanh toán thành công",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ThanhToan.this, ChonSan.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void LoadDanhSach() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Lấy thông tin tổng tiền từ collection "Rental"
            db.collection("Rental").document(rentalID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        tv_tongTien.setText(documentSnapshot.getDouble("total").toString() + "VND");
                    });



            // Truy vấn danh sách đồ uống đã mua từ collection "Drink_Rental"
            db.collection("Drink_Rental")
                    .whereEqualTo("rental_id", rentalID)
                    .get()
                    .addOnSuccessListener(drinkRentalQueryDocumentSnapshots -> {

                        for (QueryDocumentSnapshot drinkRentalSnapshot : drinkRentalQueryDocumentSnapshots) {
                            String drinkID = drinkRentalSnapshot.getString("drink_id");
                            int quantity = drinkRentalSnapshot.getLong("quantity").intValue();

                            // Truy vấn thông tin về đồ uống từ collection "Drink"
                            db.collection("Drink")
                                    .document(drinkID)
                                    .get()
                                    .addOnSuccessListener(drinkSnapshot -> {
//                                        purchasedServices = new ArrayList<>()
                                        String drinkName = drinkSnapshot.getString("name");
                                        String img_url = drinkSnapshot.getString("img_url");
                                        double drinkPrice = drinkSnapshot.getDouble("price");

                                        PurchasedService purchasedService =
                                                new PurchasedService(drinkID, drinkName, "Drink",
                                                img_url, drinkPrice, quantity);
                                        purchasedServices.add(purchasedService);
                                        purchasedServiceAdapter.setPurchasedServiceList(purchasedServices);

                                    })
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                        } else {
                                        }
                                    });
                        }
                        if (drinkRentalQueryDocumentSnapshots.isEmpty()) {
                        }
                    });



            // Truy vấn danh sách sân đã thuê từ collection "Stadium_Rental"
            db.collection("Stadium_Rental")
                    .whereEqualTo("rental_id", rentalID)
                    .get()
                    .addOnSuccessListener(stadiumRentalQueryDocumentSnapshots -> {

                        for (QueryDocumentSnapshot stadiumRentalSnapshot : stadiumRentalQueryDocumentSnapshots) {
                            String stadiumID = stadiumRentalSnapshot.getString("stadium_id");
                            int rental_time = stadiumRentalSnapshot.getLong("rental_time").intValue();

                            // Truy vấn thông tin về sân từ collection "Stadium"
                            db.collection("Stadium")
                                    .document(stadiumID)
                                    .get()
                                    .addOnSuccessListener(stadiumSnapshot -> {
                                        String stadiumName = stadiumSnapshot.getString("name");
                                        String img_url = stadiumSnapshot.getString("img_url");
                                        double stadiumPrice = stadiumSnapshot.getDouble("price");

                                        PurchasedService purchasedService =
                                                new PurchasedService(stadiumID, stadiumName,
                                                "Stadium", img_url, stadiumPrice, rental_time);
                                        purchasedServices.add(purchasedService);
                                        purchasedServiceAdapter.setPurchasedServiceList(purchasedServices);
                                    })
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                        } else {
                                        }
                                    });
                        }

                        if (stadiumRentalQueryDocumentSnapshots.isEmpty()) {
                        }
                    });



            // Chờ hoàn thành của cả ba hàm bất đồng bộ

    }
    void ShowPurchasedServices(List<PurchasedService> purchasedServices) {
        purchasedServiceAdapter = new PurchasedServiceAdapter(purchasedServices);
        recyclerView.setAdapter(purchasedServiceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        purchasedServiceAdapter.setPurchasedServiceList(purchasedServices);
    }


    private void SetupAdapter() {
        purchasedServiceAdapter = new PurchasedServiceAdapter(purchasedServices);
        recyclerView.setAdapter(purchasedServiceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void FindViewByIds() {
        btn_goback = findViewById(R.id.btn_khachhang_thanhtoan_goback);
        tv_tongTien = findViewById(R.id.tv_khachhang_thanhtoan_tongtien2);
        recyclerView = findViewById(R.id.rcv_khachhang_thanhtoan);

        userID = getIntent().getStringExtra("userID");
        rentalID = getIntent().getStringExtra("rentalID");

        btn_pay = findViewById(R.id.btn_khachhang_thanhtoan);

        radioGroup_phuongthuc = findViewById(R.id.radioGroup_paymentMethod);
        radioButtonCash = findViewById(R.id.radioButton_cash);
        radioButtonZalo = findViewById(R.id.radioButton_zalo);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(554, Environment.SANDBOX);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
