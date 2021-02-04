package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class obstacle extends AppCompatActivity implements View.OnClickListener{
    Button more_select;
    RadioButton radio0;
    RadioButton radio1;
    RadioButton radio2;
    RadioButton radio3;
    String str;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle);

        more_select = findViewById(R.id.more_select);
        radio0 = findViewById(R.id.radio0);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);

        more_select.setOnClickListener(this);

        radio0.setOnClickListener(this);
        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view==more_select){
            Intent intent = new Intent(obstacle.this,menu.class);
            startActivity(intent);

        }


        if(view==radio0||view==radio1||view==radio2||view==radio3){
            mFirestore = FirebaseFirestore.getInstance();
            Map<String,Object> userId = new HashMap<>();
            if(view==radio0){
                userId.put("obstacle","시각 장애인");
            }else if(view ==radio1){
                userId.put("obstacle","청각 장애인");
            }
            else if(view ==radio2){
                userId.put("obstacle","지체 장애인");
            }else if(view ==radio3){
                userId.put("obstacle","비 장애인");
            }


            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            String UID = currentUser.getUid();

            mFirestore.collection(UID).document("userInfo").set(userId, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
//                    Toast.makeText(login.this,"Values added!",Toast.LENGTH_SHORT).show();
//                }
                    }
                }});
            Intent intent = new Intent(obstacle.this,select_date.class);

            startActivity(intent);


        }


    }
}