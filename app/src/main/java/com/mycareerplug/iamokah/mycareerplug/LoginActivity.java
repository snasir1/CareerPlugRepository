package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shobhitpuri.custombuttons.GoogleSignInButton;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREFS_NAME;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_CODE;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_AUTHENTICATED;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_PHONE;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_VERID;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    EditText login_email;
   // EditText login_password;
    Button loginBtn;
    Button createAccPhone;
    Button createmailbtn;
    GoogleSignInButton myGoogleBtn;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationID, code;

    private TextView dbtext, createAccText;
    private Button dbButton;
    private DatabaseReference mRef;
    private ValueEventListener db_read;

    private boolean isCodeVerifyBtn = false;
    String mVerificationId;
    String globalPhone = "";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setTitle("Log In");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        login_email = findViewById(R.id.login_email);

        boolean loggedIn = false;
        if (mAuth.getCurrentUser() != null) {
            loggedIn = true;
            Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().toString(), Toast.LENGTH_LONG).show();
        }
        System.out.println("LOGINPAGE: CURRENTLY LOGGED IN = " + loggedIn);
        System.out.println("LOGINPAGE: CURRENTLY LOGGED IN = " + loggedIn);
        System.out.println("LOGINPAGE: CURRENTLY LOGGED IN = " + loggedIn);
        System.out.println("LOGINPAGE: CURRENTLY LOGGED IN = " + loggedIn);


      //  login_password = findViewById(R.id.login_pw);
      //  login_password.setVisibility(View.INVISIBLE);
        createmailbtn = findViewById(R.id.createacc_emailbtn);
        loginBtn = findViewById(R.id.login_loginbtn);
        dbButton = findViewById(R.id.databaseid);
        dbtext = findViewById(R.id.testdb);
        createAccText = findViewById(R.id.create_acc_textid);
        createAccText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRef = FirebaseDatabase.getInstance().getReference();

                for(int k=0; k < 415; k++) {

                    mRef.child("user").child("event_list").child(Integer.toString(k)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> test = (Map) dataSnapshot.getValue();
                            Log.v(TAG, "DATA: " + test.toString());
                            Log.v(TAG, "test size" + test.size());
                            String vals = "";
                            for(int i=0; i <test.size(); i++) {

                                vals = test.keySet().toArray()[i] + " = " + test.values().toArray()[i] + "\n";
                                Log.v(TAG, "vals DATA: " + vals);
                            }
                           // List<String> test = (List)dataSnapshot.getValue();
                        //    Log.v(TAG, "DATA: " + test);
                            /*
                            String vals = "";
                            for(int i=0; i <test.size(); i++) {
                                vals += test.get(i) + "\n";
                            }
                            Log.v(TAG, "AFTERLOOP: " + vals);
                            dbtext.setMovementMethod(new ScrollingMovementMethod());
                            dbtext.setText(vals); */
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        createAccPhone = findViewById(R.id.createacc_phonebtn);

        //setGooglePlusButtonText(myGoogleBtn, "Sign in with Google");

    /*    mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        }; */




        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                System.out.println("inside onverification complete NUm");
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
            //    mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //      updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
               
                if (credential != null) {
                    if (credential.getSmsCode() != null) {
                    //    phoneVerifyField.setText(credential.getSmsCode());

                    } else {
                        //  mVerificationField.setText(R.string.instant_validation);
                        System.out.println("INSIDE VERIFICATION COMPLETE WITH NULL SMSCODE");
                    }
                }



                  signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                System.out.println("inside onverificationfailed NUm");
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]

             //   mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(LoginActivity.this, "Invalid phone number format. Please try again.", Toast.LENGTH_LONG).show();
                    System.out.println("INVALID PHONE NUMBER");
                    System.out.println("INVALID PHONE NUMBER");
                    System.out.println("INVALID PHONE NUMBER");
                    System.out.println("INVALID PHONE NUMBER");
                    // mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    System.out.println("PHONE QUOTA EXCEEDED");
                    System.out.println("PHONE QUOTA EXCEEDED");
                    System.out.println("PHONE QUOTA EXCEEDED");
                    System.out.println("PHONE QUOTA EXCEEDED");
                    // Toast.make(findViewById(android.R.id.content), "Quota exceeded.",
                    //         Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(LoginActivity.this, "Incorrect Phone number.", Toast.LENGTH_LONG).show();
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //   updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
               // Toast.makeText(LoginActivity.this, "Verification code sent.", Toast.LENGTH_LONG).show();
                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);

                //  verifyEmail.setVisibility(View.VISIBLE);
                //      insideCodeSent = true;

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
            //    mResendToken = token;
                progressBar.setVisibility(View.VISIBLE);
                loginBtn.setText("Logging in...");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        Toast.makeText(LoginActivity.this, "SMSCode sent", Toast.LENGTH_SHORT).show();
                        login_email.setText("");
                        login_email.setHint("Enter Code");
                        login_email.setInputType(InputType.TYPE_CLASS_NUMBER);
                        loginBtn.setText("Verify code");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 1500);   //5 seconds

                isCodeVerifyBtn = true;
                // [START_EXCLUDE]
                // Update UI
              //  changeUI();
                //      updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                System.out.println("INSIDE AUTORETRIVAL TIMEOUT");

            }
        };
        // [END phone_auth_callbacks]



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LOGINBTN text is " + loginBtn.getText().toString());
                if (isCodeVerifyBtn == true) {
                    System.out.println("INSIDE verify code btn" + loginBtn.getText().toString());
                    verifyPhoneNumberWithCode(mVerificationId, login_email.getText().toString());
                } else {
                    String login_email_str, login_pw_str;
                    login_email_str = login_email.getText().toString();
                    login_pw_str = "password";

                    attemptLogin(login_email_str, login_pw_str);
                }



/*
                if ((login_email_str.matches("[0-9]+") && login_email_str.length() > 9 && !TextUtils.isEmpty(login_email_str)) || login_email_str.equals("test@gmail.com")) {
                    //Means user is entering phone number.
                    login_email_str = "+1" + login_email_str;
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                            Context.MODE_PRIVATE);
                    verificationID = settings.getString(PREF_VERID, "");
                    code = settings.getString(PREF_CODE, "");
                    System.out.println("VERID " + verificationID);
                    System.out.println("code " + code);
                    System.out.println("VERID " + verificationID);
                    System.out.println("code " + code);
                //    PhoneAuthCredential cred = PhoneAuthProvider.getCredential(verificationID, null);
                   // startPhoneNumberVerification(login_email_str);

                    startPhoneNumberVerification(login_email_str);
                    String memail, mpassword;
                    memail = "test@gmail.com";
                    mpassword = "password";
                    mAuth.signInWithEmailAndPassword(memail, mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Information" , Toast.LENGTH_SHORT).show();
                }  */

             /*   if(!TextUtils.isEmpty(login_email_str) && !TextUtils.isEmpty(login_pw_str)) {

                    mAuth.signInWithEmailAndPassword(login_email_str, login_pw_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } */
            }
        });


        // Configure Google Sign In
        System.out.println("PRINTING STRING DEFAULTWEBCLIENTID " + getString(R.string.default_web_client_id));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

/*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Got Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build(); */

        myGoogleBtn = findViewById(R.id.googleBtn);
        myGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });




        createAccPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intenttype = "phone";
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("phone", intenttype);
                startActivity(intent);
            }
        });


        createAccPhone.setVisibility(View.INVISIBLE);
        createmailbtn.setVisibility(View.INVISIBLE);
        //dbButton.setVisibility(View.INVISIBLE);
    }

    public boolean isValidPhoneNumber(String phone) {
        int numDigits = 0;
        for (char ch : phone.toCharArray()){
            System.out.println("Inside isvalidphoneNumber printing ch: " + ch);
            if (Character.isDigit(ch)) {
                numDigits++;
            }
        }
        System.out.println("Inside isvalidphoneNumber numDigits is: " + numDigits);
        if (numDigits == 10) {
            return true;
        } else {
            return false;
        }
    }



    public void attemptLogin(String login_email_str, String password) {
        if (!TextUtils.isEmpty(login_email_str)) {
            //USER IS ENTERING THROUGH PHONE NUMBER
            if (isValidPhoneNumber(login_email_str)) {
                //making sure this phone number matches the one that was authorized and inputted by user.
               /* if(isCorrectPhoneNum(login_email_str)) {
                    System.out.println("INSIDE isCorrect if statement: value of login_phone is: " + login_email_str);
                    login_email_str = "+1" + login_email_str;
                    System.out.println("inside isValidPhone NUm");
                    startPhoneNumberVerification(login_email_str);
                }*/
                globalPhone = login_email_str;
                login_email_str = "+1" + login_email_str;
                System.out.println("inside isValidPhone NUm");
                startPhoneNumberVerification(login_email_str);

            } else {
                loginBtn.setText("Logging in...");
                progressBar.setVisibility(View.VISIBLE);
                //USER IS ENTERING THROUGH EMAIL.
                mAuth.signInWithEmailAndPassword(login_email_str, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateIsAuthenticated();
                            sendToMain();
                            Log.d(TAG, "EMAIL LOGIN:success");
                        } else {
                            loginBtn.setText("Login");
                            progressBar.setVisibility(View.INVISIBLE);
                            String errorMessage = task.getException().getMessage();
                            Log.d(TAG, "EMAIL LOGIN:failed exception " + errorMessage);
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(LoginActivity.this, "Field cannot be empty" , Toast.LENGTH_SHORT).show();
        }
    }

    public void updateIsAuthenticated() {
        System.out.println("SIGN IN SUCCESSFUL INSIDE UPDATEISAUTHENTICATED");
        SharedPreferences storeuserinfo = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
        editor.putBoolean(PREF_SIGNUP_USER_AUTHENTICATED, true);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //mAuth.addAuthStateListener(mAuthListener);
    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        System.out.println("inside startphonenumverificaiton");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                LoginActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

       // mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        System.out.println("LOGINACTIVITY INSIDE VERIFYPHONENUMBERWITHCODE");
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(LoginActivity.this, "Code cannot be empty",
                    Toast.LENGTH_SHORT).show();
            System.out.println("CODE CANNOT BE EMPTY");
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
          //  linkEmailToPhone(credential);
            signInWithPhoneAuthCredential(credential);
        }

        // [END verify_with_code]


    }


    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        loginBtn.setText("Logging in...");
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithPHONECredential:success");
                            System.out.println("SIGN IN SUCCESSFUL");

                           /* if (isCorrectPhoneNum(task.getResult().getUser().getProviderId())) {
                                //set isAuthenticated to true.
                                updateIsAuthenticated();
                                sendToMain();
                            }*/

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("user").child("user_data").child(mAuth.getUid()).orderByChild("phone").equalTo(globalPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     Log.v("LOGINACTIVITY", "datasnapshot " + dataSnapshot);
                                    if (dataSnapshot.exists()) {
                                        Log.v("LOGINACTIVITY", "datasnapshot " + dataSnapshot);
                                        //set isAuthenticated to true.
                                        updateIsAuthenticated();
                                        sendToMain();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid phone number.", Toast.LENGTH_LONG).show();
                                        isCodeVerifyBtn = false;
                                        globalPhone = "";
                                        login_email.setText("");
                                        login_email.setHint("Email or Phone");
                                        login_email.setInputType(InputType.TYPE_CLASS_TEXT);
                                        loginBtn.setText("Login");
                                        progressBar.setVisibility(View.INVISIBLE);

                                        //delete phone account.
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        System.out.println("Phone account successfully deleted");
                                                    } else {
                                                        System.out.println(task.getException());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //   FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            //    updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(LoginActivity.this, "Incorrect SMS code.", Toast.LENGTH_LONG).show();
                            loginBtn.setText("Verify code");
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "signInWithPHONECredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                System.out.println("INVALID CODE");
                                System.out.println("INVALID CODE");
                                System.out.println("INVALID CODE");
                                System.out.println("INVALID CODE");
                                //        mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //     updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Successfully SIGNEDOUT");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
          //  GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
          //  handleSignInResult(result);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed. Error message displayed INSIDE onActivityResult", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "HandleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            loginBtn.setText("Logging in...");
            progressBar.setVisibility(View.VISIBLE);
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);

        //    statusTextView.setText("Welcome, Name:" + acct.getDisplayName());
        } else {

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        loginBtn.setText("Logging in...");
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateIsAuthenticated();
                            sendToMain();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //  updateUI(user);
                        } else {
                            loginBtn.setText("Login");
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "firebase Auth Error", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }



}
