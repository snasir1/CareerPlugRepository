package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final String TAG = "SignUpActivity";
    public static final String PREFS_NAME = "preferences";
    public static final String PREF_VERID = "VerificationID";
    public static final String PREF_CODE = "SMScode";

    //CURRENTLY USING
    public static final String SIGNUP_PREFNAME = "signup_pref";
    public static final String PREF_SIGNUP_USER_NAME = "name";
    public static final String PREF_SIGNUP_USER_EMAIL = "email";
    public static final String PREF_SIGNUP_USER_PHONE = "phone";
    public static final String PREF_SIGNUP_USER_AUTHENTICATED = "phone_authenticated";



    Button logOutBtn;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    String emailLink;



    String phonestr;
    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    TextInputEditText user_name, user_email, user_phone;
    Boolean isName, isEmail, isPhone;
    TextView invalid_name, invalid_email, invalid_phone, sign_in_text_btn;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                                                "[a-zA-Z0-9_+&*-]+)*@" +
                                                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                                "A-Z]{2,7}$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setTitle("Sign Up");


        user_name = findViewById(R.id.signup_name);
        user_email = findViewById(R.id.signup_email);
        user_phone = findViewById(R.id.signup_phone);
        user_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        invalid_name = findViewById(R.id.signup_name_invalid);
        invalid_email = findViewById(R.id.signup_email_invalid);
        invalid_phone = findViewById(R.id.signup_phone_invalid);

        logOutBtn = findViewById(R.id.log_out_btn);
        sign_in_text_btn = findViewById(R.id.createacc_sign_in_text);
        sign_in_text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
       // logOutBtn.setVisibility(View.INVISIBLE);
        logOutBtn.setEnabled(false);
        enableSignButton(user_phone);

    //    userEmailtv.setText(mAuth.getCurrentUser().getEmail());

        Intent mintent = getIntent();
        String intentMsg = mintent.getStringExtra("phone");
        System.out.println("PRINTING INTENT " + intentMsg);
        System.out.println("PRINTING INTENT " + intentMsg);
        System.out.println("PRINTING INTENT " + intentMsg);
//        if (intentMsg.equals("phone")) {
          //  Toast.makeText(SignupActivity.this,"Clicked Phone Button", Toast.LENGTH_LONG);

  //      }

        mAuth = FirebaseAuth.getInstance();


        System.out.println("FIREBASE USER IS " + mAuth.getCurrentUser());
        user = mAuth.getCurrentUser();



        checkIntent(getIntent());




     /*   mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }
            }
        }; */
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CLICKING CREATEACCOUNT:" + user_name.getText().toString());
                if (verifyInput()) {
                    String email, password;
                    email = user_email.getText().toString();
                    password = "password";
                    createEmailAccount(email, password);
             //       saveUserInfo();
          //          startActivity(new Intent(getApplicationContext(), phoneAuthActivity.class));
            //        finish();
                } else {
                    Log.d(TAG, "ELSE STATEMENT INCORRET INFO");
                }

             //   signInWithEmailLink(emailVer, emailLink);

               // mAuth.signOut();
            }
        });



        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
          //      updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]

                if (credential != null) {
                    if (credential.getSmsCode() != null) {
                        //phoneVerifyField.setText(credential.getSmsCode());
                        System.out.println("INSIDE VERIFICATION COMPLETE WITH SMSCODE" + credential.getSmsCode().toString());
                    } else {
                      //  mVerificationField.setText(R.string.instant_validation);
                        System.out.println("INSIDE VERIFICATION COMPLETE WITH NULL SMSCODE");
                    }
                }

                SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString(PREF_VERID, mVerificationId);
                editor.putString(PREF_CODE, credential.getSmsCode());
                editor.commit();


              //  signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                Toast.makeText(SignupActivity.this, "Invalid phone number format. Please try again.", Toast.LENGTH_LONG).show();
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
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
                Toast.makeText(SignupActivity.this, "Verification code sent.", Toast.LENGTH_LONG).show();
                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);
                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);

                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);





                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                mResendToken = token;


                // [START_EXCLUDE]
                // Update UI
          //      updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]



    }

    public void enableSignButton(EditText mphone) {

        System.out.println("inside enableSign  name: " + user_name.getText().toString());
        System.out.println("inside enableSign  email: " + user_email.getText().toString());
        System.out.println("inside enableSign  phone: " + user_phone.getText().toString());

        mphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("inside before");
                if (!TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(user_email.getText().toString())
                        &&  !TextUtils.isEmpty(user_phone.getText().toString())) {
                    logOutBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("inside during");
                if (!TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(user_email.getText().toString())
                        &&  !TextUtils.isEmpty(user_phone.getText().toString())) {
                    logOutBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("inside after");
                if (!TextUtils.isEmpty(user_name.getText().toString()) && !TextUtils.isEmpty(user_email.getText().toString())
                        &&  !TextUtils.isEmpty(user_phone.getText().toString())) {
                    logOutBtn.setEnabled(true);
                }
            }
        });


    }

    private boolean verifyInput() {
        String name = user_name.getText().toString();
        String email = user_email.getText().toString();
        String phone = user_phone.getText().toString();
        Boolean isCorrectInput = true;
        if (TextUtils.isEmpty(name)) {
            invalid_name.setVisibility(View.VISIBLE);
            isCorrectInput = false;
        } else {
            invalid_name.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            invalid_email.setVisibility(View.VISIBLE);
            isCorrectInput = false;
        } else {
            invalid_email.setVisibility(View.INVISIBLE);
        }
        if (TextUtils.isEmpty(phone) || !isValidPhoneNumber(phone)) {
            System.out.println("inside ifstatement" + phone.toString());
            invalid_phone.setVisibility(View.VISIBLE);
            isCorrectInput = false;
        } else {
            System.out.println("inside ifstatement" + (TextUtils.isEmpty(phone) || !isValidPhoneNumber(phone)));
            invalid_phone.setText("Please only enter phone digits. The number will be autoformatted.");
            invalid_phone.setTextColor(getResources().getColor(R.color.defaulttextviewcolor));
           // invalid_phone.setVisibility(View.INVISIBLE);
        }
        return isCorrectInput;
    }

    public boolean isEmailValid(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhoneNumber(String phone) {
        int numDigits = 0;
        String phoneOnly = PhoneNumberUtils.stripSeparators(phone);
        for (char ch : phoneOnly.toCharArray()){
            System.out.println("Inside isvalidphoneNumber printing ch: " + ch);
            if (Character.isDigit(ch)) {
                numDigits++;
            }
        }
        if (numDigits == 10) {
            return true;
        } else {
            invalid_phone.setText("Phone number must have 10 digits.");
            invalid_phone.setTextColor(getResources().getColor(R.color.red));
            return false;
        }
    }

    public void createEmailAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    saveUserInfo();
                    startActivity(new Intent(SignupActivity.this, phoneAuthActivity.class));
                    finish();
                } else {
                    //Toast.makeText(SignupActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    //Email is already in use.
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(SignupActivity.this, "Can't create account. Email already exists.", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    public void saveUserInfo() {
        SharedPreferences storeuserinfo = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
        boolean test = true;
        editor.putString(PREF_SIGNUP_USER_NAME, user_name.getText().toString());
        Log.d(TAG, "user" + user_name.getText().toString());
        editor.putString(PREF_SIGNUP_USER_EMAIL, user_email.getText().toString());
        Log.d(TAG, "email" + user_email.getText().toString());
        editor.putString(PREF_SIGNUP_USER_PHONE, PhoneNumberUtils.stripSeparators(user_phone.getText().toString()));
        Log.d(TAG, "phone" + PhoneNumberUtils.stripSeparators(user_phone.getText().toString()));

        editor.putBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);

        editor.commit();

        test = storeuserinfo.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, true);

        System.out.println("INSIDE SAVEUSERINFO and value of test is " + test);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                SignupActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // [END verify_with_code]

        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            System.out.println("SIGN IN SUCCESSFUL");
                         //   FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                        //    updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
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




/*    private boolean validatePhoneNumber() {
        String phoneNumber = phonetxt.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            phonetxt.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
*/

    public void sendSignInLink(String email) {
        // [START auth_send_sign_in_link]

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        .setAndroidPackageName(
                                getPackageName(),
                                false, /* installIfNotAvailable */
                                null   /* minimumVersion */)
                        .setHandleCodeInApp(true)
                        .setUrl("https://mycareerplug.firebaseapp.com/__/auth/action?mode=<action>&oobCode=<code>")
                        .build();


        mAuth.sendSignInLinkToEmail(email, actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Email Sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Exception e = task.getException();
                            Log.w(TAG, "Could not send link", e);
                            Toast.makeText(SignupActivity.this, "Failed to send email", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END auth_send_sign_in_link]
    }

    public void signInWithEmailLink(String email, String link) {
        // [START auth_verify_sign_in_link]

        mAuth.signInWithEmailLink(email, link)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                 //       hideProgressDialog();
               //         mPendingEmail = null;

                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmailLink:success");
                            Toast.makeText(SignupActivity.this, "Sign in Successful", Toast.LENGTH_LONG).show();
                   //         mEmailField.setText(null);
                  //          updateUI(task.getResult().getUser());
                        } else {
                            Log.w(TAG, "signInWithEmailLink:failure", task.getException());
                         //   updateUI(null);

                            if (task.getException() instanceof FirebaseAuthActionCodeException) {
                            //    showSnackbar("Invalid or expired sign-in link.");
                                System.out.println("Invalid or expired sign-in link.");
                                System.out.println("Invalid or expired sign-in link.");
                                System.out.println("Invalid or expired sign-in link.");
                            }
                        }
                    }
                });


        // [END auth_verify_sign_in_link]
    }




    @Override
    protected void onStart() {
        super.onStart();
       // phonestr = "+1" + phonetxt.getText().toString();
        // [START_EXCLUDE]
 /*       if (mVerificationInProgress && validatePhoneNumber()) {
            System.out.println("MVERIFICATIONPROGRESS AND VALIDATEPHONENUMBER IS TRUE");
            System.out.println("MVERIFICATIONPROGRESS AND VALIDATEPHONENUMBER IS TRUE");
            System.out.println("MVERIFICATIONPROGRESS AND VALIDATEPHONENUMBER IS TRUE");
            startPhoneNumberVerification(phonestr);
        }*/
        // [END_EXCLUDE]

     //   mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }

    private void checkIntent(@Nullable Intent intent) {
        if (intentHasEmailLink(intent)) {
            emailLink = intent.getData().toString();

         //   mStatusText.setText(R.string.status_link_found);
         //   mSendLinkButton.setEnabled(false);
         //   mSignInButton.setEnabled(true);
        } else {
            System.out.println("CHECKINTENT: FAILED");
            System.out.println(emailLink);
          //  mStatusText.setText(R.string.status_email_not_sent);
            //mSendLinkButton.setEnabled(true);
           // mSignInButton.setEnabled(false);
        }
    }


    private boolean intentHasEmailLink(@Nullable Intent intent) {
        if (intent != null && intent.getData() != null) {
            String intentData = intent.getData().toString();
            System.out.println("PRINTING WHAT INTENTDATA IS" + intentData);
            if (mAuth.isSignInWithEmailLink(intentData)) {
                return true;
            }
        }
        System.out.println("INTENTHASEMAILLINK: FAILED");

        return false;
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

}
