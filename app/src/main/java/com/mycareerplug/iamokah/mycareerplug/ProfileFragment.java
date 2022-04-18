package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    Context myContext;
    View view;
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


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");

        this.view = view;

        tv = (TextView) view.findViewById(R.id.textView9);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearcheckbox);

        //     interestgroup = (RadioGroup) view.findViewById(R.id.interestradiogroup);
        student_rg = (RadioGroup) view.findViewById(R.id.student_type_radiogroup);
        grade_level_rg = (RadioGroup) view.findViewById(R.id.grade_level_radiogroup);

        grade_level_sp = view.findViewById(R.id.gradelevel_spinner);
        isCompleted = false;
        isSpinnerUsed = false;
        isInterestCompleted = false;

        createCheckBoxes();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    private void createCheckBoxes() {
        for (int i = 0; i < career_interests_array.length; i++) {
            String current_career_interest = career_interests_array[i];
            if (myContext != null && isAdded()) {
                CheckBox cb = new CheckBox(myContext);
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
        if (myContext != null && isAdded()) {
            ArrayAdapter<String> grade_level_adapter = new ArrayAdapter<String>(myContext,
                    android.R.layout.simple_spinner_dropdown_item, other_grade_level_array);
            grade_level_sp.setAdapter(grade_level_adapter);
        }
    }

    private String getRadioGroupValue(RadioGroup rg) {
        String getstring = "";
        if (rg.getCheckedRadioButtonId() != -1) {
            // get selected radio button from radioGroup
            int selectedId = rg.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton selectedRadioButton = (RadioButton) getView().findViewById(selectedId);

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
        if (myContext != null && isAdded()) {
            SharedPreferences storeuserinfo = myContext.getSharedPreferences(CREATEPROFILE_PREF,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = storeuserinfo.edit();
            //   boolean test = true;
            TinyDB tinyDB = new TinyDB(myContext);
            tinyDB.putBoolean("first_time_login", true);
            tinyDB.putListString("interestArray", interestArray);
            editor.putString(PREF_INTEREST, "Business");
            editor.putString(PREF_STUDENT_TYPE, getRadioGroupValue(student_rg));
            editor.putString(PREF_GRADE_LEVEL, getGradeLevel(isSpinnerUsed));

            for (int i = 0; i < interestArray.size(); i++) {
                System.out.println("i " + i + " val " + interestArray.get(i));
            }
            System.out.println("Printing interest: " +
                    " student type: " + getRadioGroupValue(student_rg) + " grade level: " + getGradeLevel(isSpinnerUsed));

            editor.commit();
        }

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //       int pos;
        //     String actualPos = "";
        int id = item.getItemId();
        if (id == R.id.done_checkmark) {
            System.out.println("CLICKED CHECKMARK BUTTON");

            if (interestArray.isEmpty()) {
                if (myContext != null && isAdded()) {
                    Toast.makeText(myContext, "Please select a value for career interest.",
                            Toast.LENGTH_LONG).show();
                }

                isCompleted = false;
                isInterestCompleted = false;
            } else {
                isCompleted = true;
                isInterestCompleted = true;
            }

            //User has not clicked HS/college/other radio button.
            if (student_rg.getCheckedRadioButtonId() == -1) {
                if (myContext != null && isAdded()) {
                    Toast.makeText(myContext, "Please select a student type.",
                            Toast.LENGTH_LONG).show();
                }

                isCompleted = false;
            } else {
                // get selected radio button from radioGroup
                int selectedId = student_rg.getCheckedRadioButtonId();
                System.out.println("Printing SELECTED ID " + selectedId);
                System.out.println("Printing SELECTED ID " + selectedId);
                System.out.println("Printing SELECTED ID " + selectedId);
                // find the radiobutton by returned id
                RadioButton selectedRadioButton = (RadioButton)this.getView().findViewById(selectedId);
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
                        if (myContext != null && isAdded()) {
                            Toast.makeText(myContext, "Please select grade level.",
                                    Toast.LENGTH_LONG).show();
                        }
                        isCompleted = false;
                    } else {
                        isCompleted = true;
                    }

                }
            }


            // return true;

            if (isCompleted && isInterestCompleted) {
                saveInfo();
               // Toast.makeText(myContext, "saved profile", Toast.LENGTH_SHORT).show();
                sendToHome();


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

    private void sendToHome() {
        // TODO: 3/24/2019  Change bottomnavigationview to home.
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setBottomNavSelected(R.id.discover);

        FragmentTransaction ft = this.getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new DiscoverFragment()).commit();
    }


}
