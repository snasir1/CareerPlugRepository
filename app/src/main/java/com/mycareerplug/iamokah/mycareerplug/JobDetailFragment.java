package com.mycareerplug.iamokah.mycareerplug;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.CREATEPROFILE_PREF;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_GRADE_LEVEL;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_INTEREST;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_PHONE;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;


/**
 * A simple {@link Fragment} subclass.
 */


public class JobDetailFragment extends Fragment {

    private static final String TAG = "Main_Activity";

    TextView name, program_name, application_start, application_end;

    TextView event_type, desc, city, state, event_start, event_end;

    TextView gpa, sat_min, male, female;

    Button mainz;

    String interest, grade;

    DatabaseReference mRef;
    ArrayList<Integer> filteredArray;
    public JobDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        filteredArray = new ArrayList<>();
        mainz = view.findViewById(R.id.mainact);

        name = view.findViewById(R.id.institution_text);
        program_name = view.findViewById(R.id.program_id);

        application_start = view.findViewById(R.id.application_start);
        application_end = view.findViewById(R.id.application_end);
       // name.setText("Testing");

        event_type = view.findViewById(R.id.event_type_text);
        desc = view.findViewById(R.id.description);
        city = view.findViewById(R.id.city_text);
        state = view.findViewById(R.id.state_text);
        event_start = view.findViewById(R.id.event_start);
        event_end = view.findViewById(R.id.event_end);

        //initialize requirements
        gpa = view.findViewById(R.id.gpa_id);
        sat_min = view.findViewById(R.id.sat_id);
        male = view.findViewById(R.id.male_only);
        female = view.findViewById(R.id.female_only);

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child("user").child("event_list").child(Integer.toString(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> test = (Map) dataSnapshot.getValue();
                Log.v(TAG, "DATA: " + test.toString());
                Log.v(TAG, "test size" + test.size());
                String vals = "";
                for(int i=0; i <test.size(); i++) {

                    if (test.values().toArray()[i].equals("")) {
                        System.out.println("INSIDE CHANGING " );
                        test.put(test.keySet().toArray()[i].toString(), "N/A");
                    }

                    if (test.keySet().toArray()[i].equals("INSTITUTION NAME")) {
                        name.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("PROGRAM NAME")) {
                        program_name.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("APPLICATION OPEN DATE")) {
                        application_start.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("APPLICATION DUE DATE")) {
                        application_end.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("TYPE OF EVENT")) {
                        event_type.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("INSTITUTION DESCRIPTION")) {
                        desc.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("EVENT START DATE")) {
                        event_start.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("EVENT END DATE")) {
                        event_end.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("CITY")) {
                        city.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("STATE")) {
                        state.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("GPA Minimum")) {
                        gpa.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("SAT Minimum")) {
                        sat_min.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("Males Only")) {
                        male.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("Females Only")) {
                        female.setText(test.values().toArray()[i].toString());
                    }

                    vals = test.keySet().toArray()[i] + " = " + test.values().toArray()[i] + "\n";
                    //Log.v(TAG, "vals DATA: " + vals);
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

/*
        Query categoryQuery = mRef.child("user").child("event_list")
                .orderByChild("CATEGORY (Insert IDs from Interests list)").equalTo("Liberal Arts");
        System.out.println("before query");


        categoryQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do with your result
                        Log.v(TAG, "DATA: " + issue.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

        /*
        SharedPreferences settings = getActivity().getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        interest = settings.getString(PREF_INTEREST, "");
        grade = settings.getString(PREF_GRADE_LEVEL, "");
        System.out.println("jobdetailfragment pref interest value: " + interest + " gradelevel " + grade);
        mRef.child("user").child("event_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("children count " + dataSnapshot.getChildrenCount());

                String category, gradelevel;
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    category = dataSnapshot.child(Integer.toString(i)).child("CATEGORY (Insert IDs from Interests list)").getValue().toString();
                    gradelevel = dataSnapshot.child(Integer.toString(i)).child("Grade Level").getValue().toString();
                   // System.out.println("Index: " + i + " category: " + category + " and gradelevel: " + gradelevel);

                  //  System.out.println("before if statement. value of category " + category + " value of interest  " + interest);
                  //  System.out.println("before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    Log.v(TAG, "before if statement. value of category " + category + " value of interest  " + interest);
                    Log.v(TAG, "before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    if (category.contains(interest) && gradelevel.contains(grade)) {
                        System.out.println("adding index " + i + " to array");
                        Log.v(TAG, "adding index " + i + " to array");
                        filteredArray.add(i);
                    }
                }

                if (filteredArray.size() != 0) {
                    for (int i = 0; i < filteredArray.size(); i++) {
                    //    System.out.println("printing filtered array " + filteredArray.get(i).toString());
                        Log.v(TAG, "printing filtered array " + filteredArray.get(i).toString());
                    }
                }
              //  System.out.println("filter size " + filteredArray.size());
                Log.v(TAG, "filter size " + filteredArray.size());


               /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Printing snapshot.child category: " + snapshot.child("CATEGORY (Insert IDs from Interests list)"));
                   // User user = snapshot.getValue(User.class);
                   // System.out.println(user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */
            /*
                mRef.child("user").child("event_list").child(Integer.toString(0))
                        .child("CATEGORY (Insert IDs from Interests list)").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Log.d("TAG", "snapshotvalue for child 0: " + dataSnapshot.getValue());
                        } else {
                            Log.d("TAG", "NULL for child 0: " + dataSnapshot.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); */



        mainz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Main2Activity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Integer> eventlist_array() {
        SharedPreferences settings = getActivity().getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        interest = settings.getString(PREF_INTEREST, "");
        grade = settings.getString(PREF_GRADE_LEVEL, "");
        System.out.println("jobdetailfragment pref interest value: " + interest + " gradelevel " + grade);
        mRef.child("user").child("event_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("children count " + dataSnapshot.getChildrenCount());

                String category, gradelevel;
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    category = dataSnapshot.child(Integer.toString(i)).child("CATEGORY (Insert IDs from Interests list)").getValue().toString();
                    gradelevel = dataSnapshot.child(Integer.toString(i)).child("Grade Level").getValue().toString();
                    // System.out.println("Index: " + i + " category: " + category + " and gradelevel: " + gradelevel);

                    //  System.out.println("before if statement. value of category " + category + " value of interest  " + interest);
                    //  System.out.println("before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    Log.v(TAG, "before if statement. value of category " + category + " value of interest  " + interest);
                    Log.v(TAG, "before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    if (category.contains(interest) && gradelevel.contains(grade)) {
                        System.out.println("adding index " + i + " to array");
                        Log.v(TAG, "adding index " + i + " to array");
                        filteredArray.add(i);
                    }
                }

                if (filteredArray.size() != 0) {
                    for (int i = 0; i < filteredArray.size(); i++) {
                        //    System.out.println("printing filtered array " + filteredArray.get(i).toString());
                        Log.v(TAG, "printing filtered array " + filteredArray.get(i).toString());
                    }
                }
                //  System.out.println("filter size " + filteredArray.size());
                Log.v(TAG, "filter size " + filteredArray.size());


               /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Printing snapshot.child category: " + snapshot.child("CATEGORY (Insert IDs from Interests list)"));
                   // User user = snapshot.getValue(User.class);
                   // System.out.println(user.email);
                } */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return filteredArray;

    }

/*
    private void setData(DatabaseReference mRef) {
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("user").child("event_list").child(Integer.toString(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> test = (Map) dataSnapshot.getValue();
                Log.v(TAG, "DATA: " + test.toString());
                Log.v(TAG, "test size" + test.size());
                String vals = "";
                for(int i=0; i <test.size(); i++) {

                    if (test.keySet().toArray()[i].equals("INSTITUTION NAME")) {

                    }

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
                            dbtext.setText(vals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } */
}
