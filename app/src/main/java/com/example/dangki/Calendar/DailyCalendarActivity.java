package com.example.dangki.Calendar;

import static android.content.ContentValues.TAG;

import static com.example.dangki.Calendar.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dangki.KhachHang.ChiTietSan;
import com.example.dangki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyCalendarActivity extends AppCompatActivity
{

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    LocalTime time;
    List<LocalTime> start_time_list;
    List<LocalTime> end_time_list;
    String sanID;
    Button btn_datlich;
    LocalTime selected_StartTime;
    Date selected_EndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_daily_calendar);
        initWidgets();
        GetData();


//        GetData();
//        setDayView();
//        time = LocalTime.now();
//        String eventName = eventNameET.getText().toString();
//        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
//        Event.eventsList.add(newEvent);

    }

    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
        btn_datlich = findViewById(R.id.btn_khachhang_datlichngay);
        Intent intent = getIntent();
        sanID = intent.getStringExtra("idSan_intent");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GetData();
//        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();

        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }

        return list;
    }
    private void GetData()
    {
        Event.eventsList = new ArrayList<>();
        start_time_list = new ArrayList<>();
        end_time_list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Stadium_Rental")
                .whereEqualTo("stadium_id", sanID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Lấy dữ liệu từ mỗi document trong collection
                                String id = document.getId();
                                Date start_time = document.getDate("start_time");
                                Date end_time = document.getDate("end_time");

                                LocalDate localDate = start_time.toInstant().atZone(ZoneId.systemDefault())
                                        .toLocalDate();

                                if(localDate.equals(selectedDate)){
                                    LocalTime localTimeStart_time = start_time.toInstant()
                                            .atZone(ZoneId.systemDefault()).toLocalTime();

                                    LocalTime localTimeEnd_time = end_time.toInstant()
                                            .atZone(ZoneId.systemDefault()).toLocalTime();
                                    LocalTime iTime = localTimeStart_time;

                                    start_time_list.add(localTimeStart_time);
                                    end_time_list.add(localTimeEnd_time);
                                    while (!iTime.isAfter(localTimeEnd_time.plusHours(1))){
                                        Event newEvent = new Event(id, selectedDate, iTime);
                                        Event.eventsList.add(newEvent);
                                        iTime = iTime.plusHours(1);
                                    }
                                }
                            }
                            setDayView();
                            if(selectedDate.isBefore(LocalDate.now())){
                                btn_datlich.setEnabled(false);
                                btn_datlich.setBackgroundTintList(ContextCompat.getColorStateList(
                                        DailyCalendarActivity.this, R.color.lightGray));
                            }else{
                                btn_datlich.setEnabled(true);
                                btn_datlich.setBackgroundTintList(ContextCompat.getColorStateList(
                                        DailyCalendarActivity.this, R.color.green));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        setDayView();
    }
    private Date getDateAtStartOfDay(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        Instant instant = startOfDay.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    // Phương thức để lấy thời điểm bắt đầu của ngày tiếp theo
    private Date getDateAtStartOfNextDay(LocalDate localDate) {
        LocalDateTime startOfNextDay = localDate.plusDays(1).atStartOfDay();
        Instant instant = startOfNextDay.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
    public void previousDayAction(View view)
    {
        selectedDate = selectedDate.minusDays(1);
        GetData();
    }

    public void nextDayAction(View view)
    {
        selectedDate = selectedDate.plusDays(1);
        GetData();
    }

    public void newEventAction(View view)
    {
//        startActivity(new Intent(this, EventEditActivity.class));

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                // Lấy giờ và phút được chọn
                LocalTime selectedTime = LocalTime.of(hourOfDay, minute);

                // Khởi tạo biến date1 và date2 với giá trị LocalTime tương ứng
                boolean isBetween = false;
                for (int i = 0; i < start_time_list.size(); i++) {
                    LocalTime start_Time = start_time_list.get(i);
                    LocalTime end_Time = end_time_list.get(i);

                    if (selectedTime.isAfter(start_Time) && selectedTime.isBefore(end_Time)) {
                        isBetween = true;
                        break;
                    }
                }
                LocalDateTime selectedDateTime = selectedDate.atTime(selectedTime);

                // Kiểm tra nếu giờ được chọn là khung giờ từ 11:PM - 5:PM
                if(selectedTime.isBefore(LocalTime.of(5,30))){
                    Toast.makeText(getApplicationContext(), "11PM - 5:30AM chưa phục vụ. Quý khách" +
                            " vui lòng chọn giờ khác", Toast.LENGTH_SHORT).show();
                }else if (selectedTime.isAfter(LocalTime.of(22,59))) {
                    Toast.makeText(getApplicationContext(), "11PM - 5:30AM chưa phục vụ. Quý khách" +
                            " vui lòng chọn giờ khác", Toast.LENGTH_SHORT).show();

                    //Kiểm tra nếu Giờ được chọn là giờ quá khứ
                }else if(selectedDateTime.isBefore(LocalDateTime.now())){
                    Toast.makeText(getApplicationContext(), "Giờ không hợp lệ"
                            , Toast.LENGTH_SHORT).show();
                } else if (isBetween) {
                    Toast.makeText(getApplicationContext(), "Giờ đã được chọn, vui lòng chọn lại giờ khác"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    // Giờ hợp lệ, tiếp tục xử lý
                    // ...
                    selected_StartTime = selectedTime;
                    showBottomDialog();
                }
            }
        }, 0, 0, false);

        timePickerDialog.show();
    }
    private void showBottomDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DailyCalendarActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_chonphut);

        NumberPicker numberPicker = bottomSheetDialog.findViewById(R.id.numberpicker_khachhang_chitietsan_sophut);
        Button btnHuy = bottomSheetDialog.findViewById(R.id.btn_khachhang_chitietsan_huy);
        Button btnChon = bottomSheetDialog.findViewById(R.id.btn_khachhang_chitietsan_chon);

        String[] phutValues = {"30", "60", "90", "120", "150", "180"};
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(phutValues.length - 1);
        numberPicker.setDisplayedValues(phutValues);

        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tv_chonphut.setText(phutValues[numberPicker.getValue()] + " phút");

                // Tạo một đối tượng Calendar từ selectedStartDate
                Date selectedStartDate = ConvertLocalDateTimeToDate(selectedDate, selected_StartTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedStartDate);

                // Thêm số phút được chọn vào selectedStartDate
                int phut = Integer.parseInt(phutValues[numberPicker.getValue()]);
                calendar.add(Calendar.MINUTE, phut);
                selected_EndTime = calendar.getTime();
                LocalTime selected_EndTime_LocalTime = ConvertDateToLocalTime(selected_EndTime);
                // Lưu selectedEndDate
                boolean isBetween = false;
                LocalTime start_Time_db = null;
                LocalTime end_Time_db = null;
                for (int i = 0; i < start_time_list.size(); i++) {
                    start_Time_db = start_time_list.get(i);
                    end_Time_db = end_time_list.get(i);

                    if (selected_EndTime_LocalTime.isAfter(start_Time_db) &&
                            selected_EndTime_LocalTime.isBefore(end_Time_db)) {
                        isBetween = true;
                        break;
                    }
                }
                if(!isBetween){
                    bottomSheetDialog.dismiss();
                    Toast.makeText(DailyCalendarActivity.this, "Oke", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DailyCalendarActivity.this, "Khung giờ bạn chọn đã có lịch đặt khác (" +
                            start_Time_db.getHour() + ":" + start_Time_db.getMinute() + " - " +
                            end_Time_db.getHour() + ":" + end_Time_db.getMinute() +
                            "). Vui lòng chọn giờ khác!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
    Date ConvertLocalDateTimeToDate(LocalDate localDate, LocalTime localTime){

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        ZoneId zoneId = ZoneId.systemDefault(); // Use a specific time zone, or specify a desired time zone

        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        Instant instant = zonedDateTime.toInstant();

        Date date = Date.from(instant);
        return date;
    }
    LocalTime ConvertDateToLocalTime(Date date){
        LocalTime localTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        return localTime;
    }
}