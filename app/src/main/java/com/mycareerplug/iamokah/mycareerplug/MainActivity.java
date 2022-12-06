package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_AUTHENTICATED;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "Main_Activity";

    private WebView myWebView;
    private TextView homeText;
    private Button goal_btn, interest_btn, skill_btn;
    private DatabaseReference mRef;

    private SignInButton myGoogleBtn;
    Button signOutBtn;
    TextView statusTextView;
    GoogleApiClient mGoogleApiClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    //    myWebView = findViewById(R.id.webView);
        signOutBtn = findViewById(R.id.button);

    /*    WebSettings myWebSettings = myWebView.getSettings();
        myWebSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://www.mycareerplug.com");
        myWebView.setWebViewClient(new WebViewClient()); */

        homeText = findViewById(R.id.homepage_text);
        homeText.setMovementMethod(new ScrollingMovementMethod());
        goal_btn = findViewById(R.id.home_btn);
        interest_btn = findViewById(R.id.home_btn2);
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef = FirebaseDatabase.getInstance().getReference();
                homeText.setText("");
                DatabaseReference c_goal = mRef.child("user").child("career_goals").child("goals");
               c_goal.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       List<String> test = (List)dataSnapshot.getValue();

                       Log.v(TAG, "HOMEPAGE DATA: " + test.get(0));
                       String vals = "";
                       for(int i=0; i <test.size(); i++) {
                           vals += test.get(i) + "\n";
                       }
                       Log.v(TAG, "HOMEPAGE LOOP: " + vals);


                       homeText.setText(vals);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
            }
        });

        interest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef = FirebaseDatabase.getInstance().getReference();
                homeText.setText("");
                for(int k=0; k < 415; k++) {

                  //  homeText.append(record_num);
                    DatabaseReference interests = mRef.child("user").child("event_list").child(Integer.toString(k));
                    interests.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, String> test = (Map) dataSnapshot.getValue();
                            //    List<String> test = (List)dataSnapshot.getValue();
                            //      Map<String, String> testmap = (Map)test.get(0);
                            //       Log.v(TAG, "HOMEPAGE DATA: " + test.get(0));
                        String vals = "";
                        for(int i=0; i <test.size(); i++) {
                            if (i==0) {
                                vals += "\n\n" + "-----------NEW RECORD-------------" + "\n\n";
                            }
                            vals += test.keySet().toArray()[i] + " = " + test.values().toArray()[i] + "\n";
                        }
                        Log.v(TAG, "HOMEPAGE LOOP: " + vals);
                       // homeText.setText("");

                        homeText.append(vals);
                            Log.v(TAG, "HOMEPAGE DATA: " + dataSnapshot.getValue());
                            Log.v(TAG, "First key: " + test.keySet().toArray()[0]);
                            Log.v(TAG, "First value: " + test.values().toArray()[0]);
                           // Log.v(TAG, "Size: " + test.keySet().size());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                   // String record_num = "RECORD # " + k + "\n\n";
                  //  homeText.append(record_num);
                }
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Got Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAuth.getInstance().signOut();
                signOut();
                sendToLoginScreen();
//                Toast.makeText(MainActivity.this, "Logged in as " + mAuth.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //startActivity(intent);
                //finish();
            }
        });

     /*   mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginUIActivity.class));
                    finish();
                }
            }
        }; */


        /*
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //If user is not null that means we have signed in.
                if (firebaseAuth.getCurrentUser() != null) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    System.out.println("Printing useremail:" + user.getEmail() + " and displayname: " + user.getDisplayName());

                    startActivity(new Intent(MainActivity.this, LoggedInActivity.class));
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Got Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //INITIALIZE ALL VIEW IDS.
        myGoogleBtn = findViewById(R.id.googleBtn);
        statusTextView = findViewById(R.id.textView2);
        myGoogleBtn = findViewById(R.id.googleBtn);
        myGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signOutBtn = findViewById(R.id.btn1);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

     //   mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //initialize authentication.
    //    mAuth = FirebaseAuth.getInstance();
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


      //  mAuth.addAuthStateListener(mAuthlistener);

     /*   SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        Boolean isAuthenticated = settings.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);
        Log.d(TAG, "IN MAINACTIVITY isAuthenticated is: " + isAuthenticated.toString());
        Log.d(TAG, "IN MAINACTIVITY isAuthenticated is: " + isAuthenticated.toString());
        Log.d(TAG, "IN MAINACTIVITY isAuthenticated is: " + isAuthenticated.toString());
        Log.d(TAG, "IN MAINACTIVITY CURRENT USER is: " + currentUser.isEmailVerified());

        if (!isAuthenticated) {
            Log.d(TAG, "IN MAINACTIVITY INSIDE ISAUTHENTICATED is: " + isAuthenticated.toString());
            currentUser = null;

          //  Log.d(TAG, "IN MAINACTIVITY currentuser  is: " + currentUser.toString());
        }*/
        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        Boolean isAuthenticated = settings.getBoolean(PREF_SIGNUP_USER_AUTHENTICATED, false);
        System.out.println("MAIN ACTIVITY PRINTING iSAuthenticated value: " + isAuthenticated);
        System.out.println("PRINTING iSAuthenticated value: " + isAuthenticated);
        System.out.println("PRINTING iSAuthenticated value: " + isAuthenticated);
        if (!isAuthenticated) {
            if (mAuth.getCurrentUser() != null) {
                System.out.println("Printing currentuser" + mAuth.getCurrentUser().toString());
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
        }


        if (currentUser == null) {
            sendToLoginScreen();
        } else {
          //  Toast.makeText(MainActivity.this, "Logged in as " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
            sendToCreateProfile();
        }
       // mAuth.addAuthStateListener(mAuthlistener);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
             //   statusTextView.setText("You have Signed Out");
            }
        });
    }

    public void sendToLoginScreen() {
        Intent intent = new Intent(MainActivity.this, LoginUIActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendToCreateProfile() {
        TinyDB tinyDB = new TinyDB(MainActivity.this);
        ArrayList<String> get_interest_array = tinyDB.getListString("interestArray");
        if (get_interest_array == null || get_interest_array.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, CreateUserProfile.class);
            startActivity(intent);
            System.out.println("INside createprofile null");
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            System.out.println("INside createprofile home");
            finish();
        }

    }


    /*

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("You have Signed Out");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "HandleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            statusTextView.setText("Welcome, Name:" + acct.getDisplayName());
        } else {

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "firebase Auth Error", Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    */






}
