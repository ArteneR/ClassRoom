package com.xdot.classroom.screens.activate_account;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;



public class ActivateAccountActivity extends AppCompatActivity {
        private static String LOG_TAG = "ActivateAccountActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private String registeredEmailAddress;
        private EditText etActivationCode;
        private EditText etEmailAddress;
        private ProgressBar progressBarActivateAccount;
        private String enteredActivationCode;
        private String enteredEmailAddress;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_activate_account);

                connectToFirebase();

                initializeUIElements();
                registeredEmailAddress = getIntent().getStringExtra("email_address");
                etEmailAddress.setText(registeredEmailAddress);

                activateCustomActionBar();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
        }


        private void initializeUIElements() {
                etActivationCode = (EditText) findViewById(R.id.etActivationCode);
                etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
                progressBarActivateAccount = (ProgressBar) findViewById(R.id.progressBarActivateAccount);
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

                View mCustomView = mInflater.inflate(R.layout.custom_actionbar_activate_account, null);

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

                    case R.id.btnActivateAccount:
                        Log.d(LOG_TAG, "Button: Activate Account");
                        activateAccount();
                        break;

                    case R.id.tvResendActivationCode:
                        Log.d(LOG_TAG, "Button: Resend Activation Code");
                        resendActivationCode();
                        break;
                }
        }


        private void goToPreviousActivity() {
                super.onBackPressed();
        }


        private void activateAccount() {
                progressBarActivateAccount.setVisibility(View.VISIBLE);
                getUserEnteredValues();

                if (isEnteredInputValid()) {
                        activateFirebaseAccount();
                }
                else {
                        progressBarActivateAccount.setVisibility(View.GONE);
                }
        }


        private void resendActivationCode() {

        }


        private void getUserEnteredValues() {
                enteredActivationCode = etActivationCode.getText().toString().toUpperCase();
                enteredEmailAddress = etEmailAddress.getText().toString();
        }


        private boolean isEnteredInputValid() {
                if (CommonFunctionalities.isFieldEmpty(enteredActivationCode)) {
                        CommonFunctionalities.displayLongToast("Activation Code field is required!", getApplicationContext());
                        return false;
                }

                return true;
        }


        private void activateFirebaseAccount() {
                firebaseDBRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot usersSnapshot) {
                                for (DataSnapshot userSnapshot: usersSnapshot.getChildren()) {
                                    String currentUserKey = userSnapshot.getKey();
                                    String currentUserEmail = userSnapshot.child("Email").getValue().toString();
                                    String activationCode = userSnapshot.child("ActivationCode").getValue().toString();

                                    if (currentUserEmail.equals(enteredEmailAddress)) {
                                            if (activationCode.equals(enteredActivationCode)) {
                                                    firebaseDBRef.child("Users").child(currentUserKey).child("AccountActivated").setValue(true);
                                                    CommonFunctionalities.displayLongToast("Account has been successfully activated!", getApplicationContext());
                                                    progressBarActivateAccount.setVisibility(View.GONE);
                                                    goToPreviousActivity();
                                                    return ;
                                            }
                                    }
                                }

                                CommonFunctionalities.displayLongToast("The account couldn't be activated! Maybe the specified email address or activation code is incorrect.", getApplicationContext());
                                progressBarActivateAccount.setVisibility(View.GONE);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }

}
