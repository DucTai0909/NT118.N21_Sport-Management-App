package com.example.dangki.KhachHang;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dangki.Model.PurchasedService;
import com.example.dangki.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ThanhToan extends AppCompatActivity {

    ImageView btn_goback;
    TextView tv_tongTien;
    String userID, rentalID;
    RecyclerView recyclerView;
    PurchasedServiceAdapter purchasedServiceAdapter;
    List<PurchasedService> purchasedServices;

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
    }
}
