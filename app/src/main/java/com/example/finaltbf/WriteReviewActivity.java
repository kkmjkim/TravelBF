package com.example.finaltbf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class WriteReviewActivity extends AppCompatActivity{

    private TextView tvReviewPlace;
    private EditText etWriteReview;
    private RatingBar rbRatingBar;
    private ImageButton ibSaveReview;
    private DatabaseReference databaseReference;
    private TextView star;
    float ratingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        tvReviewPlace = (TextView)findViewById(R.id.review_place);
        etWriteReview = (EditText) findViewById(R.id.write_review);
        rbRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        ibSaveReview = (ImageButton) findViewById(R.id.save_review);

        //intent로 contentid(String)이랑 장소이름(String) 가져온다
        //이름은 textview로, contentid는 firebase에 리뷰 저장할 때 키로 쓴다

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Place");

        ibSaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                insertReviewItem();
                //채워진 별 갯수 반환
                RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
                ratingValue = ratingBar.getRating();
                EditText editText = (EditText)findViewById(R.id.write_review);
                String write_review = editText.getText().toString();
//                Toast.makeText(WriteReviewActivity.this,"저장 성공",Toast.LENGTH_SHORT).show();
//                rbRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//                    @Override
//                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                        star.setText(String.valueOf(rating));
//                    }
//                });

                Intent intent = new Intent(WriteReviewActivity.this,ShowReviewActivity.class);
//                intent.putExtra("star value", ratingValue);
//                intent.putExtra("write_review", write_review);
                startActivity(intent);

                //ShowReviewActivity에
//                Intent secondIntent =getIntent();
//String obstacle = secondIntent.getIntExtra("star value");
//String trip_date = secondIntent.getStringExtra("write_review");
            }
        });
    }

    private void insertReviewItem(){
        String review = etWriteReview.getText().toString();
        double rate = rbRatingBar.getRating();

        //Firebase에 Date 넣는 방식이라는데..
        Map date = new HashMap();
        date.put("timestamp", ServerValue.TIMESTAMP);
        databaseReference.child("yourNode").updateChildren(date);
        String contentId = "100";
        ReviewItem reviewItem = new ReviewItem(rate, review, date, contentId);

        databaseReference.push().setValue(reviewItem); //generating a unique id? inaudible
        Toast.makeText(WriteReviewActivity.this, "Data inserted!"+ Float.toString(ratingValue), Toast.LENGTH_LONG).show();
    }
}