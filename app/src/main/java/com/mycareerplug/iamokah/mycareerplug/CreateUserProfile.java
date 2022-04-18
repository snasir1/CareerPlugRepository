package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateUserProfile extends AppCompatActivity {

    TextView tv;
    ArrayList<String> interestArray = new ArrayList<>();
    RadioGroup grade_level_rg, student_rg;
    LinearLayout linearLayout;
    Spinner grade_level_sp;
    boolean isCompleted, isSpinnerUsed, isInterestCompleted;

    String interest, student_type, grade_level;

    private static final String[] career_interests_array = {"Arts/Design", "Business", "Engineering",
            "Health Sciences", "Liberal Arts", "Mathematics", "Personal Development", "Science", "Technology",
            "Trades"};

    private static final String[] other_grade_level_array = {"Please select a category",
            "6th Grade", "7th Grade", "8th Grade", "High School Graduate", "Community College Student",
            "Graduate Student", "College Graduate", "Law Student"};

    public static final String CREATEPROFILE_PREF = "createprof_pref";
    public static final String PREF_INTEREST = "interest";
    public static final String PREF_STUDENT_TYPE = "student_type";
    public static final String PREF_GRADE_LEVEL = "grade_level";

    String getIntentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        getSupportActionBar().setTitle("Create Profile");

       /* Intent i = getIntent();

        if (i.hasExtra("HomeProfile")) {
            getIntentValue = i.getExtras().getString("HomeProfile");
            System.out.println("printing getINtentvalue " + getIntentValue);
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } */

        tv = (TextView) findViewById(R.id.textView9);

        linearLayout = (LinearLayout) findViewById(R.id.linearcheckbox);

   //     interestgroup = (RadioGroup) findViewById(R.id.interestradiogroup);
        student_rg = (RadioGroup) findViewById(R.id.student_type_radiogroup);
        grade_level_rg = (RadioGroup) findViewById(R.id.grade_level_radiogroup);

        grade_level_sp = findViewById(R.id.gradelevel_spinner);
        isCompleted = true;
        isSpinnerUsed = false;
        isInterestCompleted = false;

      //  createRadioButtons();
       // changeUI();
        createCheckBoxes();
    }

    private void createCheckBoxes() {
        for (int i = 0; i < career_interests_array.length; i++) {
            String current_career_interest = career_interests_array[i];

            CheckBox cb = new CheckBox(this);
            cb.setText(current_career_interest);
            cb.setId(i+100);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    // Check which checkbox was clicked
                    if (checked){
                        // Do your coding
                        if (!interestArray.contains(((CheckBox) v).getText().toString())) {
                            interestArray.add(((CheckBox) v).getText().toString());
                        }
                        Log.d("onclick cb", "Checkbox:ID " + v.getId() + " text " + ((CheckBox) v).getText().toString());
                    }
                    else{
                        // Do your coding
                        if (interestArray.contains(((CheckBox) v).getText().toString())) {
                            interestArray.remove(((CheckBox) v).getText().toString());
                        }
                        System.out.println("Not checked" + v.getId());
                    }
                }
            });
            linearLayout.addView(cb);
        }

        student_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) student_rg.findViewById(checkedId);
                if (rb != null) {
                    if (!rb.getText().toString().equals("Other")) {
                        showGradeButtons(true);
                    } else {
                        showSpinner();
                    }
                }
            }
        });
    }

    private void createRadioButtons() {
       // final RadioGroup interestgroup = (RadioGroup) findViewById(R.id.interestradiogroup);

        //create radio buttons.
        for (int i = 0; i < career_interests_array.length; i++) {
            String current_career_interest = career_interests_array[i];

            RadioButton rb = new RadioButton(this);
            rb.setText(current_career_interest);


            //interestgroup.addView(rb);
        }
/*
        interestgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) interestgroup.findViewById(checkedId);
                if (rb != null) {
                    Toast.makeText(CreateUserProfile.this, rb.getText(), Toast.LENGTH_LONG).show();
                }
            }
        }); */

        student_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) student_rg.findViewById(checkedId);
                if (rb != null) {
                    if (!rb.getText().toString().equals("Other")) {

                       showGradeButtons(true);
                    } else {
                       showSpinner();
                    }
                }
            }
        });

    }

    private void changeUI() {
        RadioGroup student_type_rg = (RadioGroup) findViewById(R.id.student_type_radiogroup);
        int select_type_id = student_type_rg.getCheckedRadioButtonId();
        RadioButton rb = findViewById(select_type_id);

        String rb_text = rb.getText().toString();
        if (rb_text.equals("Other")) {
            showSpinner();
        } else {
            showGradeButtons(false);
        }
    }

    private void showGradeButtons(boolean isShow) {
        if (isShow) {
            grade_level_sp.setVisibility(View.GONE);

            tv.setVisibility(View.VISIBLE);
            grade_level_rg.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
            grade_level_rg.setVisibility(View.GONE);
        }

    }

    private void showSpinner() {
        showGradeButtons(false);
        grade_level_sp.setVisibility(View.VISIBLE);
        //INITIALIZE GRADE LEVEL SPINNER
        ArrayAdapter<String> grade_level_adapter = new ArrayAdapter<String>(CreateUserProfile.this,
                android.R.layout.simple_spinner_dropdown_item, other_grade_level_array);
        grade_level_sp.setAdapter(grade_level_adapter);

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

            if (interestArray.isEmpty()) {
                Toast.makeText(CreateUserProfile.this, "Please select a value for career interest.",
                        Toast.LENGTH_LONG).show();
                isCompleted = false;
                isInterestCompleted = false;
            } else {
                isCompleted = true;
                isInterestCompleted = true;
            }

            //User has not clicked HS/college/other radio button.
            if (student_rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(CreateUserProfile.this, "Please select a student type.",
                        Toast.LENGTH_LONG).show();
                isCompleted = false;
            } else {
                // get selected radio button from radioGroup
                int selectedId = student_rg.getCheckedRadioButtonId();
                System.out.println("Printing SELECTED ID " + selectedId);
                System.out.println("Printing SELECTED ID " + selectedId);
                System.out.println("Printing SELECTED ID " + selectedId);
                // find the radiobutton by returned id
                RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);
                if (selectedRadioButton.getText().toString().equals("Other")) {
                    if (!isSpinnerValid(grade_level_sp, "Grade level")) {
                        isCompleted = false;
                        isSpinnerUsed = false;
                    } else {
                        isCompleted = true;
                        isSpinnerUsed = true;
                    }
                } else {
                    //college/hs radio button
                    if (grade_level_rg.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(CreateUserProfile.this, "Please select grade level.",
                                Toast.LENGTH_LONG).show();
                        isCompleted = false;
                    } else {
                        isCompleted = true;
                    }

                }
            }


           // return true;

            if (isCompleted && isInterestCompleted) {
                saveInfo();
                sendToMain();

             //   System.out.println("Printing interest: " + getRadioGroupValue(interestgroup) +
              //          " student type: " + getRadioGroupValue(student_rg) + " grade level: " + getRadioGroupValue(grade_level_rg));
                return true;
            } else {
                System.out.println("test");
                isCompleted = false;
                isInterestCompleted = false;
                isSpinnerUsed = false;
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getRadioGroupValue(RadioGroup rg) {
        String getstring = "";
        if (rg.getCheckedRadioButtonId() != -1) {
            // get selected radio button from radioGroup
            int selectedId = rg.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton selectedRadioButton = (RadioButton)findViewById(selectedId);

            getstring = selectedRadioButton.getText().toString();
        }
        return getstring;
    }

    private String getGradeLevel(boolean isSpinnerUsed) {
        String value = "";
        if (!isSpinnerUsed) {
            value = getRadioGroupValue(grade_level_rg);
        } else {
            value = grade_level_sp.getItemAtPosition(grade_level_sp.getSelectedItemPosition()).toString();
        }

        return value;
    }

    private void saveInfo() {
        SharedPreferences storeuserinfo = getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storeuserinfo.edit();
     //   boolean test = true;
        TinyDB tinyDB = new TinyDB(this);
        tinyDB.putListString("interestArray", interestArray);
        editor.putString(PREF_INTEREST, "Business");
        editor.putString(PREF_STUDENT_TYPE, getRadioGroupValue(student_rg));
        editor.putString(PREF_GRADE_LEVEL, getGradeLevel(isSpinnerUsed));

        for (int i = 0; i < interestArray.size(); i++) {
            System.out.println("i " + i + " val" + interestArray.get(i));
        }
        System.out.println("Printing interest: " +
                " student type: " + getRadioGroupValue(student_rg) + " grade level: " + getGradeLevel(isSpinnerUsed));

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

    public void sendToMain() {
        Intent intent = new Intent(CreateUserProfile.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
