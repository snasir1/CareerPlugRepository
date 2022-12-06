package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_AUTHENTICATED;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_EMAIL;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_NAME;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_PHONE;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;

public class phoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "phoneAuthActivity";

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    Button sendCodeBtn, verifyCodeBtn;
    EditText phoneNum, mCode;

    //Phone Auth Variables
    boolean mVerificationInProgress = false;
    boolean insideCodeSent = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    Boolean startAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        getSupportActionBar().setTitle("Authentication");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        sendCodeBtn = findViewById(R.id.button2);
        verifyCodeBtn = findViewById(R.id.button3);
        phoneNum = findViewById(R.id.phone_num);
        mCode = findViewById(R.id.code);

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
                Log.d(TAG, "onVerificationCompleted:" + credential.getProvider());
                Log.d(TAG, "onVerificationCompleted:" + credential.getSmsCode());
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //      updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                if (credential != null) {
                    if (credential.getSmsCode() != null) {
                       //  mCode.setText(credential.getSmsCode());
                    } else {
                        //  mVerificationField.setText(R.string.instant_validation);
                        System.out.println("INSIDE VERIFICATION COMPLETE WITH NULL SMSCODE");
                    }
                }

             /*   SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString(PREF_VERID, mVerificationId);
                editor.putString(PREF_CODE, credential.getSmsCode());
                editor.commit(); */
               // updateIsAuthenticated();
                linkEmailToPhone(credential);
                System.out.println("inside verification complete next to authlistener");
              //  mAuth.addAuthStateListener(mAuthListener);
              //    signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]

                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(phoneAuthActivity.this, "Invalid phone number format. Please try again.", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(phoneAuthActivity.this, "Quota Exceeded for this device. " +
                            "Please try again later!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(phoneAuthActivity.this, "Verification code sent.", Toast.LENGTH_LONG).show();
                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);
                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);

                System.out.println("CODE HAS BEEN SENT. Verificationid is " + verificationId);

              //  verifyEmail.setVisibility(View.VISIBLE);
          //      insideCodeSent = true;

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                System.out.println("SAVING VERIFCIATIONID " + mVerificationId);
                mResendToken = token;


                // [START_EXCLUDE]
                // Update UI
                changeUI();
                //      updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]




        autoFillPhone();
        clickSendCodeBtn();
        clickVerifyCodeBtn();
    }

    public void autoFillPhone() {
        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        String phone = settings.getString(PREF_SIGNUP_USER_PHONE, "");
        phoneNum.setText(PhoneNumberUtils.formatNumber(phone));

    }

    public void clickSendCodeBtn() {

        sendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println("Printing phoneNum" + PhoneNumberUtils.stripSeparators(phoneNum.getText().toString()));
                String phonestr = "+1" + PhoneNumberUtils.stripSeparators(phoneNum.getText().toString());
                System.out.println("Printing PHONESTR: " + phonestr);
                System.out.println("Printing PHONESTR: " + phonestr);
                System.out.println("Printing PHONESTR: " + phonestr);
                startPhoneNumberVerification(phonestr);
            }
        });
    }

    public void changeUI() {
        phoneNum.setVisibility(View.INVISIBLE);
        sendCodeBtn.setVisibility(View.INVISIBLE);
        sendCodeBtn.setEnabled(false);

        mCode.setVisibility(View.VISIBLE);
        verifyCodeBtn.setVisibility(View.VISIBLE);

    }

    public void updateIsAuthenticated() {
        System.out.println("SIGN IN SUCCESSFUL INSIDE UPDATEISAUTHENTICATED");
        SharedPreferences storeuserinfo = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
        editor.putBoolean(PREF_SIGNUP_USER_AUTHENTICATED, true);


        editor.commit();
        startAuthenticated = storeuserinfo.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);
        System.out.println("SIGN IN SUCCESSFUL INSIDE UPDATEISAUTHENTICATED VALUE OF " + startAuthenticated);
    }

    public void clickVerifyCodeBtn() {
        verifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mycode = mCode.getText().toString();
                System.out.println("Printing VERIFICATIONID: " + mVerificationId);
                System.out.println("Printing CODE: " + mycode);
                verifyPhoneNumberWithCode(mVerificationId, mycode);
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                phoneAuthActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(phoneAuthActivity.this, "Code cannot be empty",
                    Toast.LENGTH_SHORT).show();
            System.out.println("CODE CANNOT BE EMPTY");
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            linkEmailToPhone(credential);
        }



        // [END verify_with_code]

     //   signInWithPhoneAuthCredential(credential);
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateIsAuthenticated();

                            linkEmailToPhone(getEmailAccountCredential());
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

    public AuthCredential getEmailAccountCredential() {
        String email, password;
        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
       email = settings.getString(PREF_SIGNUP_USER_EMAIL, "");
       password = "password";
       Log.d(TAG, "INSIDE emailcredential: email is: " + email);

       AuthCredential credential = EmailAuthProvider.getCredential(email, password);

       return credential;
    }

    public void linkEmailToPhone(AuthCredential credential) {
        if (mAuth != null) {
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateIsAuthenticated();
                                // mAuth.addAuthStateListener(mAuthListener);
                                saveUserInfo();
                                sendToCreateProfile();
                                Log.d(TAG, "linkWithCredential:success. PHONE AND EMAIL LINKED");
                                FirebaseUser user = task.getResult().getUser();
                                Log.d(TAG, "Printing user " + user.toString());
                            } else {

                                Log.w(TAG, "linkWithCredential:failure", task.getException());
                                Toast.makeText(phoneAuthActivity.this, "Link failed" + task.getException(),
                                        Toast.LENGTH_LONG).show();
                                //  updateUI(null);
                                Exception e = task.getException();
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(phoneAuthActivity.this, "Invalid Code. Please re-enter and try again.",
                                            Toast.LENGTH_LONG).show();
                                } else if (e instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(phoneAuthActivity.this, "This phone number is already associated with a different account.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            // ...
                        }
                    });
        }

    }

    private void saveUserInfo() {
        String name, email, phone;
        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        name = settings.getString(PREF_SIGNUP_USER_NAME, "");
        email = settings.getString(PREF_SIGNUP_USER_EMAIL, "");
        phone = settings.getString(PREF_SIGNUP_USER_PHONE, "");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        if (mAuth != null && mAuth.getUid() != null) {
            mRef.child("user").child("user_data").child(mAuth.getUid()).child("data").setValue(
                    new User(name, email, phone), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                System.out.println("Data saved successfully.");
                            }
                        }
                    });
        }
    }

    public void sendToCreateProfile() {
        System.out.println("INSIDE SENDTOCREATEPROFILE");
        startActivity(new Intent(phoneAuthActivity.this, CreateUserProfile.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        startAuthenticated = settings.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);
        System.out.println("INSIDE PHONEAUTH START: CURRENT VALUE OF isAuthenticated is" + startAuthenticated);
      /*  mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                System.out.println("INSIDE onAUTHSTATECHANGED CURRENT VALUE OF isAuthenticated is" + startAuthenticated);
                if ((firebaseAuth.getCurrentUser() != null) && (startAuthenticated == true)) {
                    startActivity(new Intent(phoneAuthActivity.this, CreateProfile.class));
                    System.out.println("STARTING NEW ACTIVITY value is" + startAuthenticated);
                }
            }
        };*/
    }

    @Override
    public void onBackPressed() {
        System.out.println("Inside backpressed");
        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        String email = settings.getString(PREF_SIGNUP_USER_EMAIL, "");

        AuthCredential credential = EmailAuthProvider.getCredential(email, "password");
        if (mAuth.getCurrentUser() != null) {
            System.out.println("About to delete currentuser in PhoneAuth backpress" + mAuth.getCurrentUser().getEmail().toString());

            mAuth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("reauthentication successful");
                    mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println("DELETE USER SUCCESSFUL");
                                System.out.println("DELETE USER SUCCESSFUL");
                                System.out.println("DELETE USER SUCCESSFUL");
                                System.out.println("DELETE USER SUCCESSFUL");
                            } else {
                                System.out.println("CANNOT DELETE USER" + task.getException());
                                System.out.println("CANNOT DELETE USER" + task.getException());
                                System.out.println("CANNOT DELETE USER" + task.getException());
                                System.out.println("CANNOT DELETE USER" + task.getException());

                            }
                        }
                    });
                }
            });
            mAuth.signOut();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
