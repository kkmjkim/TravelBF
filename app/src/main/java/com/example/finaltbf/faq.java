package com.example.finaltbf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class faq extends AppCompatActivity implements View.OnClickListener{

    ImageButton backicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        backicon = findViewById(R.id.backicon);

        backicon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(faq.this,menu.class);
        startActivity(intent);
    }
}