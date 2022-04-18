package com.mycareerplug.iamokah.mycareerplug;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.CREATEPROFILE_PREF;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_GRADE_LEVEL;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_INTEREST;
import static com.mycareerplug.iamokah.mycareerplug.CreateProfile.PREF_STUDENT_TYPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {

    private static final String[] other_grade_level_array = {"6th Grade", "7th Grade", "8th Grade",
            "HS Freshman", "HS Sophomore", "HS Junior", "HS Senior",
            "College Freshman", "College Sophomore", "College Junior", "College Senior", "Graduate Student"};

    TextView no_item_text;
    ProgressBar progressBar;

    Context myContext;
    View fragmentView;

    String interest, grade, student_type;

    String instname, programname, deadline, cost, financialaid, pay, urlLink;
    String open_date, type_of_event, video_link;

    DatabaseReference mRef;

    private List<CardModel> modelList = new ArrayList<>();
    private List<CardModel> tempList = new ArrayList<>();
    ArrayList<Integer> interested_modelarray_pos = new ArrayList<>();

    ViewPager viewPager;
    pageAdapter pageAdapter;   //PAGER ADAPTER
    TabLayout tabLayout;

    String[] tabTitle={"Current","Upcoming","Past", "Planning"};
    int[] unreadCount={1, 99, 50, 4};
    int new_current = 0, new_upcoming = 0, old_current = 0, old_upcoming = 0;
    int current_counter = 0, upcoming_counter = 0;

    boolean oldFound = false;
    boolean oldFoundUpcoming = false;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set Toolbar title.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Discover");

        fragmentView = view;

        tabLayout = fragmentView.findViewById(R.id.tablayout);

        progressBar = fragmentView.findViewById(R.id.progress_bar);

        no_item_text = fragmentView.findViewById(R.id.noitemtext);

        mRef = FirebaseDatabase.getInstance().getReference();

        SharedPreferences settings = myContext.getSharedPreferences(CREATEPROFILE_PREF,
                Context.MODE_PRIVATE);

        interest = settings.getString(PREF_INTEREST, "");
        student_type = settings.getString(PREF_STUDENT_TYPE, "");
        grade = settings.getString(PREF_GRADE_LEVEL, "");


        //new FirebaseTask().execute(null, null, null);

        //createViewPager();
        setupTabLayout();


    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Current"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.addTab(tabLayout.newTab().setText("Planning"));

        showDialogForTab("Current");
        removeNotificationBadge(tabLayout.getTabAt(0));
        loadData("Current");



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              /*  if (tab.getText().toString().equals("Current")) {
                    getBadge(tab);
                } */
                removeNotificationBadge(tab);
                showDialogForTab(tab.getText().toString());
                loadData(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    private void removeNotificationBadge(final TabLayout.Tab tab) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                tab.setCustomView(null);
            }
        }, 7500);


    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.badged_tab,null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        int[] unreadCount={new_current, new_upcoming};
        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_count.getLayoutParams();
            if (unreadCount[pos] > 99) {
                tv_count.setTextSize(10);
              //  LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_count.getLayoutParams();
                params.height = getResources().getDimensionPixelSize(R.dimen.notification_text_view_height_changed);
                params.width = getResources().getDimensionPixelSize(R.dimen.notification_text_view_width_changed);
                tv_count.setLayoutParams(params);
                tv_count.setText(""+99 + "+");
            } else {
                tv_count.setTextSize(12);
                params.height = getResources().getDimensionPixelSize(R.dimen.notification_text_view_height_default);
                params.width = getResources().getDimensionPixelSize(R.dimen.notification_text_view_width_default);
                tv_count.setLayoutParams(params);
                tv_count.setText(""+unreadCount[pos]);
            }

        }
        else
            tv_count.setVisibility(View.GONE);

        System.out.println("finished tabs");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    public void createViewPager(List<CardModel> list, String getTime) {
        if (pageAdapter == null) {
            if (myContext != null && isAdded()) {
                pageAdapter = new pageAdapter(list, myContext);
                pageAdapter.notifyDataSetChanged();
                System.out.println("pageadapter count " + pageAdapter.getCount());
                getNoItemText(getTime, pageAdapter.getCount());
                viewPager = fragmentView.findViewById(R.id.viewPager);
                viewPager.setAdapter(pageAdapter);
                viewPager.setPadding(130, 50, 130, 50);
                viewPager.setPageMargin(50);
                progressBar.setVisibility(View.GONE);
            }

        } else {
            pageAdapter.notifyDataSetChanged();
            getNoItemText(getTime, pageAdapter.getCount());
            System.out.println("printing childs " + pageAdapter.getCount());
            viewPager.setAdapter(pageAdapter);
            progressBar.setVisibility(View.GONE);
        }

    }


    public void loadData(final String getTime) {
        System.out.println("GETCHECK IS " + getTime);
       // System.out.println("GET_NAV_ID IS " + get_nav_id);
        new_current = 0;
        new_upcoming = 0;


        if (!modelList.isEmpty()) {
            modelList.clear();
            if (pageAdapter != null) {
                pageAdapter.notifyDataSetChanged();
            }
        }
        progressBar.setVisibility(View.VISIBLE);
     //   List<CardModel> tempList = new ArrayList<>();
        //  String instname, programname, deadline, cost, financialaid, pay;
        // progressBar.setVisibility(View.VISIBLE);
        //  System.out.println("main2act pref interest value: " + interest + " gradelevel " + grade);
        mRef.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   System.out.println("children count " + dataSnapshot.getChildrenCount());
                int total = 0;
              //  final int current_counter = 0;
               // int upcoming_counter = 0,
                int past_counter = 0, upcoming_varies_counter = 0;
                String category, gradelevel;

               // Date last_user_login = new Date();
              //  System.out.println("First time printing last_user " + last_user_login);
                TinyDB tinyDB = new TinyDB(myContext);
                try {
                    Date last_user_login = tinyDB.getDate("last_time_logged_in", Date.class);
                } catch (NullPointerException e) {
                    tinyDB.putBoolean("first_time_login", true);
                    lastTimeUserLoggedIn();
                }
                Date last_user_login = tinyDB.getDate("last_time_logged_in", Date.class);
                /*boolean first_time_login = tinyDB.getBoolean("first_time_login");
                if (last_user_login == null) {
                    System.out.println("FIRST TIME USER IS EVER LOGGING IN");
                }
                System.out.println("last_user first get val" + last_user_login); */



                //outerloop:
                for (int i = 0; i < dataSnapshot.child("event_list").getChildrenCount(); i++) {
                    try {
                        category = dataSnapshot.child("event_list").child(Integer.toString(i)).child("CATEGORY").getValue().toString();
                        gradelevel = dataSnapshot.child("event_list").child(Integer.toString(i)).child("Grade Level").getValue().toString();
                        instname = dataSnapshot.child("event_list").child(Integer.toString(i)).child("INSTITUTION NAME").getValue().toString();
                        //System.out.println("instname " + instname);
                        programname = dataSnapshot.child("event_list").child(Integer.toString(i)).child("PROGRAM NAME").getValue().toString();
                        deadline = dataSnapshot.child("event_list").child(Integer.toString(i)).child("DEADLINE").getValue().toString();
                        cost = dataSnapshot.child("event_list").child(Integer.toString(i)).child("COST").getValue().toString();
                        financialaid = dataSnapshot.child("event_list").child(Integer.toString(i)).child("FINANCIAL AID AVAILABLE").getValue().toString();
                        pay = dataSnapshot.child("event_list").child(Integer.toString(i)).child("PAY").getValue().toString();
                        urlLink = dataSnapshot.child("event_list").child(Integer.toString(i)).child("LINK TO PROGRAM WEBSITE").getValue().toString();
                        open_date = dataSnapshot.child("event_list").child(Integer.toString(i)).child("APPLICATION OPEN DATE").getValue().toString();
                        type_of_event = dataSnapshot.child("event_list").child(Integer.toString(i)).child("TYPE OF EVENT").getValue().toString();
                        video_link = dataSnapshot.child("event_list").child(Integer.toString(i)).child("VIDEO LINKS FOR PICTURE").getValue().toString();
                    } catch (NullPointerException e) {
                        continue;
                    }
                    //for eventlist
                //    dataSnapshot.child("event_list").child(Integer.toString(i)).child("CATEGORY").getValue().toString();

                    //for user_data equal to the for loop value.
                 //   dataSnapshot.child("user_data").child(myAuth.getUid()).child("old_values").getChildrenCount();


                    CardModel model = new CardModel(instname, programname, deadline, cost,
                            financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Current", category, true);

                  /*  int skipped_counter = 0;
                    TinyDB tinyDB = new TinyDB(myContext);
                    ArrayList<CardModel> thrash_modelList =
                            tinyDB.getListObject("thrash_modelList_key", CardModel.class);

                    for (int k = 0; k < thrash_modelList.size(); k++) {
                        if (i == thrash_modelList.get(k).array_pos) {
                            skipped_counter++;
                            System.out.println("Skipping pos " + thrash_modelList.get(k).array_pos + " name " + thrash_modelList.get(k).getProgramname());
                            continue outerloop;
                        }
                    }
                    System.out.println("Total skipped " + skipped_counter + " thrashlist size " + thrash_modelList.size());*/


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

                 //   checkCatagory(category);

                    if (getTime.equals("Interested")) {
                        if (myContext != null && isAdded()) {
                             tinyDB = new TinyDB(myContext);
                            interested_modelarray_pos = tinyDB.getListInt("interested_arraypos_key");
                            if (interested_modelarray_pos.contains(i)) {
                                modelList.add(new CardModel(instname, programname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Interested", category, false));
                            }
                        }
                    }

                    if (getTime.equals("Planning")) {
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
                        //System.out.println("glevel " + glevel);
                        test = getPlanning(glevel);
                        if (test != null) {
                         //   System.out.println("printing test1 " + test[0] + " test2 " + test[1]);
                         //   System.out.println("printing gradelevel.contains " + gradelevel);
                        }

                        if (!glevel.equals("Graduate Student") && test != null) {
                            if (checkCatagory(category)
                                    && (gradelevel.contains(test[0]) || gradelevel.contains(test[1]))
                                    && filterEvent(type_of_event)) {
                                modelList.add(new CardModel(instname, programname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Planning", category, false));
                            }
                        }

                    }

                    if (checkCatagory(category) && gradeLevel(student_type, grade, gradelevel)
                            && filterEvent(type_of_event)) {
                        //      System.out.println("main adding index " + i + " to array");
                        //     Log.v(TAG, "main adding index " + i + " to array");
                        //   mfilteredArray.add(i);
                        if (getTime.equals("Current")) {
                            if (isCurrent(open_date, deadline, c_date)) {

                               // System.out.println("PRINTING MODEL " + model.getInstname());

                                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                               // DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

                                for (int loop = 0; loop < dataSnapshot.child("user_data").child(myAuth.getUid()).child("old_values").getChildrenCount(); loop++) {
                                    CardModel dataModel = dataSnapshot.child("user_data")
                                            .child(myAuth.getUid()).child("old_values")
                                            .child(Integer.toString(loop)).getValue(CardModel.class);
                                    if (model.equals(dataModel)) {
                                        oldFound = true;
                                        System.out.println("FOUND OLD VALUE" + model.getInstname());
                                           /*     model.set_is_new(false);
                                                modelList.add(model);
                                                current_counter++;
                                                System.out.println("FOUND OLD VALUE"); */
                                        break;
                                    }
                                }

                                System.out.println("oldfound Val " + oldFound);
                                if (oldFound == true) {
                                    model.set_is_new(false);
                                    modelList.add(model);
                                    current_counter++;
                                    oldFound = false;
                                    System.out.println("FOUND OLD VALUE");
                                } else {
                                    model.set_is_new(true);
                                    modelList.add(model);
                                    current_counter++;
                                    new_current++;

                                    //  System.out.println("ADDED and incremented to add");
                                }

                             /*   myref.child("user").child("user_data").child(myAuth.getUid()).child("old_values").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                                            //String inst = dataSnapshot.child("event_list").child(Integer.toString(i)).child("instname").getValue().toString();
                                            //
                                            // String programname = dataSnapshot.child("event_list").child(Integer.toString(i)).child("programname").getValue().toString();
                                            CardModel dataModel = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                                            System.out.println("ModelList Size " + modelList.size());
                                            if (model.equals(dataModel)) {
                                                oldFound = true;
                                                System.out.println("FOUND OLD VALUE" + model.getInstname());
                                           /*     model.set_is_new(false);
                                                modelList.add(model);
                                                current_counter++;
                                                System.out.println("FOUND OLD VALUE");
                                                break;
                                            }

                                       //     System.out.println("REMOVED " + modelList.get(i).getProgramname());
                                       //     System.out.println("ModelList Size AFTER" + modelList.size());
                                            // System.out.println("DATAsnapshot REMOVETHRASH  " + dataSnapshot.child("event_list").child(Integer.toString(i)).getValue());
                                            // System.out.println("Modellist first item " + modelList.get(0).toString());
                                        }
                                   //     System.out.println("about to add");
                                        //Not found in old_value list. Must be a new record.
                                 /*       model.set_is_new(true);
                                        modelList.add(model);
                                        current_counter++;
                                        new_current++;
                                        if (pageAdapter != null) {
                                            pageAdapter.notifyDataSetChanged();
                                        }
                                        System.out.println("ADDED and incremented to add");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }); */


                            }

                            if (open_date.equals("Varies") || compareDate2(c_date, addMonths(c_date, 2), o_date)) {
                                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                                // DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

                                for (int k = 0; k < dataSnapshot.child("user_data").child(myAuth.getUid()).child("old_values").getChildrenCount(); k++) {
                                    CardModel dataModel = dataSnapshot.child("user_data")
                                            .child(myAuth.getUid()).child("old_values")
                                            .child(Integer.toString(k)).getValue(CardModel.class);
                                    if (model.equals(dataModel)) {
                                        oldFoundUpcoming = true;
                                        System.out.println("FOUND OLD VALUE" + model.getInstname());
                                           /*     model.set_is_new(false);
                                                modelList.add(model);
                                                current_counter++;
                                                System.out.println("FOUND OLD VALUE"); */
                                        break;
                                    }
                                }

                                System.out.println("oldfound Val " + oldFound);
                                if (oldFoundUpcoming == true) {
                                    //model.set_is_new(false);
                                    //modelList.add(model);
                                    upcoming_counter++;
                                    oldFoundUpcoming = false;
                                    System.out.println("FOUND OLD VALUE");
                                } else {
                                    //model.set_is_new(true);
                                    //modelList.add(model);
                                    upcoming_counter++;
                                    new_upcoming++;

                                    //  System.out.println("ADDED and incremented to add");
                                }

                            }

                        } else if (getTime.equals("Upcoming")) {
                         //   System.out.println("Open_date " + open_date);
                            if (open_date.equals("Varies") || compareDate2(c_date, addMonths(c_date, 2), o_date)) {
                                if (open_date.equals("Varies")) {
                                    upcoming_varies_counter++;
                                }

                                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                                // DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

                                for (int k = 0; k < dataSnapshot.child("user_data").child(myAuth.getUid()).child("old_values").getChildrenCount(); k++) {
                                    CardModel dataModel = dataSnapshot.child("user_data")
                                            .child(myAuth.getUid()).child("old_values")
                                            .child(Integer.toString(k)).getValue(CardModel.class);
                                    if (model.equals(dataModel)) {
                                        oldFoundUpcoming = true;
                                        System.out.println("FOUND OLD VALUE" + model.getInstname());
                                           /*     model.set_is_new(false);
                                                modelList.add(model);
                                                current_counter++;
                                                System.out.println("FOUND OLD VALUE"); */
                                        break;
                                    }
                                }

                                System.out.println("oldfound Val " + oldFound);
                                if (oldFoundUpcoming == true) {
                                    model.set_is_new(false);
                                    modelList.add(model);
                                    upcoming_counter++;
                                    oldFoundUpcoming = false;
                                    System.out.println("FOUND OLD VALUE");
                                } else {
                                    model.set_is_new(true);
                                    modelList.add(model);
                                    upcoming_counter++;
                                    new_upcoming++;

                                    //  System.out.println("ADDED and incremented to add");
                                }

                            /*    modelList.add(new CardModel(instname, programname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Upcoming", category, false));
                                upcoming_counter++;
                                //  System.out.println("UPCOMING val " + i); */
                            }
                            //TO COUNT # OF NEW ITEMS FOR CURRENT TO SHOW IN TAB.
                            if (isCurrent(open_date, deadline, c_date)) {
                                FirebaseAuth myAuth = FirebaseAuth.getInstance();
                                // DatabaseReference myref = FirebaseDatabase.getInstance().getReference();

                                for (int k = 0; k < dataSnapshot.child("user_data").child(myAuth.getUid()).child("old_values").getChildrenCount(); k++) {
                                    CardModel dataModel = dataSnapshot.child("user_data")
                                            .child(myAuth.getUid()).child("old_values")
                                            .child(Integer.toString(k)).getValue(CardModel.class);
                                    if (model.equals(dataModel)) {
                                        oldFoundUpcoming = true;
                                        System.out.println("FOUND OLD VALUE" + model.getInstname());
                                           /*     model.set_is_new(false);
                                                modelList.add(model);
                                                current_counter++;
                                                System.out.println("FOUND OLD VALUE"); */
                                        break;
                                    }
                                }

                                System.out.println("oldfound Val " + oldFound);
                                if (oldFoundUpcoming == true) {
                                    //model.set_is_new(false);
                                    //modelList.add(model);
                                    current_counter++;
                                    oldFoundUpcoming = false;
                                    System.out.println("FOUND OLD VALUE");
                                } else {
                                    //model.set_is_new(true);
                                    //modelList.add(model);
                                    current_counter++;
                                    new_current++;

                                    //  System.out.println("ADDED and incremented to add");
                                }

                            }



                        } else if (getTime.equals("Past") || getTime.equals("past opportunities")) {
                            if (isCurrent(open_date, deadline, c_date) == false &&
                                    !(open_date.equals("Varies") || compareDate2(c_date, addMonths(c_date, 2), o_date))) {
                                past_counter++;
                              //  System.out.println("PAST val " + i);
                                modelList.add(new CardModel(instname, programname, deadline, cost,
                                        financialaid, pay, urlLink, open_date, type_of_event, video_link, i, "Past", category, false));
                            }
                        }



                        total++;
                    }

                }
                tinyDB.putBoolean("first_time_login", false);
                if (isAdded()) {
                    if (tabLayout.getTabAt(0).getCustomView() == null) {
                        tabLayout.getTabAt(0).setCustomView(prepareTabView(0));
                    }

                    if (tabLayout.getTabAt(1).getCustomView() == null) {
                        tabLayout.getTabAt(1).setCustomView(prepareTabView(1));
                    }
                }


                System.out.println("Total count is " + total);
                System.out.println("current counter is " + current_counter + " current_new is " + new_current);
                System.out.println("upcoming_counter " + upcoming_counter + " new_upcoming is " + new_upcoming);
                System.out.println("past_counter " + past_counter);
              //  System.out.println("New current_counter " + new_current + " new_upcoming counter " + new_upcoming);
               // TinyDB tinyDB = new TinyDB(myContext);
                ArrayList<CardModel> thrash_modelList =
                        tinyDB.getListObject("thrash_modelList_key", CardModel.class);
                if (pageAdapter == null) {
                    removeThrash(modelList);
                    System.out.println("CREATING PAGEADAPTER. Modelist Size " + modelList.size());
                    pageAdapter = new pageAdapter(modelList, myContext);
                    pageAdapter.notifyDataSetChanged();
                    System.out.println("pageadapter count " + pageAdapter.getCount());
                    getNoItemText(getTime, pageAdapter.getCount());
                    viewPager = fragmentView.findViewById(R.id.viewPager);
                    viewPager.setAdapter(pageAdapter);
                    viewPager.setPadding(130, 50, 130, 50);
                    viewPager.setPageMargin(50);
                    progressBar.setVisibility(View.GONE);
                    System.out.println("NULL PAGE ADAPTER");
                } else {
                    removeThrash(modelList);
                    pageAdapter.notifyDataSetChanged();
                    getNoItemText(getTime, pageAdapter.getCount());
                    System.out.println("printing childs " + pageAdapter.getCount());
                    viewPager.setAdapter(pageAdapter);
                    progressBar.setVisibility(View.GONE);
                    System.out.println("ELSE PAGE ADAPTER");
                }

                System.out.println("INSIDE");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        System.out.println("OUTSIDE");
    }

    private void viewPageListener(ViewPager viewPager) {
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void removeThrash(final List<CardModel> modelList) {
        System.out.println("START removeThrash");
        /*
        if (thrashList != null && !thrashList.isEmpty()) {
            for (int i = 0; i < thrashList.size(); i++) {
                if (modelList.contains(thrashList.get(i))) {
                    modelList.remove(thrashList.get(i));
                }
            }
        } */


        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user").child("user_data").child(mAuth.getUid()).child("thrash").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    //String inst = dataSnapshot.child("event_list").child(Integer.toString(i)).child("instname").getValue().toString();
                    //
                    // String programname = dataSnapshot.child("event_list").child(Integer.toString(i)).child("programname").getValue().toString();
                    CardModel model = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                    System.out.println("ModelList Size " + modelList.size());
                    if (modelList.contains(model)) {
                        modelList.remove(model);
                        if (pageAdapter != null) {
                            pageAdapter.notifyDataSetChanged();
                        }
                        System.out.println("REMOVED " + modelList.get(i).getProgramname());
                    }
                    System.out.println("ModelList Size AFTER" + modelList.size());
                   // System.out.println("DATAsnapshot REMOVETHRASH  " + dataSnapshot.child("event_list").child(Integer.toString(i)).getValue());
                   // System.out.println("Modellist first item " + modelList.get(0).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();

            }
        }, 5000);   //5 seconds
        System.out.println("ModelList Size WHEN DONE" + modelList.size());
        System.out.println("END removeThrash"); */

    }

    private boolean isCurrent(String open_date, String deadline_date, Date c_date) {

        Date o_date = stringToDate(open_date);
        Date d_date = stringToDate(deadline_date);
       // System.out.println("odate " + open_date + " o_date_date " + o_date  );
        if (open_date != null && deadline_date != null && c_date != null) {
            if (open_date.isEmpty() && !deadline_date.isEmpty()) {
                if (compareDate(subtractMonths(d_date, 3), d_date, c_date)) {
                    // System.out.println("deadline empty " + i);
                    return true;
                }

            } else if (!open_date.isEmpty() && deadline_date.isEmpty()) {
                if (compareDate(o_date, addMonths(o_date, 3), c_date)) {
                    // System.out.println("deadline empty " + i);
                    return true;
                }
            } else if (!open_date.isEmpty() && !deadline_date.isEmpty()) {
                if (compareDate(o_date, d_date, c_date)) {
                    return true;
                }
            }  else if (open_date.isEmpty() && deadline_date.isEmpty()) {
                //   System.out.println("both empty " + i);
                return true;
            }
        }

        return false;
    }

    private boolean filterEvent(String eventval) {
        //  Log.d(TAG, "filterEvent: bottomid " + bottomid);
        //  Log.d(TAG, "filterEvent: eventval " + eventval);

        /*

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
        */
        if (myContext != null && isAdded()) {
            TinyDB tinyDB = new TinyDB(myContext);
            ArrayList<String> get_filter_checklist = tinyDB.getListString("filter_checklist");
            String[] filterItems = getResources().getStringArray(R.array.filter_list);
            if (get_filter_checklist == null || get_filter_checklist.isEmpty()) {
                get_filter_checklist = new ArrayList(Arrays.asList(filterItems));
                System.out.println("get_filter is null " + get_filter_checklist.toString());
                tinyDB.putListString("filter_checklist", get_filter_checklist);

                for(int i=0;i<get_filter_checklist.size();i++){
                    System.out.println(get_filter_checklist.get(i));
                }


                for (int i = 0; i < get_filter_checklist.size(); i++) {
                    if (eventval.contains(get_filter_checklist.get(i))) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < get_filter_checklist.size(); i++) {
                    if (eventval.contains(get_filter_checklist.get(i))) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    private boolean checkCatagory(String catagory) {
        if (myContext != null && isAdded()) {
            TinyDB tinyDB = new TinyDB(myContext);
            ArrayList<String> get_interest_array = tinyDB.getListString("interestArray");

    /*    for (int i = 0; i < get_interest_array.size(); i++) {
            System.out.println("PRINTING INTEREST ARRAY " + i + " val" + get_interest_array.get(i));
        } */

            for (int i = 0; i < get_interest_array.size(); i++) {
                // System.out.println("Catagory is " + catagory);
                // System.out.println("getInterestVal " + get_interest_array.get(i));
                if (catagory.contains(get_interest_array.get(i))) {
                    //System.out.println("return true ");
                    return true;
                } else {
                    //   System.out.println("return false ");
                }
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

        if (date_start != null && date_end != null && current_date != null) {
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

   /* private static Date addTwoMonths(Date start) {
        // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        month++;
        // System.out.println("printing month " + month);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (month == 11) {
            month = 1;
        } else if (month == 12) {
            month = 2;
        } else {
            month += 2;
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    } */

    private static Date addMonths(Date start, int numMonths) {
        // System.out.println("add2months date input = " + start);
        if (start != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int month = cal.get(Calendar.MONTH);
            month++;
            //  System.out.println("printing month " + month + " printing numMonths " + numMonths);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            month += numMonths;
            //   System.out.println("printing mont+numMonth " + month);
            //10 + 3 = 13.  12 = december.
            if (month > 12) {
                month = month - 12; //13 - 12 = 1
                // System.out.println("month - 12 " + month);
            }

            return stringToDate(month + "/" + String.format("%02d", day));
        } else {
            return null;
        }

    }

    private static Date subtractMonths(Date start, int numMonths) {
        // System.out.println("add2months date input = " + start);
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(Calendar.MONTH);
        month++;
        System.out.println("printing month " + month + " printing numMonths " + numMonths);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month -= numMonths;
        System.out.println("printing mont-numMonth " + month);
        //10 + 3 = 13.  12 = december.
        if (month < 1) {
            month = 12 - Math.abs(month); //13 - 12 = 1
            System.out.println("12 - month " + month);
        }

        return stringToDate(month + "/" + String.format("%02d", day));
    }


    private static boolean compareDate2(Date c_date, Date c_date_plus_twomonths, Date open_date) {
        Date YEAR_START = stringToDate("1-Jan"),
                YEAR_END = stringToDate("31-Dec");

        if (c_date != null && c_date_plus_twomonths != null && open_date != null) {
           /* System.out.println("c_date(before) " + c_date.before(c_date_plus_twomonths));
            System.out.println("c_date (after) open " + c_date.after(open_date));
            System.out.println("c_date_plus_twomonths " + !c_date_plus_twomonths.before(open_date)); */
            if ((open_date.compareTo(c_date_plus_twomonths) <= 0 && open_date.compareTo(c_date) > 0) && !c_date_plus_twomonths.before(open_date)) {
                return true;
            } else if (c_date.after(c_date_plus_twomonths)) {
                System.out.println("compare2 else if");
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

    private String getPlanningStrings(int stringNum) {
        String returnString = null;
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
        //System.out.println("glevel " + glevel);
        test = getPlanning(glevel);
        if (test != null) {
            if (stringNum == 0) {
                returnString = test[0];
            } else if (stringNum == 1) {
                returnString = test[1];
            }
            if (returnString.contains("HS")) {
                returnString = returnString.replace("HS", "High School");
            }
        }

        return returnString;
    }

    private void showDialogForTab(String tabName) {
        System.out.println("Printing tabName");
        String prefName = tabName + "Dialog";
        if (tabName.equals("Current")) {
            System.out.println("Inside current dialog");
            CharSequence title = "About Current";
            CharSequence message = "The current tab shows current opportunities which you can apply to right now. \n" +
                    "To learn more about an opportunity just hit 'View details'";

            showCancelDialog(title, message, prefName);
        } else if (tabName.equals("Upcoming")) {
            CharSequence title = "About Upcoming";
            CharSequence message = "The upcoming tab shows upcoming opportunities that will be available within the next two months.";
            showCancelDialog(title, message, prefName);
        } else if (tabName.equals("Past")) {
            CharSequence title = "About Past";
            CharSequence message = "The past tab shows past opportunities that have already passed this year.";
            showCancelDialog(title, message, prefName);
        } else if (tabName.equals("Planning")) {
            CharSequence title = "About Planning";
            if (getPlanningStrings(0) == null || getPlanningStrings(1) == null) {
                CharSequence message = "The planning tab shows you opportunities for the next two years.";
                showCancelDialog(title, message, prefName);
            } else {
                CharSequence message = "The planning tab will show you opportunities for the next two years. \n"
                        + "For you, it will show you " + getPlanningStrings(0) +
                        " and " + getPlanningStrings(1) + " opportunities to help you plan for the future.";
                showCancelDialog(title, message, prefName);
            }

        }

    }



    private void showCancelDialog(CharSequence title, CharSequence message, final String prefName) {
        System.out.println("start showcancel");
        //  isConfirmed = false;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(myContext);
        LayoutInflater adbInflater = LayoutInflater.from(myContext);
        View mView = adbInflater.inflate(R.layout.checkbox, null);
        final CheckBox dontShowAgain = mView.findViewById(R.id.skip);
        final HomeActivity activity = (HomeActivity) myContext;
        SharedPreferences settings = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        String skipMessage = settings.getString("skipMessage", "NOT checked");

        mBuilder.setTitle(title);
        mBuilder.setMessage(message);
        mBuilder.setView(mView);

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";

                if (dontShowAgain.isChecked()) {
                    checkBoxResult = "checked";
                }

                SharedPreferences settings = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("skipMessage", checkBoxResult);
                editor.commit();

                // Do what you want to do on "OK" action
                //     isConfirmed = true;
                System.out.println("inside thrashbtn");


                dialog.dismiss();
                System.out.println("End of yes btn.");
            }
        });

       /* mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";

                if (dontShowAgain.isChecked()) {
                    checkBoxResult = "checked";
                }

                SharedPreferences settings = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("skipMessage", checkBoxResult);
                editor.commit();

                // Do what you want to do on "CANCEL" action
               // isConfirmed = false;
            }
        }); */

        if (!skipMessage.equals("checked")) {
            mBuilder.show();
        }
       // System.out.println("before isConfirmed return " + isConfirmed);

    }
    private void lastTimeUserLoggedIn() {
        System.out.println("printing in lasttime");
        TinyDB tinyDB = new TinyDB(myContext);
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        Date c_date = stringToDate(dateFormat.format(date));
        System.out.println("printing c_date " + c_date);
        c_date = subtractMonths(c_date, 3);
        System.out.println("printing c_date 3 months before" + c_date);
        tinyDB.putDate("last_time_logged_in", c_date);

    }

    @Override
    public void onPause() {
        super.onPause();
        //System.out.println("running");
       // lastTimeUserLoggedIn();
    }

    private class FirebaseTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            System.out.println("Inside PRE-EXECUTE");

          /*  Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                }
            }, 1500);*/
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //  System.out.println("INSIDE background");

            loadData("current");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Inside POST-EXECUTE");
            super.onPostExecute(aVoid);
            //   progressBar.setVisibility(View.GONE);
        }
    }
}



