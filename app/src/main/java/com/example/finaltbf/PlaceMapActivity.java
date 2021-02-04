package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//나중에
public class PlaceMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTextViewState;

    private Button show_review;
    private ImageButton list_button;
    private Button button_tel;
    private String mNum;
    private ImageButton mImageButton;
    private ImageButton route_button;
    private ImageButton menu_button;

    private FirebaseAuth auth;
    private TextView bt_showdata;
    private TextView tv_info;
    double lat;
    double lng;
    private TextView textView_address;
    String title;

    //추가
    GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<Place> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);

        View bottomSheet = findViewById(R.id.bottomsheet_custom);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mImageButton = findViewById(R.id.ib_star1);
        tv_info = findViewById(R.id.tv_info);
        textView_address = findViewById(R.id.textView_address);
        route_button = findViewById(R.id.route_button);
        route_button.setOnClickListener(this);

        menu_button= findViewById(R.id.menu_button);
        menu_button.setOnClickListener(this);


        markerList = new ArrayList<>();

        //카테고리 구분 버튼 -> 0:모든장소 1:관광지 2:음식점 3:숙박 4:쇼핑
        Button tourBtn = findViewById(R.id.btn_tour);
        Button restBtn = findViewById(R.id.btn_restaurant);
        Button accoBtn = findViewById(R.id.btn_accomodation);
        Button shopBtn = findViewById(R.id.btn_shopping);
        tourBtn.setTag("12"); //민진: 아예 처음부터 contenttypeid로
        restBtn.setTag("39");
        accoBtn.setTag("32");
        shopBtn.setTag("38");

        show_review = findViewById(R.id.show_review);
        show_review.setOnClickListener(this);
        list_button= findViewById(R.id.ib_list);
        list_button.setOnClickListener(this);
        button_tel= findViewById(R.id.button_tel);
        button_tel.setOnClickListener(this);


        //지도 객체 화면에 띄움
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

        //markerLoad("0"); //default: 모든 장소 마커 다 띄우기?? (민진)

        //카테고리 버튼 클릭리스너
        View.OnClickListener categoryListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String categoryNo = (String)v.getTag();
                //mTextViewState.setText(categoryNo); //테스트용
                markerLoad(categoryNo); ////민진: switch-case 대신 이거
            }
        };
        tourBtn.setOnClickListener(categoryListener);
        restBtn.setOnClickListener(categoryListener);
        accoBtn.setOnClickListener(categoryListener);
        shopBtn.setOnClickListener(categoryListener);


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                insertFavorites(lat, lng, title);
                if (view.isSelected()){
                    view.setSelected(false);
                } else {
                    view.setSelected(true);
                    //...Handled toggle on
                }
            }
        });


    }

    public void insertFavorites(double lat, double lng, String title){
        int NUM_OF_DAYS = 5;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Favorites").child("yuna");
        for(int i = 0; i < NUM_OF_DAYS; i++){
            Place place = new Place(lng, lat, title);
            databaseReference.child("Day" + Integer.toString(i+1)).child(place.getTitle()).setValue(place);

//            databaseReference.push().setValue(place); //generating a unique id? inaudible
        }
        Toast.makeText(PlaceMapActivity.this, "Fav inserted!", Toast.LENGTH_LONG).show();
    }

    //지도화면 로드될 때 이벤트 처리
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //markerLoad("0"); //모든 장소 출력
        mMap.setOnMarkerClickListener(this);
    }

    public void markerLoad(String categoryNum){
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("PlaceSample"); // 파이어베이스 Marker에서 가져옴 (예시)
        // 나중에 제대로된 데이터 가져오려면 Place 클래스 고쳐야함 -> 민진:전체 샘플인 PlaceSample 생성함

        //"0"일 때 밑에 addValueEventListener를 한 번 더 쓰지 않기 위해
        Query query;

        if(categoryNum.equals("12"))
            query = databaseReference.orderByChild("contenttypeid").equalTo("12");
        else if(categoryNum.equals("39"))
            query = databaseReference.orderByChild("contenttypeid").equalTo("39");
        else if(categoryNum.equals("32"))
            query = databaseReference.orderByChild("contenttypeid").equalTo("32");
        else
            query = databaseReference.orderByChild("contenttypeid").equalTo("38");


        //민진: 변경되지 않는 데이터 snapshot이면 addListenerForSingleValueEvent()쓴다고 함 (일단 스킵)
        query.addValueEventListener(new ValueEventListener() {
            @Override
            //아래 마커리스트라고 적힌 코드 나중에 다시 수정
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                markerList.clear(); //초기화
                //민진: 전체 데이터 목록인 datasnapshot의 개별 하위 요소를 loop으로 접근
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    markerList.add(place);
                }

                mMap.clear();
                for (int i = 0; i < markerList.size(); i++) {
                    double lat, lng;
                    // 값 추출
                    lat = markerList.get(i).getMapy();
                    lng = markerList.get(i).getMapx();
                    title = markerList.get(i).getTitle();

                    MarkerOptions options = new MarkerOptions();
                    LatLng pos = new LatLng(lat, lng);
                    options.position(pos);
                    //options.title(title);
                    //options.snippet(vicinity);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(pos));
                    marker.setTag(title);
                    //markers_list.add(marker);
                    //onMarkerClick(marker); //생성된 마커에 마커클릭리스너
                }

                //switch case문 없앰

                LatLng SEOUL = new LatLng(37.56, 126.96); //여기는 서울시청 위도경도값 넣으면 됨
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 14));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlaceMapActivity", String.valueOf(databaseError.toException()));
            }
        });
    }

    //마커 믈릭시 buttomsheet올라오고 정보 띄우기
    @Override
    public boolean onMarkerClick(Marker marker) {
        //아래부분 테스트용 코드 아직 완성안함
        //이쪽에서 마커 눌렀을 때 아래 bottom sheet 텍스트들 갱신해야함
        //아이디어 -> 클릭한 마커 객체의 Title과 아래 dataload함수의 Title을 비교해서
        //일치하는 데이터를 불러옴

        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
        title = (String)marker.getTag();
        mImageButton.setSelected(false);
        dataload(lat, lng, title);


        Toast.makeText(this, title, Toast.LENGTH_LONG).show();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return false;
    }

    public void dataload(double lat, double lng, String title){

        textView_address.setText(title);
        Toast.makeText(this, title+"2", Toast.LENGTH_LONG).show();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("PlaceSample").child(title).child("item");
//
//        DatabaseReference temp = database.getReference("PlaceSample").child(title);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //contentid 받고 그거 equal한거의 속성을 가져오기
                // = makeItems(contentTypeID).get("chkbabycarriage");
                tv_info.setText("");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tv_info.append(makeItems().get(snapshot.getKey()) + "\n");
                    tv_info.append(snapshot.getValue(String.class) + "\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Map<String, String> makeItems(){ //returns Map<>
        Map<String, String> tour = new HashMap<>();
//        Map<String, String> restaurant = new HashMap<>();
//        Map<String, String> accomodation = new HashMap<>();
//        Map<String, String> shopping = new HashMap<>();

        tour.put("accomcount", "수용인원");
        tour.put("chkbabycarriage", "유모차 대여 여부");
        tour.put("chkcreditcard", "신용카드 가능 여부");
        tour.put("chkpet", "애완동물 가능 여부");
        tour.put("expagerange", "체험가능 연령");
        tour.put("expguide", "체험안내");
        tour.put("contentid", "콘텐츠ID");
        tour.put("contenttypeid", "콘텐츠타입ID");
        tour.put("heritage1", "세계 문화유산 유무");
        tour.put("heritage2", "세계 자연유산 유무");
        tour.put("heritage3", "세계 기록유산 유무");
        tour.put("infocenter", "문의 및 안내");
        tour.put("opendate", "개장일");
        tour.put("parking", "주차시설");
        tour.put("restdate", "쉬는날");
        tour.put("useseason", "이용시기");
        tour.put("usetime", "이용시간");

        tour.put("accomcountculture", "수용인원");
        tour.put("chkbabycarriageculture", "유모차 대여 여부");
        tour.put("chkcreditcardculture", "신용카드 가능 여부");
        tour.put("chkpetculture", "애완동물 가능 여부");
        tour.put("discountinfo", "할인정보");
        tour.put("infocenterculture", "문의 및 안내");
        tour.put("parkingculture", "주차시설");
        tour.put("parkingfee", "주차요금");
        tour.put("restdateculture", "쉬는날");
        tour.put("usefee", "이용요금");
        tour.put("usetimeculture", "이용시간");
        tour.put("scale", "규모");
        tour.put("spendtime", "관람 소요시간");

        tour.put("chkcreditcardfood", "신용카드가능 정보");
        tour.put("discountinfofood", "할인정보");
        tour.put("firstmenu", "대표 메뉴");
        tour.put("infocenterfood", "문의 및 안내");
        tour.put("kidsfacility", "어린이 놀이방 여부");
        tour.put("opendatefood", "개업일");
        tour.put("opentimefood", "영업시간");
        tour.put("packing", "포장 가능");
        tour.put("parkingfood", "주차시설");
        tour.put("reservationfood", "예약안내");
        tour.put("restdatefood", "쉬는날");
        tour.put("scalefood", "규모");
        tour.put("seat", "좌석수");
        tour.put("smoking", "금연/흡연 여부");
        tour.put("treatmenu", "취급 메뉴");

        tour.put("accomcountlodging", "수용 가능인원");
        tour.put("benikia", "베니키아 여부");
        tour.put("checkintime", "입실 시간");
        tour.put("checkouttime", "퇴실 시간");
        tour.put("chkcooking", "객실내 취사 여부");
        tour.put("foodplace", "식음료장");
        tour.put("goodstay", "굿스테이 여부");
        tour.put("hanok", "한옥 여부");
        tour.put("infocenterlodging", "문의 및 안내");
        tour.put("parkinglodging", "주차시설");
        tour.put("pickup", "픽업 서비스");
        tour.put("roomcount", "객실수");
        tour.put("reservationlodging", "예약안내");
        tour.put("reservationurl", "예약안내 홈페이지");
        tour.put("roomtype", "객실유형");
        tour.put("scalelodging", "규모");
        tour.put("subfacility", "부대시설 (기타)");
        tour.put("barbecue", "바비큐장 여부");
        tour.put("beauty", "뷰티시설 정보");
        tour.put("beverage", "식음료장 여부");
        tour.put("bicycle", "자전거 대여 여부");
        tour.put("campfire", "캠프파이어 여부");
        tour.put("fitness", "휘트니스 센터 여부");
        tour.put("karaoke", "노래방 여부");
        tour.put("publicbath", "공용 샤워실 여부");
        tour.put("publicpc", "공용 PC실 여부");
        tour.put("sauna", "사우나실 여부");
        tour.put("seminar", "세미나실 여부");
        tour.put("sports", "스포츠 시설 여부");
        tour.put("refundregulation", "환불규정");

        tour.put("chkbabycarriageshopping", "유모차대여 정보");
        tour.put("chkcreditcardshopping", "신용카드가능 정보");
        tour.put("chkpetshopping", "애완동물동반가능 정보");
        tour.put("culturecenter", "문화센터 바로가기");
        tour.put("fairday", "장서는 날");
        tour.put("infocentershopping", "문의 및 안내");
        tour.put("opendateshopping", "개장일");
        tour.put("opentime", "영업시간");
        tour.put("parkingshopping", "주차시설");
        tour.put("restdateshopping", "쉬는날");
        tour.put("restroom", "화장실 설명");
        tour.put("saleitem", "판매 품목");
        tour.put("saleitemcost", "판매 품목별 가격");
        tour.put("scaleshopping", "규모");
        tour.put("shopguide", "매장안내");
        tour.put("exit", "출입통로");
        tour.put("guidehuman", "안내요원");
        tour.put("publictransport", "접근로");
        tour.put("restroom1", "화장실");
        tour.put("route", "대중교통");
        tour.put("parking1", "주차여부");
        tour.put("handicapetc", "지체장애 기타 상세");
        return tour;
    }

    public void onClick(View view) {

        if(view == list_button){
            Intent intent = new Intent(PlaceMapActivity.this, PlaceListActivity.class);
            startActivity(intent);
        }

        if(view ==button_tel){
            mNum=button_tel.getText().toString();
            String tel = "tel:" +mNum;
            startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
        }

        if(view ==show_review){
            Intent intent = new Intent(PlaceMapActivity.this, ShowReviewActivity.class);
            startActivity(intent);
        }

        if(view ==route_button){
            Intent intent = new Intent(PlaceMapActivity.this, MainActivity2.class);
            startActivity(intent);
        }

        if(view ==menu_button){
            Intent intent = new Intent(PlaceMapActivity.this, menu.class);
            startActivity(intent);
        }
    }

}