package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.markushi.ui.CircleButton;

import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.CREATEPROFILE_PREF;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_GRADE_LEVEL;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_INTEREST;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_STUDENT_TYPE;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_EMAIL;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.PREF_SIGNUP_USER_NAME;
import static com.mycareerplug.iamokah.mycareerplug.SignupActivity.SIGNUP_PREFNAME;
//import static com.mycareerplug.iamokah.mycareerplug.listview_activity.removeDuplicates;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";

    private static final String[] other_grade_level_array = {"6th Grade", "7th Grade", "8th Grade",
            "HS Freshman", "HS Sophomore", "HS Junior", "HS Senior",
            "College Freshman", "College Sophomore", "College Junior", "College Senior", "Graduate Student"};

    CircleButton test;

    TextView name, program_name, app_end, city, state, event_type, prgmname, desc, no_item_text;

    String interest, grade, student_type;

    String instname, programname, deadline, cost, financialaid, pay, urlLink;
    String open_date, type_of_event, video_link;

    DatabaseReference mRef;

    public ArrayList<Integer> mfilteredArray;

    private DrawerLayout mDrawerLayout;
    private BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    private SwipeCardsView swipeCardsView;
    private List<CardModel> modelList = new ArrayList<>();

    ViewPager viewPager;
    pageAdapter pageAdapter;

    ActionBar ab;

    String title = "";
    String get_bottom_id, get_nav_id;

    FirebaseAuth mAuth;

    ArrayList<Integer> interested_modelarray_pos = new ArrayList<>();

    private static final String MENU_ITEM = "menu_item";
    private int menuItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Toolbar tb = findViewById(R.id.my_toolbar);
        setSupportActionBar(tb);

        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView userText = headerView.findViewById(R.id.nav_header_username);
        TextView emailText = headerView.findViewById(R.id.nav_header_user_email);



        //Display when no viewpage items are displayed
        no_item_text = findViewById(R.id.noitemtext);

        SharedPreferences settings = getSharedPreferences(SIGNUP_PREFNAME,
                Context.MODE_PRIVATE);
        String email = settings.getString(PREF_SIGNUP_USER_EMAIL, "");
        String username = settings.getString(PREF_SIGNUP_USER_NAME, "");

        if (username != null && !username.isEmpty() && email != null && !email.isEmpty()) {
            userText.setText(username);
            emailText.setText(email);
        } else {
            userText.setText("Syed Nasir");
            emailText.setText("nasir@gmail.com");
        }

      //  userText.setText(username);
       // emailText.setText(email);
        // TODO: 1/23/2019  save user info in database



     /*   LayoutInflater inflater = getLayoutInflater();
        View vi = inflater.inflate(R.layout.nav_header, null);
        LinearLayout test = vi.findViewById(R.id.nav_header_id);
        TextView navheaderText = vi.findViewById(R.id.nav_header_text);
        navheaderText.setText("My Name");
        test.view(vi); */
        get_nav_id = navigationView.getMenu().findItem(navigationView.getCheckedItem().getItemId()).toString();
        ab.setTitle(get_nav_id);
       // System.out.println("TESTING ISCHECKED" + navigationView.getCheckedItem().getItemId());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
               // ab.setTitle(menuItem.toString());
                title = menuItem.toString();
                System.out.println("printing title" + title);
                // close drawer when item is tapped
                mDrawerLayout.closeDrawers();

                get_nav_id  = menuItem.toString();
                ab.setTitle(get_nav_id);

                if (menuItem.toString().equals("Interested")) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
             //   System.out.println("Navid" + navigationView.getCheckedItem().getItemId());

                if (menuItem.toString().equals("Current")) {
                    System.out.println("selected current");

                    eventlist_array("current");
                }

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here
                System.out.println("SELECTED MENU ITEM " + menuItem.toString());
                if (menuItem.toString().equals("Interested")) {
                  /*  Intent i = new Intent(Main2Activity.this, listview_activity.class);
                    i.putExtra("Interested", "Interested");
                    startActivity(i); */
                    eventlist_array("Interested");
                } else if (menuItem.toString().equals("Thrash")) {
                    Intent i = new Intent(Main2Activity.this, listview_activity.class);
                    i.putExtra("Thrash", "Thrash");
                    startActivity(i);
                }

                if (menuItem.toString().equals("Upcoming")) {
                    System.out.println("selected upcoming");

                    eventlist_array("upcoming");
                }

                if (menuItem.toString().equals("Past Opportunities")) {

                    eventlist_array("past");
                }

                if (menuItem.toString().equals("Planning")) {
                    System.out.println("selected planning");

                    eventlist_array("planning");
                }

                return true;
            }
        });



        //BOTTOMNAV SETUP
        bottomNavigationView.setSelectedItemId(R.id.discover);
        get_bottom_id = bottomNavigationView.getMenu().
                findItem(bottomNavigationView.getSelectedItemId()).toString();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("Printing menu Item " + menuItem.toString());
                switch (menuItem.getItemId()) {
                    case R.id.discover:
                        System.out.println("touched explore");
                        get_bottom_id = menuItem.toString();
                        //resetList(modelList);
                        eventlist_array(get_nav_id.toLowerCase());
                        return true;
                    case R.id.interests:
                        System.out.println("touched education");
                        get_bottom_id = menuItem.toString();
                        eventlist_array(get_nav_id.toLowerCase());
                        return true;
                    case R.id.profile:
                        System.out.println("touched experience");
                        get_bottom_id = menuItem.toString();
                        eventlist_array(get_nav_id.toLowerCase());
                        return true;
                }

                return false;
            }
        });

          //  System.out.println("after menuitem " + menuItemId );
       //    System.out.println("print navid " +  navigationView.getMenu().findItem(navigationView.getCheckedItem().getItemId()).toString());
         //   navigationView.g
      //  navigationView.getCheckedItem().getItemId()


      //  test = findViewById(R.id.circleButton);

        mfilteredArray = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference();

       /* swipeCardsView = (SwipeCardsView) findViewById(R.id.swipecardview);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true); */
       // getData();
        eventlist_array("current");
        /*
        name = findViewById(R.id.institutionrealid);
        app_end = findViewById(R.id.app_due);
        program_name = findViewById(R.id.programrealid);
        city = findViewById(R.id.city);
       // state = findViewById(R.id.state);
        event_type = findViewById(R.id.event_typez);
        desc = findViewById(R.id.descrip);






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
                        name.setText(test.values().toArray()[i].toString().toUpperCase());
                    } else if (test.keySet().toArray()[i].equals("PROGRAM NAME")) {
                        program_name.setText(test.values().toArray()[i].toString().toUpperCase());
                    }  else if (test.keySet().toArray()[i].equals("APPLICATION DUE DATE")) {
                        app_end.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("TYPE OF EVENT")) {
                        event_type.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("INSTITUTION DESCRIPTION")) {
                        desc.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("CITY")) {
                        city.setText(test.values().toArray()[i].toString());
                    } else if (test.keySet().toArray()[i].equals("STATE")) {
                      //  state.setText(test.values().toArray()[i].toString());
                    }



                    vals = test.keySet().toArray()[i] + " = " + test.values().toArray()[i] + "\n";
                   // Log.v(TAG, "vals DATA: " + vals);
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

        //POPULATE FILTER ARRAY
        /*
        SharedPreferences settings = getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);
        minterest = settings.getString(PREF_INTEREST, "");
        mgrade = settings.getString(PREF_GRADE_LEVEL, "");
        //   System.out.println("jobdetailfragment pref interest value: " + interest + " gradelevel " + grade);
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
                    Log.v(TAG, "before if statement. value of category " + category + " value of interest  " + minterest);
                    Log.v(TAG, "before if statement. value of gradelevel " + gradelevel + " value of grade  " + mgrade);
                    if (category.contains(minterest) && gradelevel.contains(mgrade)) {
                        System.out.println("adding index " + i + " to array");
                        Log.v(TAG, "adding index " + i + " to array");
                        mfilteredArray.add(i);
                    }
                }

                if (mfilteredArray.size() != 0) {
                    for (int i = 0; i < mfilteredArray.size(); i++) {
                        //    System.out.println("printing filtered array " + filteredArray.get(i).toString());
                        //      Log.v(TAG, "printing filtered array " + filteredArray.get(i).toString());
                    }
                }
                //  System.out.println("filter size " + filteredArray.size());
                   // Log.v(TAG, "filter size " + mfilteredArray.size());


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
        //POPULATE DONE.
     //   Log.v(TAG, "filter size " + mfilteredArray.size());
        if (mfilteredArray.size() != 0) {
          //  Log.v(TAG, "First index of filterarray " + mfilteredArray.get(0).toString());
        }

        /*test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> event_array = eventlist_array();
                System.out.println("INSIDE PASS CIRCLE BUTTON arraysize is" + event_array.size());
                //Intent intent = new Intent(Main2Activity.this, FragmentDetailActivity.class);
                //startActivity(intent);
            }
        }); */


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MENU_ITEM, get_nav_id);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.get_bottom_id = savedInstanceState.getString(MENU_ITEM);
    }

    private int getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return i;
            }
        }

        return -1;
    }

    private void resetList(List<CardModel> modelList) {
        if (!modelList.isEmpty()) {
            modelList.clear();
        }
    }

    private void getData() {
     /*   modelList.add(new CardModel("Coursera", "Graphic Design", "Varies", "FREE+", "YES", ""));
        modelList.add(new CardModel("Coursera", "Effective Communication", "Varies", "FREE+", "YES", "N/A"));
        modelList.add(new CardModel("Community College of Baltimore County", "Kids @ CCBC", "May 01", "VARIES+", "N/A", "N/A"));
        modelList.add(new CardModel("Coursera", "Graphic Design", "Varies", "FREE+", "YES", ""));
        modelList.add(new CardModel("Community College of Baltimore County", "Kids @ CCBC", "May 01", "VARIES+", "N/A", "N/A"));
        */

        //eventlist_array();
        pageAdapter = new pageAdapter(modelList, this);
        pageAdapter.notifyDataSetChanged();
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pageAdapter);
        viewPager.setPadding(130, 50, 130, 50);
        viewPager.setPageMargin(50);



      //  CardAdapter cardAdapter = new CardAdapter(modelList, this);
      //  swipeCardsView.setAdapter(cardAdapter);
    }


    public void eventlist_array(final String getTime) {
        System.out.println("GETCHECK IS " + getTime);
        System.out.println("GET_NAV_ID IS " + get_nav_id);

        SharedPreferences settings = getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);


        interest = settings.getString(PREF_INTEREST, "");
        student_type = settings.getString(PREF_STUDENT_TYPE, "");
        grade = settings.getString(PREF_GRADE_LEVEL, "");

        if (navigationView.getCheckedItem() != null) {
            System.out.println("TESTING ISCHECKED" + navigationView.getCheckedItem().getItemId());

        }


        if (!modelList.isEmpty()) {
            modelList.clear();
            if (pageAdapter != null) {
                pageAdapter.notifyDataSetChanged();
            }
        }
        //  String instname, programname, deadline, cost, financialaid, pay;
    /*
      //  System.out.println("main2act pref interest value: " + interest + " gradelevel " + grade);
        mRef.child("user").child("event_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   System.out.println("children count " + dataSnapshot.getChildrenCount());
                int total = 0;
                int current_counter = 0;
                int upcoming_counter = 0, past_counter = 0;
                String category, gradelevel;
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    try {
                        category = dataSnapshot.child(Integer.toString(i)).child("CATEGORY").getValue().toString();
                        gradelevel = dataSnapshot.child(Integer.toString(i)).child("Grade Level").getValue().toString();
                        instname = dataSnapshot.child(Integer.toString(i)).child("INSTITUTION NAME").getValue().toString();
                        //System.out.println("instname " + instname);
                        programname = dataSnapshot.child(Integer.toString(i)).child("PROGRAM NAME").getValue().toString();
                        deadline = dataSnapshot.child(Integer.toString(i)).child("DEADLINE").getValue().toString();
                        cost = dataSnapshot.child(Integer.toString(i)).child("COST").getValue().toString();
                        financialaid = dataSnapshot.child(Integer.toString(i)).child("FINANCIAL AID AVAILABLE").getValue().toString();
                        pay = dataSnapshot.child(Integer.toString(i)).child("PAY").getValue().toString();
                        urlLink = dataSnapshot.child(Integer.toString(i)).child("LINK TO PROGRAM WEBSITE").getValue().toString();
                        open_date = dataSnapshot.child(Integer.toString(i)).child("APPLICATION OPEN DATE").getValue().toString();
                        type_of_event = dataSnapshot.child(Integer.toString(i)).child("TYPE OF EVENT").getValue().toString();
                        video_link = dataSnapshot.child(Integer.toString(i)).child("VIDEO LINKS FOR PICTURE").getValue().toString();
                    } catch (NullPointerException e) {
                        continue;
                    }

                  //  System.out.println("Type of event " + type_of_event);
                  //  System.out.println("bottomid " + get_bottom_id);
                   // System.out.println("filterEvent: " + filterEvent(get_bottom_id, type_of_event));

                   // filterEvent(get_bottom_id, type_of_event);

                    DateFormat dateFormat = new SimpleDateFormat("MM/dd");
                    Date date = new Date();
                    Date c_date = stringToDate(dateFormat.format(date));
                    Date o_date = stringToDate(open_date);
                    Date deadline_date = stringToDate(deadline);

                    // System.out.println("Index: " + i + " category: " + category + " and gradelevel: " + gradelevel);

                  //  System.out.println("printing deadline " + deadline);

                    //  System.out.println("before if statement. value of category " + category + " value of interest  " + interest);
                    //  System.out.println("before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);
                    //   Log.v(TAG, "before if statement. value of category " + category + " value of interest  " + interest);
                    //   Log.v(TAG, "before if statement. value of gradelevel " + gradelevel + " value of grade  " + grade);

                    ///checkCatagory(category);

                    if (getTime.equals("Interested")) {
                        TinyDB tinyDB = new TinyDB(Main2Activity.this);
                        interested_modelarray_pos = tinyDB.getListInt("interested_arraypos_key");
                        if (interested_modelarray_pos.contains(i)) {
                            modelList.add(new CardModel(programname, instname, deadline, cost,
                                    financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Interested", category));
                        }
                    }

                    if (getTime.equals("planning")) {
                        String glevel;
                        String[] test;
                        if (!student_type.equals("Other")) {
                            if (student_type.equals("High School")) {
                                glevel = "HS " + grade;
                            } else {
                                glevel = student_type + " " + grade;
                            }
                        } else {
                            glevel = grade;
                        }
                        System.out.println("glevel " + glevel);
                        test = getPlanning(glevel);
                        if (test != null) {
                            System.out.println("printing test1 " + test[0] + " test2 " + test[1]);
                            System.out.println("printing gradelevel.contains " + gradelevel);
                        }

                        if (!glevel.equals("Graduate Student") && test != null) {
                            if (checkCatagory(category)
                                    && (gradelevel.contains(test[0]) || gradelevel.contains(test[1]))
                                    && filterEvent(get_bottom_id, type_of_event)) {
                                modelList.add(new CardModel(programname, instname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Planning", category));
                            }
                        }

                    }

                    if (checkCatagory(category) && gradeLevel(student_type, grade, gradelevel) && filterEvent(get_bottom_id, type_of_event)) {
                        //      System.out.println("main adding index " + i + " to array");
                        //     Log.v(TAG, "main adding index " + i + " to array");
                     //   mfilteredArray.add(i);
                        if (getTime.equals("current")) {
                            if (compareDate(o_date, deadline_date, c_date)) {
                                modelList.add(new CardModel(programname, instname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Current", category));
                                current_counter++;
                              //  System.out.println("CURRENT val " + i);
                            }
                        } else if (getTime.equals("upcoming")) {
                            System.out.println("Open_date " + open_date);
                            if (open_date.equals("Varies") || compareDate2(c_date, addTwoMonths(c_date), o_date)) {
                                modelList.add(new CardModel(programname, instname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Upcoming", category));
                                upcoming_counter++;
                              //  System.out.println("UPCOMING val " + i);
                            }
                        } else if (getTime.equals("past") || getTime.equals("past opportunities")) {
                            if (compareDate(o_date, deadline_date, c_date) == false &&
                                    !(open_date.equals("Varies") || compareDate2(c_date, addTwoMonths(c_date), o_date))) {
                                past_counter++;
                                System.out.println("PAST val " + i);
                                modelList.add(new CardModel(programname, instname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Past", category));
                            }
                        }



                        total++;
                    }
                }
                System.out.println("Total count is " + total);
                System.out.println("current counter is " + current_counter);
                System.out.println("upcoming_counter " + upcoming_counter);
                System.out.println("past_counter " + past_counter);
                if (pageAdapter == null) {
                    pageAdapter = new pageAdapter(modelList, Main2Activity.this);
                    pageAdapter.notifyDataSetChanged();
                    getNoItemText(getTime, pageAdapter.getCount());
                    viewPager = findViewById(R.id.viewPager);
                    viewPager.setAdapter(pageAdapter);
                    viewPager.setPadding(130, 50, 130, 50);
                    viewPager.setPageMargin(50);
                } else {
                    pageAdapter.notifyDataSetChanged();
                    getNoItemText(getTime, pageAdapter.getCount());
                    System.out.println("printing childs " + pageAdapter.getCount());
                    viewPager.setAdapter(pageAdapter);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

    }

    private boolean filterEvent(String bottomid, String eventval) {
      //  Log.d(TAG, "filterEvent: bottomid " + bottomid);
      //  Log.d(TAG, "filterEvent: eventval " + eventval);

        if (bottomid != null && eventval != null) {
            if (bottomid.equals("Explore")) {
                if (eventval.contains("Exposure") || eventval.contains("Connections")) {
                    return true;
                }
            } else if (bottomid.equals("Education")) {
                if (eventval.contains("Skills and Training") || eventval.contains("Education and Credentials")) {
                    return true;
                }
            } else if (bottomid.equals("Experience")) {
                if (eventval.contains("Experience") || eventval.contains("Employment")) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkCatagory(String catagory) {

        TinyDB tinyDB = new TinyDB(Main2Activity.this);
        ArrayList<String> get_interest_array = tinyDB.getListString("interestArray");

    /*    for (int i = 0; i < get_interest_array.size(); i++) {
            System.out.println("PRINTING INTEREST ARRAY " + i + " val" + get_interest_array.get(i));
        } */

        for (int i = 0; i < get_interest_array.size(); i++) {
            System.out.println("Catagory is " + catagory);
            System.out.println("getInterestVal " + get_interest_array.get(i));
            if (catagory.contains(get_interest_array.get(i))) {
                //System.out.println("return true ");
                return true;
            } else {
                System.out.println("return false ");
            }
        }

        return false;
    }

    private boolean gradeLevel(String student_type, String gradelevel, String checkgrade) {
      /*  System.out.println("INSIDE GRADELEVEL");
        System.out.println("INSIDE GRADELEVEL");
        System.out.println("INSIDE GRADELEVEL");
        System.out.println("Printing student type: " + student_type + " gradelevel " + gradelevel + " checkgrade " + checkgrade); */
        boolean answer = false;
        if (student_type.equals("High School")) {
            if (checkgrade.contains("HS " + gradelevel)) {
                answer = true;
            }
        }
        if (student_type.equals("College")) {
            if (checkgrade.contains("College " + gradelevel)) {
                answer = true;
            }
        }

        if (student_type.equals("Other")) {
            if (checkgrade.contains(gradelevel)) {
                return true;
            }
        }
        //System.out.println("answer = " + answer);
        return answer;
    }

    private static String[] getPlanning(String gradelevel) {
        String[] answer = new String[2];
        for (int i = 0; i < other_grade_level_array.length; i++) {
            if (gradelevel.contains(other_grade_level_array[i])) {
                if (i == 10) {
                    answer[0] = other_grade_level_array[i+1];
                    answer[1] = "College Graduate";
                    return answer;
                } else if (i == 11) {
                    return null;
                } else {
                    answer[0] = other_grade_level_array[i+1];
                    answer[1] = other_grade_level_array[i+2];
                    return answer;
                }
            }
        }

        return null;
    }




    @Override
    protected void onStart() {

       // Log.v(TAG, "filter size ONSTART " + mfilteredArray.size());
        if (mfilteredArray.size() != 0) {
            //  Log.v(TAG, "First index of filterarray " + mfilteredArray.get(0).toString());
        }
        super.onStart();
    }



    private static Date stringToDate(String date) {
        //   System.out.println("date " + date);
        try {
            if (date.contains("-")) {
                return new SimpleDateFormat("dd-MMM").parse(date);
            } else if (date.contains("/")) {
                return new SimpleDateFormat("MM/dd").parse(date);
            }
        } catch (ParseException e) {
            return null;
        }


        return null;
    }

    private static boolean compareDate(Date date_start, Date date_end, Date current_date) {
        Date YEAR_START = stringToDate("1-Jan"),
                YEAR_END = stringToDate("31-Dec");

        if (date_start != null && date_end != null) {
            if (date_start.before(date_end) && !date_start.after(current_date) && !date_end.before(current_date)) {
                return true;
            } else if (date_start.after(date_end)) {
                if (!date_start.after(current_date) && !YEAR_END.before(current_date)) {
                    return true;
                } else if (!YEAR_START.after(current_date) && !date_end.before(current_date)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Date addTwoMonths(Date start) {
        // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
       // System.out.println("printing month " + month);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month == 10) {
            month = 1;
        } else if (month == 11) {
            month = 2;
        } else {
            month += 2;
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    }

    private static Date addMonths(Date start, int num_of_months) {
        // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        // System.out.println("printing month " + month);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month == 10) {
            month = 1;
        } else if (month == 11) {
            month = 2;
        } else {
            month += 2;
        }

        month += num_of_months;
        //10 + 3 = 13.  12 = december.
        if (month > 12) {
            month = month - 12; //13 - 12 = 1
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    }


    private static boolean compareDate2(Date c_date, Date c_date_plus_twomonths, Date open_date) {
        Date YEAR_START = stringToDate("1-Jan"),
                YEAR_END = stringToDate("31-Dec");

        if (c_date != null && c_date_plus_twomonths != null && open_date != null) {
            if (c_date.before(c_date_plus_twomonths) && !c_date.after(open_date) && !c_date_plus_twomonths.before(open_date)) {
                return true;
            } else if (c_date.after(c_date_plus_twomonths)) {
              //  System.out.println("compare2 else if");
                //Here c_date > open_date. Different from above case.
                if (open_date.after(c_date) && !YEAR_END.before(open_date)) {
                    return true;
                } else if (!YEAR_START.after(open_date) && !c_date_plus_twomonths.before(open_date)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void getNoItemText(String event_type, int childcount) {
        if (event_type != null) {
            if (childcount == 0) {
                    no_item_text.setVisibility(View.VISIBLE);
                    no_item_text.setText("There are currently no oppurtunities listed in " + event_type + " section.");
                    no_item_text.setPadding(100, 0, 100, 0);
            } else {
                no_item_text.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if (id == R.id.sign_out_btn) {
            Intent intent = new Intent(Main2Activity.this, LoginUIActivity.class);
            startActivity(intent);
            finish();

            if (FirebaseAuth.getInstance() != null) {
                FirebaseAuth.getInstance().signOut();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
