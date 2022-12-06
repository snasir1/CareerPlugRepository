package com.mycareerplug.iamokah.mycareerplug;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class phonecreate extends AppCompatActivity {
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final String TAG = "CREATEACTIVITY";
    public static final String PREFS_NAME = "preferences";
    public static final String PREF_VERID = "VerificationID";
    public static final String PREF_CODE = "SMScode";

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    EditText phoneNum;
    EditText enterCode;
    Button createAcc;
    String phonenumberstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createphone_acc);

        phoneNum = findViewById(R.id.enter_phone);
        enterCode = findViewById(R.id.verify_codez);
        createAcc = findViewById(R.id.createbtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(phonecreate.this, MainActivity.class));
                }
            }
        };

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumberstr = "+1" + phoneNum.getText().toString();
                System.out.println("PHONE #" + phoneNum);
                System.out.println("PHONE #" + phoneNum);
                System.out.println("PHONE #" + phoneNum);
              //  startPhoneNumberVerification(phonestr);


            }
        });

    }
}
