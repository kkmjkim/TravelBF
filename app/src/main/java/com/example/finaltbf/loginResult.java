package com.example.finaltbf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class loginResult extends AppCompatActivity implements View.OnClickListener{

    private TextView login_reusult;     //닉네임 text
    private ImageView profile;          //이미지 뷰(프로필)
    Button ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");        //loginActivity로 부터 닉네임 전닯받음
        String photoUrl = intent.getStringExtra("photoUrl");        //loginActivity로 부터 프로필 사진 Url 전달 받음


        login_reusult = findViewById(R.id.login_reusult);
        login_reusult.setText(id);        //닉네임 text를 텍스트 뷰에 세팅

        profile = findViewById(R.id.profile);
        Glide.with(this).load(photoUrl).into(profile);  //프로필 url를 이비지 뷰에 세팅
    }

    @Override
    public void onClick(View view) {
        if(view == ok) {
            Intent intent = new Intent(loginResult.this, obstacle.class);
            startActivity(intent);
        }
    }
}