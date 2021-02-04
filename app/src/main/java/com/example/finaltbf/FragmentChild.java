package com.example.finaltbf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentChild extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    String pageNum;
    TextView textViewChildName;
    MapView mapView;
    GoogleMap map;
    TextView mTextView;
    Button resetBtn;
    ArrayList<Double> a1;
    ArrayList<Double> a2;
    ArrayList<String> a3;
    Double latitude ;
    Double longitude ;
    String title;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;


    //구글맵에 표시될 데이터 저장 리스트
    ArrayList<Place> testList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        // 여기까지 새로운 화면에 fragment 띄움
        Bundle bundle = getArguments();
        pageNum = bundle.getString("data");
        getIDs(view);
        resetBtn = view.findViewById(R.id.reset_button);
        mTextView = (TextView) view.findViewById(R.id.textViewChild);
        getIDs(view);
        dataSet();


        testList = new ArrayList<>();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTextView.setText("");
                dataReset();
                a1.clear();
                a2.clear();
                a3.clear();
                map.clear();
                dataSet();
            }
        });

        return view;
    }

    private void getIDs(View view) {
        textViewChildName = (TextView) view.findViewById(R.id.day);
        textViewChildName.setText("  장소를 클릭해 경로를 설정하세요.");
    }

    private void dataSet() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna").child(pageNum);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //아래 마커리스트라고 적힌 코드 나중에 다시 수정
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    testList.add(place);
                }

                for (int i = 0; i < testList.size(); i++) {
                    double lat, lng;
                    String title;
                    // 값 추출
                    lat = testList.get(i).getMapy();
                    lng = testList.get(i).getMapx();
                    title = testList.get(i).getTitle();
                    MarkerOptions options = new MarkerOptions();
                    LatLng pos = new LatLng(lat, lng);
                    options.position(pos);
                    options.title(title);
                    //options.snippet(vicinity);
                    Marker marker = map.addMarker(options);
                    //markers_list.add(marker);
                    //onMarkerClick(marker); //생성된 마커에 마커클릭리스너
                }

                //switch case문 없앰
                LatLng SEOUL = new LatLng(37.570442502,126.9685079323); //여기는 서울시청 위도경도값 넣으면 됨
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 14));

                a1 = new ArrayList<>();
                a2 = new ArrayList<>();
                a3 = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    int a = place.getOrder();
                    if (a > 0){
                        a1.add(place.getMapy());
                        a2.add(place.getMapx());
                        a3.add(place.getTitle());
                        //marker.remove();
                        MarkerOptions options = new MarkerOptions();

                        options.position(new LatLng(place.getMapy(), place.getMapx()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        map.addMarker(options);
                        makeLine(a1.size());
                    }

                    //textViewChildName.setText(String.valueOf(a1.size()));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaceMapActivity", String.valueOf(databaseError.toException()));
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        map.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // 클릭시 리스트에 추가
        MarkerOptions options = new MarkerOptions();
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        title = marker.getTitle();
        a1.add(latitude);
        a2.add(longitude);
        a3.add(title);

        marker.remove();

        options.position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(options);

        makeLine(a1.size());

        Toast.makeText(getContext(), "경로가 추가되었습니다.", Toast.LENGTH_LONG).show();

        orderSet(a1.size());

        return true;
    }

    public void makeLine(int n){
        if(n == 1){
            String id = String.valueOf(n);
            String name = a3.get(n-1);

            String str = String.format(getResources()
                    .getString(R.string.textview_message), id, name);

            mTextView.append(str);
            Log.d("TBF", "경로 추가");
        }

        else if(n >=2 && n <= testList.size()){ //조건 추가 더 많이 클릭시
            Polyline polyline = map.addPolyline((new PolylineOptions())
                    .width(10)
                    .color(Color.parseColor("#3CB371"))
                    .startCap(new SquareCap())
                    .endCap(new SquareCap())
                    .clickable(true)
                    .add(
                            new LatLng(a1.get(n-2),a2.get(n-2)),
                            new LatLng(a1.get(n-1),a2.get(n-1))));
            polyline.setTag("경로");

            String id = String.valueOf(n);
            String name = a3.get(n-1);

            String str = String.format(getResources()
                    .getString(R.string.textview_message), id, name);

            mTextView.append(str);
            Log.d("TBF", "경로 추가");
        }
    }
    public void orderSet(int n){
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna").child(pageNum);

        Place place = new Place(longitude, latitude, title, n);
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna").child(pageNum);
        databaseReference2.child(title).setValue(place);
        textViewChildName.setText(title);
    }


    public void dataReset() {
        for (int i = 0 ; i < testList.size() ; i++) {
            Place place = new Place(testList.get(i).getMapy(), testList.get(i).getMapx(), testList.get(i).getTitle(), 0);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna").child(pageNum);
            databaseReference.child(testList.get(i).getTitle()).setValue(place);
        }
        /*Place place = new Place(testList.get(0).getMapx(), testList.get(0).getMapy(), testList.get(0).getTitle(), 0);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna").child(pageNum);
        databaseReference.child("fav1").setValue(place);*/
    }
}