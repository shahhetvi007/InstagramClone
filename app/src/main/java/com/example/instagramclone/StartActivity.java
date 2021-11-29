package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class StartActivity extends AppCompatActivity {

    private static int SPLASH_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //remove title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //to delay the activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //after running start the activity
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                //if back pressed this activity should not be seen again
                finish();
            }
        }, SPLASH_TIMER);
    }
}