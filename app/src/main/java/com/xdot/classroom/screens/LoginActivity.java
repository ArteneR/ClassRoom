package com.xdot.classroom.screens;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;


public class LoginActivity extends AppCompatActivity {

    Button btnLogIn;
    TextView tvForgotPassword;
    TextView tvSignUp;
    TextView tvSkipLogin;
    final String TAG = "MY_DEBUG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeDataProviderModule();

        hideActionBar();

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSkipLogin = (TextView) findViewById(R.id.tvSkipLogin);
    }


    private void initializeDataProviderModule() {
        DataProvider dataProvider = (DataProvider)getApplication();
        dataProvider.init();
        dataProvider.printSchedules();
    }


    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    public void clicked(View view) {
        Log.d(TAG, "Button clicked!");

        switch (view.getId()) {
            case R.id.btnLogIn:
                Log.d(TAG, "Button: " + btnLogIn.getText());
                break;

            case R.id.tvForgotPassword:
                Log.d(TAG, "Button: " + tvForgotPassword.getText());
                break;

            case R.id.tvSignUp:
                Log.d(TAG, "Button: " + tvSignUp.getText());
                break;

            case R.id.tvSkipLogin:
                Log.d(TAG, "Button: " + tvSkipLogin.getText());
                break;
        }
    }


}
