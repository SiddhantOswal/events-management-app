package com.aspinax.lanaevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Database db;
    private Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                switch(resultCode) {
                    case 0:
                        p = (Person) result.get(mAuth.getUid());
                        break;
                }
            }

            @Override
            public void resultHandler(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), NoAuthActivity.class));
            finish();
        } else {
            db.read("users", currentUser.getUid(), Person.class, 0);
        }
    }
}