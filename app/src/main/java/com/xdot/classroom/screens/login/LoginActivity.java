package com.xdot.classroom.screens.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xdot.classroom.CommonFunctionalities;
import com.xdot.classroom.DataProvider;
import com.xdot.classroom.R;
import com.xdot.classroom.screens.schedules.SchedulesActivity;
import com.xdot.classroom.screens.signup.SignupActivity;



public class LoginActivity extends AppCompatActivity {
        private static String LOG_TAG = "LoginActivity";
        private FirebaseDatabase firebaseDB;
        private DatabaseReference firebaseDBRef;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser currentUser;
        private Button btnLogIn;
        private TextView tvForgotPassword;
        private TextView tvSignUp;
        private TextView tvSkipLogin;
        private EditText etUsername;
        private EditText etPassword;
        private ProgressBar progressBarLogin;
        private String enteredUsername;
        private String enteredPassword;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                connectToFirebase();

                initializeDataProviderModule();
                initializeUIElements();

                hideActionBar();
        }


        private void connectToFirebase() {
                firebaseDB = FirebaseDatabase.getInstance();
                firebaseDBRef = firebaseDB.getReference();
                firebaseAuth = FirebaseAuth.getInstance();
        }


        private void initializeDataProviderModule() {
                DataProvider dataProvider = (DataProvider)getApplication();
                dataProvider.init();
                dataProvider.printSchedules();
        }


        private void initializeUIElements() {
                btnLogIn = (Button) findViewById(R.id.btnLogIn);
                tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
                tvSignUp = (TextView) findViewById(R.id.tvSignUp);
                tvSkipLogin = (TextView) findViewById(R.id.tvSkipLogin);
                etUsername = (EditText) findViewById(R.id.etUsername);
                etPassword = (EditText) findViewById(R.id.etPassword);
                progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        }


        private void hideActionBar() {
                android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                actionBar.hide();
        }


        public void clicked(View view) {
                Log.d(LOG_TAG, "Button clicked!");

                switch (view.getId()) {
                    case R.id.btnLogIn:
                        Log.d(LOG_TAG, "Button: " + btnLogIn.getText());
                        login();
                        break;

                    case R.id.tvForgotPassword:
                        Log.d(LOG_TAG, "Button: " + tvForgotPassword.getText());
                        break;

                    case R.id.tvSignUp:
                        Log.d(LOG_TAG, "Button: " + tvSignUp.getText());
                        goToSignupActivity();
                        break;

                    case R.id.tvSkipLogin:
                        Log.d(LOG_TAG, "Button: " + tvSkipLogin.getText());
                        break;
                }
        }


        private void login() {
                progressBarLogin.setVisibility(View.VISIBLE);
                getUserEnteredValues();
                if (isEnteredInputValid()) {
                    loginToFirebase();
                }
                else {
                    progressBarLogin.setVisibility(View.GONE);
                }
        }


        private boolean isEnteredInputValid() {
                if (CommonFunctionalities.isFieldEmpty(enteredUsername)) {
                    CommonFunctionalities.displayLongToast("Username (Email) field is required!", getApplicationContext());
                    return false;
                }

                if (CommonFunctionalities.isFieldEmpty(enteredPassword)) {
                    CommonFunctionalities.displayLongToast("Password field is required!", getApplicationContext());
                    return false;
                }

                return true;
        }


        private void loginToFirebase() {
                firebaseAuth.signInWithEmailAndPassword(enteredUsername, enteredPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBarLogin.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(LOG_TAG, "signInWithEmail: Success!");
                                            currentUser = firebaseAuth.getCurrentUser();
                                            displaySchedulesIfUserAccountIsActivated();
                                    }
                                    else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(LOG_TAG, "signInWithEmail: Failure!", task.getException());
                                            CommonFunctionalities.displayLongToast("Authentication failed!", getApplicationContext());
                                    }
                            }
                    });
        }


        private void displaySchedulesIfUserAccountIsActivated() {
                String userId = currentUser.getUid();

                firebaseDBRef.child("Users").child(userId).child("AccountActivated").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                                boolean accountActivated = Boolean.parseBoolean(snapshot.getValue().toString());

                                if (accountActivated) {
                                        goToSchedulesActivity();
                                }
                                else {
                                        CommonFunctionalities.displayLongToast("Your user account is not activated yet!", getApplicationContext());
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                });
        }


        private void getUserEnteredValues() {
                enteredUsername = etUsername.getText().toString();
                enteredPassword = etPassword.getText().toString();
        }


        private void goToSignupActivity() {
                Intent intent = new Intent(this, SignupActivity.class);
                this.startActivity(intent);
        }


        private void goToSchedulesActivity() {
                Intent intent = new Intent(this, SchedulesActivity.class);
                this.startActivity(intent);
        }

}
