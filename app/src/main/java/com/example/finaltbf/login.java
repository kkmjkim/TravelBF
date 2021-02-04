package com.example.finaltbf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SignInButton btn_google;    //구글 로그인 버튼
    private FirebaseAuth auth;          //firebase인증 객체
    private GoogleApiClient googleApiClient;    //구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100;     //구글 로그인 결과 코드

    //    private CallbackManager mCallbackManger;    //facebooke연동
    private FirebaseAuth mFirebaseAuth;//facebooke연동
    //    private LoginButton loginButton;
//    private  static final String TAG = "FacebookAuthentication";
//    private FirebaseAuth.AuthStateListener authStateListener;
    private TextView textViewUser;
    private ImageView mLogo;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //앱이 실행될때 처음 실행 되는곳
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //구글 signIn 버튼 누를때 기본 옵션들
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();  //firebase 인증 객체 초기화

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {  //구글 로그인 버튼을 클릭했을 때 이곳을 수행
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);      //구글에서 인증 확인
                startActivityForResult(intent,REQ_SIGN_GOOGLE);
                Log.d("TBF", "로그인");
            }
        });



        textViewUser = findViewById(R.id.login_reusult);
        mLogo = findViewById(R.id.profile);

    }




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //구글 로그인 인증을 요청했을때 결과값을 돌려받는곳
//        mCallbackManger.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){     //인증결과 성공하면
                GoogleSignInAccount account = result.getSignInAccount();    //account라는 데이터는 구글 로그인 정보를 담고있음(닉네임,프로필사진 등등)
                resultLogin(account);   //로그인 결과 값 출력 수행하라는 메서드

                //firebase cloud firestore에 id저장
                mFirestore = FirebaseFirestore.getInstance();
                Map<String,Object> userId = new HashMap<>();
                userId.put("id",account.getDisplayName());

                auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                String UID = currentUser.getUid();

                mFirestore.collection(UID).document("userInfo").set(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"Values added!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {//로그인 실제 성공?
                        if(task.isSuccessful()){       //로그인 성공
                            Toast.makeText(login.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),loginResult.class);  //어디로 넘어가냐
                            intent.putExtra("id",account.getDisplayName());
                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));
                            startActivity(intent);
                            Log.d("TBF", "로그인 성공");
                        }else{
                            Toast.makeText(login.this,"로그인 실패",Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}