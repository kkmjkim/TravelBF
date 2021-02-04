package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<PlaceItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Toast.makeText(PlaceListActivity.this, "firebase connection success", Toast.LENGTH_LONG).show();

        recyclerView = (RecyclerView) findViewById(R.id.place_recycler_view); //
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        //efmsfn fnsf

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("PlaceSample"); // firebase에서의 이름

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //FB의 DB 데이터를 받아오는 곳
                arrayList.clear(); //혹시 몰라 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PlaceItem placeItem = snapshot.getValue(PlaceItem.class);
                    arrayList.add(placeItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //DB를 가져오던 중 에러 발생 시
                Log.e("PlaceListActivity", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new PlaceAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }
}