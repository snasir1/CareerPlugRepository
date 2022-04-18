package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.CREATEPROFILE_PREF;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_GRADE_LEVEL;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_INTEREST;

public class listview_activity extends AppCompatActivity {

    private static final String TAG = "listview_activity";

    DatabaseReference mRef;

    public ArrayList<Integer> mfilteredArray;

    String[] nameArray, infoArray;

    ArrayList<String> programNamesList = new ArrayList<>();
    ArrayList<String> interested_Deadline = new ArrayList<>();

    ArrayList<String> thrash_programNames = new ArrayList<>();
    ArrayList<String> thrash_Deadlines = new ArrayList<>();


    String interest, grade;

    CustomListAdapter listAdapter;
    ListView listView;

    String getIntentValue;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_activity);


        mfilteredArray = new ArrayList<>();
        mRef = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.listviewid);

        Intent i = getIntent();

        if (i.hasExtra("Interested")) {
            getIntentValue = i.getExtras().getString("Interested");
            System.out.println("printing getINtentvalue " + getIntentValue);
        } else {
            getIntentValue = i.getExtras().getString("Thrash");
            System.out.println("printing getINtentvalue " + getIntentValue);
        }

        eventlist_array();
        //System.out.println("printing size of nameArray " + nameArray.length);
      //  CustomListAdapter listAdapter = new CustomListAdapter(this, nameArray, infoArray);

       // listView.setAdapter(listAdapter);
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(listview_activity.this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void eventlist_array() {
        SharedPreferences settings = getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        interest = settings.getString(PREF_INTEREST, "");
        grade = settings.getString(PREF_GRADE_LEVEL, "");
      //  System.out.println("main2act pref interest value: " + interest + " gradelevel " + grade);
        mRef.child("user").child("event_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // System.out.println("children count " + dataSnapshot.getChildrenCount());

                String category, gradelevel;
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    category = dataSnapshot.child(Integer.toString(i)).child("CATEGORY").getValue().toString();
                    gradelevel = dataSnapshot.child(Integer.toString(i)).child("Grade Level").getValue().toString();
                    // System.out.println("Index: " + i + " category: " + category + " and gradelevel: " + gradelevel);

                    //  System.out.println("before if statement. value of category " + category + " value of interest  " + interest);
                    //  System.out.println("before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    //   Log.v(TAG, "before if statement. value of category " + category + " value of interest  " + interest);
                    //   Log.v(TAG, "before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    if (category.contains(interest) && gradelevel.contains(grade)) {
                        //      System.out.println("main adding index " + i + " to array");
                        //     Log.v(TAG, "main adding index " + i + " to array");
                        mfilteredArray.add(i);
                    }
                }

                if (mfilteredArray.size() != 0) {
                    removeDuplicates(mfilteredArray);
                }
                //  System.out.println("filter size " + filteredArray.size());
              //  Log.v(TAG, "filter size " + mfilteredArray.size());
                ArrayList<String> programList, deadlineList;
                programList = new ArrayList<String>();
                deadlineList = new ArrayList<String>();
                for (int i = 0; i < mfilteredArray.size(); i++) {
                    programList.add(dataSnapshot.child(Integer.toString(mfilteredArray.get(i))).child("PROGRAM NAME").getValue().toString());
                    deadlineList.add(dataSnapshot.child(Integer.toString(mfilteredArray.get(i))).child("DEADLINE").getValue().toString());
                   // System.out.println("printing i " + i + " programarray "
                  //          + dataSnapshot.child(Integer.toString(mfilteredArray.get(i))).child("PROGRAM NAME").getValue().toString() + "  " +
                //            dataSnapshot.child(Integer.toString(mfilteredArray.get(i))).child("DEADLINE").getValue().toString()
                //    );
                }

                nameArray = programList.toArray(new String[programList.size()]);
                infoArray = deadlineList.toArray(new String[deadlineList.size()]);

                if (getIntentValue.equals("Interested")) {
                    programNamesList = getArrayList("interested_program_key");
                    interested_Deadline = getArrayList("interested_deadline_key");
                    listAdapter = new CustomListAdapter(listview_activity.this, programNamesList, interested_Deadline);
                    listView.setAdapter(listAdapter);
                } else if (getIntentValue.equals("Thrash")) {
                    thrash_programNames = getArrayList("thrash_program_key");
                    thrash_Deadlines = getArrayList("thrash_deadline_key");
                    listAdapter = new CustomListAdapter(listview_activity.this, thrash_programNames, thrash_Deadlines);
                    listView.setAdapter(listAdapter);
                }



          //      System.out.println("inside eventlist nameArray size " + nameArray.length + "  infoarray size " + infoArray.length);


               /* for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Printing snapshot.child category: " + snapshot.child("CATEGORY (Insert IDs from Interests list)"));
                   // User user = snapshot.getValue(User.class);
                   // System.out.println(user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
  //      System.out.println("before returning" + mfilteredArray.size());

       // removeDuplicates(mfilteredArray);


        //return mfilteredArray;

    }

    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    } */
}
