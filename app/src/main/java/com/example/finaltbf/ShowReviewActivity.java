package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReviewItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_review);
        Toast.makeText(ShowReviewActivity.this, "firebase connection success", Toast.LENGTH_LONG).show();

        recyclerView = (RecyclerView) findViewById(R.id.review_recycler_view); //
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        //efmsfn fnsf
        Intent secondIntent =getIntent();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Place"); // firebase에서의 이름
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //FB의 DB 데이터를 받아오는 곳
                arrayList.clear(); //혹시 몰라 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ReviewItem reviewItem = snapshot.getValue(ReviewItem.class);
                    arrayList.add(reviewItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오던 중 에러 발생 시
                Log.e("MainActivity2", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new ReviewAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

    }
}