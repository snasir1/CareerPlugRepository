package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class CreateProfile extends AppCompatActivity {
    private static final String[] interests_array = {"Please select a category", "Science", "Technology", "Engineering",
            "Business", "Personal Development", "Liberal Arts"};

    private static final String[] student_type_array = {"Please select a category", "High School",
            "College", "Other"};

    private static final String[] grade_level_array = {"Please select a category",
            "Freshman", "Sophomore", "Junior", "Senior"};

    private static final String[] college_grade_level_array = {"Please select a category",
            "College Freshman", "College Sophomore", "College Junior", "College Senior", "College Graduate"};

    private static final String[] highschool_grade_level_array = {"Please select a category",
            "9th Grade", "10th Grade", "11th Grade", "12th Grade", "High School Graduate"};



    private static final String[] other_grade_level_array = {"Please select a category",
            "6th Grade", "7th Grade", "8th Grade", "Community College Student",
            "Graduate Student", "Law Student"};

    public static final String TAG = "CREATEPROFILE";

    public static final String CREATEPROFILE_PREF = "createprof_pref";
    public static final String PREF_INTEREST = "interest";
    public static final String PREF_STUDENT_TYPE = "student_type";
    public static final String PREF_GRADE_LEVEL = "grade_level";

    Button logoutbtn;
    Spinner interest_sp, student_type_sp, grade_level_sp;

    boolean isCompleted, isOtherSelected;


    FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setTitle("Create Profile");


        isCompleted = true;
        isOtherSelected = false;

        logoutbtn = findViewById(R.id.logoutbtn);
        interest_sp = findViewById(R.id.interest_spinner);
        student_type_sp = findViewById(R.id.student_type_spinner);

        grade_level_sp = findViewById(R.id.gradelevel_spinner);


        //INITIALIZE INTEREST SPINNER
        ArrayAdapter<String> interest_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, interests_array);
        interest_sp.setAdapter(interest_adapter);

        interest_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("INTEREST SPINNER ITEM: ", (String) parent.getItemAtPosition(position));

               // System.out.println("checking position of item " + position);
                //parent.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
           //     parent.setSelection(0);
            }
        });


        //INITIALIZE STUDENT TYPE SP;

        ArrayAdapter<String> student_type_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, student_type_array);
        student_type_sp.setAdapter(student_type_adapter);

        student_type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("STUDENT TYPE ITEM: ", (String) parent.getItemAtPosition(position));
                ArrayAdapter<String> tempAdapter;

                switch (position) {
                    //Selecting highschool
                    case 1:
                        grade_level_sp.setSelection(0);
                        grade_level_sp.setEnabled(true);
                        grade_level_sp.setClickable(true);
                        tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                highschool_grade_level_array);
                        grade_level_sp.setAdapter(tempAdapter);
                        break;
                    //selecting college
                    case 2:
                        grade_level_sp.setSelection(0);
                        grade_level_sp.setEnabled(true);
                        grade_level_sp.setClickable(true);
                        tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                college_grade_level_array);
                        grade_level_sp.setAdapter(tempAdapter);
                        break;
                    //selecting other
                    case 3:
                        grade_level_sp.setSelection(0);
                        grade_level_sp.setEnabled(true);
                        grade_level_sp.setClickable(true);
                        tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                                android.R.layout.simple_spinner_dropdown_item, other_grade_level_array);
                        grade_level_sp.setAdapter(tempAdapter);
                        break;
                    default:
                        grade_level_sp.setEnabled(false);
                        grade_level_sp.setClickable(false);
                        tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                                android.R.layout.simple_spinner_dropdown_item, grade_level_array);
                        grade_level_sp.setAdapter(tempAdapter);
                        break;
                }
              /*  if (parent.getItemAtPosition(position).toString().equals("Other")) {
                    //set grade_level to please select category.
                    grade_level_sp.setSelection(0);
                   tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                            android.R.layout.simple_spinner_dropdown_item, other_grade_level_array);
                    grade_level_sp.setAdapter(tempAdapter);
                } else {
                    tempAdapter = new ArrayAdapter<String>(CreateProfile.this,
                            android.R.layout.simple_spinner_dropdown_item, grade_level_array);
                } */

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //INITIALIZE GRADE LEVEL SPINNER
        ArrayAdapter<String> grade_level_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, grade_level_array);
        grade_level_sp.setEnabled(false);
        grade_level_sp.setClickable(false);
        grade_level_sp.setAdapter(grade_level_adapter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(CreateProfile.this, "Got Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfile.this, LoginUIActivity.class);
                startActivity(intent);
                finish();
                mAuth.getInstance().signOut();
                signOut();
            }
        });
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //   statusTextView.setText("You have Signed Out");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 //       int pos;
   //     String actualPos = "";
        int id = item.getItemId();
        if (id == R.id.done_checkmark) {
            System.out.println("CLICKED CHECKMARK BUTTON");
            System.out.println("CLICKED CHECKMARK BUTTON");
            System.out.println("CLICKED CHECKMARK BUTTON");
            System.out.println("CLICKED CHECKMARK BUTTON");
         //   pos = interest_sp.getSelectedItemPosition();

//            actualPos = (String) interest_sp.getItemAtPosition(pos);
  //          System.out.println("pos: " + pos + " actualPos: " + actualPos);
            if (isSpinnerValid(interest_sp, "Interest") &&
                    isSpinnerValid(student_type_sp, "Student type") &&
                    isSpinnerValid(grade_level_sp, "Grade level")) {
         //       return true;
            } else {
       //         return false;
            }
            if (!isSpinnerValid(interest_sp, "Interest")) {
                isCompleted = false;
            }

            if (!isSpinnerValid(student_type_sp, "Student type")) {
                isCompleted = false;
            }

            if (!isSpinnerValid(grade_level_sp, "Grade level")) {
                isCompleted = false;
            }

            if (isSpinnerValid(interest_sp, "Interest") &&
                    isSpinnerValid(student_type_sp, "Student type")
                    && isSpinnerValid(grade_level_sp, "Grade level")) {
                isCompleted = true;
            }


            if (isCompleted) {
                saveInfo();
                sendToFragmentDedtailActivity();
                return true;
            } else {
                return false;
            }


         /*   if (actualPos.equals("Please select a category")) {
                System.out.println("INSIDE ifstatement");
                setSpinnerError(interest_sp, "Interest field required");
                return false;
            } */

            //return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void saveInfo() {
        SharedPreferences storeuserinfo = getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
        boolean test = true;
        editor.putString(PREF_INTEREST, interest_sp.getItemAtPosition(interest_sp.getSelectedItemPosition()).toString());
        Log.d(TAG, "interest" + interest_sp.getItemAtPosition(interest_sp.getSelectedItemPosition()).toString());
        editor.putString(PREF_STUDENT_TYPE, student_type_sp.getItemAtPosition(student_type_sp.getSelectedItemPosition()).toString());
        Log.d(TAG, "student type" + student_type_sp.getItemAtPosition(student_type_sp.getSelectedItemPosition()).toString());
        editor.putString(PREF_GRADE_LEVEL, grade_level_sp.getItemAtPosition(grade_level_sp.getSelectedItemPosition()).toString());
        Log.d(TAG, "grade level" + grade_level_sp.getItemAtPosition(grade_level_sp.getSelectedItemPosition()).toString());

        editor.commit();
    }


    private boolean isSpinnerValid(Spinner my_spinner, String spinner_type) {
        int position = my_spinner.getSelectedItemPosition();
        String actual_pos = (String) my_spinner.getItemAtPosition(position);
        System.out.println("pos: " + position + " actualPos: " + actual_pos);
        if (actual_pos.equals("Please select a category")) {
            setSpinnerError(my_spinner, spinner_type + " field required");
            return false;
        } else {
            return true;
        }
    }

    private void setSpinnerError(Spinner spinner, String error){
        System.out.println("INSIDE setSpinnerError");
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
        }
    }

    public void sendToFragmentDedtailActivity() {
        Intent intent = new Intent(CreateProfile.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }


}
