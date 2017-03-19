package com.xdot.classroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSkipLogin = (TextView) findViewById(R.id.tvSkipLogin);
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
