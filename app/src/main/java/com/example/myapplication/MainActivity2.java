package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "token not saved";
    private  FirebaseAuth firebaseAuth;

    public static final String NODE_USERS ="users";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("updates");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());

                            return;
                        }
                        else {
                            String token = task.getResult();

                           saveToken(token);
                        }

                    }
                });


    }
    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() == null) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    private void saveToken(String token){
       String email =  firebaseAuth.getCurrentUser().getEmail();
       User user =  new User(email,token);
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(NODE_USERS);
        databaseReference.child(firebaseAuth.getCurrentUser().getUid())
        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()){
            Toast.makeText(MainActivity2.this,"token saved",Toast.LENGTH_LONG).show();
        }
    }
});

    }



}