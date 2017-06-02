package com.xdot.classroom.screens.signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.activate_account.ActivateAccountActivity;



public class SignupActivity extends AppCompatActivity {
        private static String LOG_TAG = "SignupActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser currentUser;
        private EditText etFirstname;
        private EditText etLastname;
        private EditText etEmail;
        private EditText etPassword;
        private EditText etRePassword;
        private String enteredFirstname;
        private String enteredLastname;
        private String enteredEmail;
        private String enteredPassword;
        private String enteredRePassword;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

                activateCustomActionBar();
                initializeUIElements();

                connectToFirebase();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
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


        private void initializeUIElements() {
                etFirstname = (EditText) findViewById(R.id.etFirstname);
                etLastname = (EditText) findViewById(R.id.etLastname);
                etEmail = (EditText) findViewById(R.id.etEmail);
                etPassword = (EditText) findViewById(R.id.etPassword);
                etRePassword = (EditText) findViewById(R.id.etRePassword);
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
                getUserEnteredValues();

                if (isEnteredInputValid()) {
                        createFirebaseAccount();
                }
        }


        private void createFirebaseAccount() {
                firebaseAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                            currentUser = firebaseAuth.getCurrentUser();

                                            // Here we should generate unique code and store in DB and send email to user

                                            createDBEntryForUser();

                                            clearAllInputFields();
                                            displayCreateAccountInfoMessage();
                                    }
                                    else {
                                            Log.w(LOG_TAG, "createUserWithEmail: Failure!", task.getException());
                                            CommonFunctionalities.displayLongToast("Authentication failed.", getApplicationContext());
                                    }
                            }
                    });
        }


        private void createDBEntryForUser() {
                String activationCode = "TEST_CODE";

                DatabaseReference usersRef = firebaseDBRef.child("Users").child(currentUser.getUid());
                usersRef.child("AccountActivated").setValue(false);
                usersRef.child("ActivationCode").setValue(activationCode);
                usersRef.child("Email").setValue(currentUser.getEmail());
                usersRef.child("Firstname").setValue(enteredFirstname);
                usersRef.child("Lastname").setValue(enteredLastname);
        }


        private void goToActivateAccountActivity() {
                Intent intent = new Intent(this, ActivateAccountActivity.class);
                this.startActivity(intent);
        }


        private void getUserEnteredValues() {
                enteredFirstname = etFirstname.getText().toString();
                enteredLastname = etLastname.getText().toString();
                enteredEmail = etEmail.getText().toString();
                enteredPassword = etPassword.getText().toString();
                enteredRePassword = etRePassword.getText().toString();
        }


        private boolean isEnteredInputValid() {
                if (CommonFunctionalities.isFieldEmpty(enteredFirstname)) {
                        CommonFunctionalities.displayLongToast("Firstname field is required!", getApplicationContext());
                        return false;
                }

                if (CommonFunctionalities.isFieldEmpty(enteredLastname)) {
                        CommonFunctionalities.displayLongToast("Lastname field is required!", getApplicationContext());
                        return false;
                }

                if (!CommonFunctionalities.isFieldValidEmail(enteredEmail)) {
                        CommonFunctionalities.displayLongToast("The email address doesn't seem to be valid!", getApplicationContext());
                        return false;
                }

                if (CommonFunctionalities.isFieldEmpty(enteredPassword)) {
                        CommonFunctionalities.displayLongToast("Password field is required!", getApplicationContext());
                        return false;
                }

                if (CommonFunctionalities.isFieldEmpty(enteredRePassword)) {
                        CommonFunctionalities.displayLongToast("Re-Password field is required!", getApplicationContext());
                        return false;
                }

                if (!CommonFunctionalities.areFieldsEqual(enteredPassword, enteredRePassword)) {
                        CommonFunctionalities.displayLongToast("Passwords don't match!", getApplicationContext());
                        return false;
                }

                return true;
        }


        private void clearAllInputFields() {
                etFirstname.setText(null);
                etLastname.setText(null);
                etEmail.setText(null);
                etPassword.setText(null);
                etRePassword.setText(null);
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
