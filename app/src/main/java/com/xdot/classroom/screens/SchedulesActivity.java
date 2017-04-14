package com.xdot.classroom.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xdot.classroom.R;


public class SchedulesActivity extends AppCompatActivity {

    final String TAG = "MY_DEBUG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        activateCustomActionBar();



    }


    private void activateCustomActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }


    public void clicked(View view) {
        Log.d(TAG, "Button clicked!");

        switch (view.getId()) {
            case R.id.imageView1:
                Log.d(TAG, "Button: Back");
                break;

            case R.id.imageView2:
                Log.d(TAG, "Button: Add Schedule");
                break;
        }
    }
}
