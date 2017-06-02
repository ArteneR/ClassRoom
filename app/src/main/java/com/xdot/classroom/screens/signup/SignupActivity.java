package com.xdot.classroom.screens.signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.activate_account.ActivateAccountActivity;



public class SignupActivity extends AppCompatActivity {
        private static String LOG_TAG = "SignupActivity";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

                activateCustomActionBar();
        }


        /*
         * Setup the custom action bar for navigation
         */
        private void activateCustomActionBar() {
                android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

                mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));
                mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#BB266195")));

                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
                LayoutInflater mInflater = LayoutInflater.from(this);

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_signup, null);

                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
        }


        /*
         * Handle user click events
         */
        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                        case R.id.ivLeftActionbarButton:
                                Log.d(LOG_TAG, "Button: Back");
                                goToPreviousActivity();
                                break;

                        case R.id.btnCreateAccount:
                                Log.d(LOG_TAG, "Button: Create Account");
                                createAccount();
                                break;

                        case R.id.tvEnterActivationCode:
                                Log.d(LOG_TAG, "Button: Enter Activation Code");
                                goToActivateAccountActivity();
                                break;
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void createAccount() {
                Log.d(LOG_TAG, "Creating account...");


                displayCreateAccountInfoMessage();
        }


        private void goToActivateAccountActivity() {
                Intent intent = new Intent(this, ActivateAccountActivity.class);
                this.startActivity(intent);
        }


        private void displayCreateAccountInfoMessage() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
                builder.setTitle("Your account has been created!")
                    .setMessage("Check your email!\n" +
                                "You should have received a confirmation email with an activation code that you must copy and paste into the account activation code box.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    goToActivateAccountActivity();
                            }
                    });
                AlertDialog alert = builder.create();
                alert.show();
        }
}
