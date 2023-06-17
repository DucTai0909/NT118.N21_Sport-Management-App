//package com.example.dangki.KhachHang;
//
//import static android.content.ContentValues.TAG;
//
//import static com.example.dangki.Calendar.CalendarUtils.selectedDate;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ThemLichDatSan extends AppCompatActivity {
//    String rentalID ="", sanID ="";
//    LocalTime selected_StartTime_final, selected_EndTime_final;
//
//    double totalDb=0.0, stadium_price = 0.0;
//    int gioChoi =0;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FindViewByID();
//        if(CheckFirstBooking() == true){
//            AddFirstBooking();
//        }
//
//    }
//    void FindViewByID(){
//        Intent intent = getIntent();
//        sanID = intent.getStringExtra("idSan_intent");
//        stadium_price = intent.getDoubleExtra("gia_san", 0.0);
//        gioChoi = intent.getIntExtra("gio_choi", 0);
//
//
//    }
//    boolean CheckFirstBooking(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String userID = "sAvjTnumW8fEcSiahKiGsH1g2102";
//
//        db.collection("Rental")
//                .whereEqualTo("user_id", userID)
//                .whereEqualTo("status", "Booking")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            if (!querySnapshot.isEmpty()) {
//                                DocumentSnapshot documentSnapshot = (DocumentSnapshot) querySnapshot.getDocuments();
//                                // Lấy dữ liệu từ document
//                                rentalID = documentSnapshot.getId();
//                                totalDb = documentSnapshot.getDouble("total");
//                                // Các trường dữ liệu khác...
//                                // Tiếp tục xử lý dữ liệu
//                            } else {
//                                // Không tìm thấy document thỏa mãn điều kiện
//                            }
//                        } else {
//                            // Xử lý lỗi
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//        if(!rentalID.isEmpty()){
//            return false;
//        }
//        return true;
//    }
//    private void AddFirstBooking() {
//        String userID = "sAvjTnumW8fEcSiahKiGsH1g2102";
//        double total_add = totalDb + (stadium_price / 60) * gioChoi;
//
//        Map<String, Object> rentalData = new HashMap<>();
//        rentalData.put("date", Timestamp.now());
//        rentalData.put("total", total_add);
//        rentalData.put("user_id", userID);
//        rentalData.put("status", "Booking");
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("Rental").add(rentalData)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
//                            DocumentReference document = task.getResult();
//                            if (document != null) {
//                                rentalID = document.getId();
////                                Toast.makeText(getApplicationContext(), "Hello11", Toast.LENGTH_SHORT).show();
//                                FirstAddToDb();
//                            }
//                        } else {
//                            Exception e = task.getException();
//                            // Xử lý lỗi khi thêm dữ liệu không thành công
//                        }
//                    }
//                });
//
//    }
//    void FirstAddToDb(){
//        Date start_time_add_to_db = new Date();
//        Date end_time_add_to_db = new Date();
//        start_time_add_to_db = ConvertLocalDateTimeToDate(selectedDate, selected_StartTime_final);
//        end_time_add_to_db = ConvertLocalDateTimeToDate(selectedDate, selected_EndTime_final);
//
//        Map<String, Object> stadiumRentalData = new HashMap<>();
//        stadiumRentalData.put("rental_id", rentalID);
//        stadiumRentalData.put("stadium_id", sanID);
//        stadiumRentalData.put("start_time", start_time_add_to_db);
//        stadiumRentalData.put("end_time", end_time_add_to_db);
//        stadiumRentalData.put("rental_time", gioChoi);
//
//        FirebaseFirestore db =FirebaseFirestore.getInstance();
//        db.collection("Stadium_Rental").add(stadiumRentalData)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if (task.isSuccessful()) {
////                            finish();
////                            progressBar.setVisibility(View.GONE);
////                            Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    Date ConvertLocalDateTimeToDate(LocalDate localDate, LocalTime localTime){
//
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//
//        ZoneId zoneId = ZoneId.systemDefault(); // Use a specific time zone, or specify a desired time zone
//
//        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
//
//        Instant instant = zonedDateTime.toInstant();
//
//        Date date = Date.from(instant);
//        return date;
//    }
//    LocalTime ConvertDateToLocalTime(Date date){
//        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
//        return localTime;
//    }
//
//}
