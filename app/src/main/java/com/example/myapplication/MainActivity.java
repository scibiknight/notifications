package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {
public static final String  CHANNEL_ID ="simple_notification";
public static final String  CHANNEL_NAME="simple notification";
public static final String   CHANNEL_DESC="my notification test1";
        private static final String TAG ="token not saved" ;

        private EditText edtpwd;
private EditText  edt1;
private ProgressBar bar;
private FirebaseAuth firebaseAuth;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(CHANNEL_DESC);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
        }

        bar = findViewById(R.id.bar);
        edtpwd = findViewById(R.id.edtpwd);
        edt1 = findViewById(R.id.edt1);
        firebaseAuth = FirebaseAuth.getInstance();
        bar.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        createUser();
                }
        });



}
private void createUser() {

        String email =edt1.getText().toString().trim();

        String pwd = edtpwd.getText().toString().trim();

        if(email.isEmpty()){
        edt1.setError("email required");
        edt1.requestFocus();
        return;
        }
        if (pwd.isEmpty()){
        edtpwd.setError("password required");
        edtpwd.requestFocus();
        return;

        }
        if (pwd.length()<6){
        edtpwd.setError("password must be atlest 6 charecters");
        edtpwd.requestFocus();
        return;

        }
        bar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pwd)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){

        startProfileActivity();
        }
        else {
        if(task.getException()instanceof FirebaseAuthUserCollisionException){
        userLogin(email,pwd);
        }else {
        bar.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
        }
        }
        }
        });
        }
private void userLogin(String email, String pwd){
        firebaseAuth.signInWithEmailAndPassword(email,pwd)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
        startProfileActivity();
        }
        else{
        bar.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

        }
        }
        });
        }
        @Override
        protected void onStart() {
                super.onStart();

                if(firebaseAuth.getCurrentUser()!=null) {
                        startProfileActivity();
                }
        }
private void startProfileActivity(){
        Intent intent = new Intent(this,MainActivity2.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


        }

        }