package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class menu extends AppCompatActivity implements View.OnClickListener{

    Button obstalceBtn;
    Button faq;
    Button logout;

    private FirebaseAuth auth;


    //    Button more_select;
    // 이벤트 처리를 위해 dialog 객체를 멤버변수로 선언
    AlertDialog customDialog; // android.app.AlertDialog
    AlertDialog listDialog; // android.app.AlertDialog
    AlertDialog alertDialog; // android.app.AlertDialog

    TextView userid;
    TextView user_obstacle;
    String username;


//    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
//    private DocumentReference noteRef = mFirestore.document("UID/userInfo");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // View 객체 획득
        obstalceBtn = findViewById(R.id.obstalce_button);
        faq = findViewById(R.id.faq);
        userid = findViewById(R.id.userid);

        user_obstacle= findViewById(R.id.user_obstacle);
        logout = findViewById(R.id.logout);

//        more_select = findViewById(R.id.more_select);
// 버튼 이벤트 등록
        obstalceBtn.setOnClickListener(this);
        faq.setOnClickListener(this);
        logout.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String UID = currentUser.getUid();

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        DocumentReference noteRef = mFirestore.collection(UID).document("userInfo");


        noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    username = documentSnapshot.getString("id");
                    String username_obstacle = documentSnapshot.getString("obstacle");

                    userid.setText(""+ username);
                    user_obstacle.setText(" "+ username_obstacle);

                }
            }
        });

    }


    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final String[] items = new String[]{"주차여부", "대중교통", "접근로", "매표소", "홍보물", "휠체어", "출입통로", "엘리베이터", "화장실", "관람석", "객실", "지체장애 기타 상세"};

            if (dialog == listDialog) {
                listDialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                builder.setTitle("세부 사항 선택");
                builder.setMultiChoiceItems(items,null, new DialogInterface.OnMultiChoiceClickListener() {

                    final List<String> selectedItems = new ArrayList<String>();

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItems.add(items[which]);
                        } else {
                            selectedItems.remove(items[which]);
                        }

                    }
                });

                builder.setPositiveButton("확인", null);
                builder.setNegativeButton("취소", null);
                builder.show();

            }
        }
    };



    public void onClick(View view) {
        if (view == obstalceBtn) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("장애 선택");
            builder.setSingleChoiceItems(R.array.edit_array, 0, dialogListener);
            listDialog = builder.create();
            listDialog.show();
        }
        if(view ==faq){
            Intent intent = new Intent(menu.this,faq.class);
            startActivity(intent);
        }

    }
}
