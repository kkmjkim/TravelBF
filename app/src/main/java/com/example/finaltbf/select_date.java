package com.example.finaltbf;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class select_date extends AppCompatActivity  implements View.OnClickListener {


    Button selectStartDate;
    TextView startDate;
    Button selectEndDate;
    TextView endDate;
    Button tripDate;
    TextView showTripDay;
    Button ok;
    Button textView2;

    private FirebaseAuth auth;

    DatePickerDialog datePickerDialog;
    int year1;
    int month1;
    int dayOfMonth1;
    int year2;
    int month2;
    int dayOfMonth2;
    Calendar FirstCalendar;
    Calendar SecondCalendar;
    long diffMonth;
    long diffDay;
    DatePicker d1;
    DatePicker d2;

    FirebaseFirestore mFirestore;
    String username_obstacle;
    String trip_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        selectStartDate = findViewById(R.id.startDate);
        startDate = findViewById(R.id.showStartdate);

        selectEndDate = findViewById(R.id.endDate);
        endDate = findViewById(R.id.showEnddate);

        tripDate = findViewById(R.id.tripDate);
        showTripDay = findViewById(R.id.showTripDay);

        ok = findViewById(R.id.ok);
        ok = findViewById(R.id.ok);

        ok.setOnClickListener(this);

        textView2 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView2);



        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstCalendar = Calendar.getInstance();//달력 생성하기
//                d1 = FirstCalendar.getTime();
                year1 = FirstCalendar.get(Calendar.YEAR);
                month1 = FirstCalendar.get(Calendar.MONTH);
                dayOfMonth1 = FirstCalendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(select_date.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                startDate.setText(year + "/" + (month + 1) + "/" + day);
                            }
                        }, year1, month1, dayOfMonth1);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                d1 = datePickerDialog.getDatePicker();
                datePickerDialog.show();


            }
        });

        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondCalendar = Calendar.getInstance();
//                d2 = SecondCalendar.getTime();
                year2 = SecondCalendar.get(Calendar.YEAR);
                month2 = SecondCalendar.get(Calendar.MONTH);
                dayOfMonth2 = SecondCalendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(select_date.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                endDate.setText(year + "/" + (month + 1) + "/" + day);
                            }
                        }, year2, month2, dayOfMonth2);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                d2 = datePickerDialog.getDatePicker();
                datePickerDialog.show();


            }

        });


        //날짜계산 다시하기~!!!
        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diffMonth = d2.getMonth() - d1.getMonth();
                if (diffMonth != 0) {
                    diffMonth = diffMonth * 30;
                    diffDay = (d2.getDayOfMonth() + diffMonth) - d1.getDayOfMonth();
                } else {
                    diffDay = d2.getDayOfMonth() - d1.getDayOfMonth() + 1;
                }
                String resultDay = String.valueOf(diffDay);
                showTripDay.setText(resultDay);

                mFirestore = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                String UID = currentUser.getUid();
                Map<String, Object> userId = new HashMap<>();
                userId.put("traveling days", resultDay);
                mFirestore.collection(UID).document("userInfo").set(userId, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                    Toast.makeText(login.this,"Values added!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == selectStartDate || view == selectEndDate) {
            mFirestore = FirebaseFirestore.getInstance();
            Map<String, Object> userId = new HashMap<>();

            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            String UID = currentUser.getUid();

            mFirestore.collection(UID).document("userInfo").set(userId, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                    Toast.makeText(login.this,"Values added!",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        //유나 -> 민진 map화면으로 연결 || view == textView2
        if (view == ok) {


            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            String UID = currentUser.getUid();

            mFirestore = FirebaseFirestore.getInstance();
            DocumentReference noteRef = mFirestore.collection(UID).document("userInfo");


            noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        username_obstacle = documentSnapshot.getString("obstacle");
                        trip_date = documentSnapshot.getString("traveling days");
                    }
                }
            });

            //이것 추가 (지금 민진거랑 합치지 않아서 에러남
            Intent intent = new Intent(select_date.this, PlaceMapActivity.class);
            intent.putExtra("obstacle", username_obstacle);
            intent.putExtra("traveling days",trip_date);
//            Toast.makeText(select_date.this,"Success",Toast.LENGTH_SHORT).show();
            startActivity(intent);


        }
    }
}

//민진's PlaceMapActivity에 추가(intent로 data 받기)(장애유형 ,여행날짜)
//Intent secondIntent =getIntent();
//String obstacle = secondIntent.getStringExtra("obstacle");
//String trip_date = secondIntent.getStringExtra("traveling days");
