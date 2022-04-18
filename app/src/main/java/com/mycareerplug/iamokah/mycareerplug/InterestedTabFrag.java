package com.mycareerplug.iamokah.mycareerplug;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestedTabFrag extends Fragment {

    Context myContext;
    CustomListAdapter listAdapter;
    ListView listView;
    CustomViewPager viewPager;
    Activity myActivity;

    boolean viewCreated = false;
    int viewPagerPoition = 0;

    ArrayList<CardModel> interested_modelList = new ArrayList<>();

    ArrayList<CardModel> modelList = new ArrayList<>();

    public InterestedTabFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interested_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        listView = view.findViewById(R.id.listviewid);
        viewPager = view.findViewById(R.id.customViewPager);

        viewCreated = true;
        myActivity = this.getActivity();
        if (myContext != null && isAdded()) {

            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user").child("user_data").child(mAuth.getUid()).child("interested").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                        //String inst = dataSnapshot.child(Integer.toString(i)).child("instname").getValue().toString();
                        //
                        // String programname = dataSnapshot.child(Integer.toString(i)).child("programname").getValue().toString();
                        CardModel model = dataSnapshot.child(Integer.toString(i)).getValue(CardModel.class);
                    //    System.out.println("ModelList Size " + modelList.size());
                        modelList.add(model);
                        System.out.println("ModelList Size AFTER" + modelList.size());
                        // System.out.println("DATAsnapshot REMOVETHRASH  " + dataSnapshot.child(Integer.toString(i)).getValue());
                        // System.out.println("Modellist first item " + modelList.get(0).toString());
                    }

                    listAdapter = new CustomListAdapter(myActivity, modelList);
                    listView.setAdapter(listAdapter);

                    listViewOnClick();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

          //  setUpListView();
          //  listViewOnClick();

            /*
            TinyDB tinyDB = new TinyDB(myContext);
            interested_modelList = tinyDB.getListObject("interested_modelList_key", CardModel.class);
            interested_modelList = removeDuplicates(interested_modelList);


            setUpListView();
            listViewOnClick(); */
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    private void listViewOnClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                setViewPager(position);
            }
        });
    }

    private void setViewPager(int position) {
        if (myContext != null && isAdded()) {
         //   PagerAdapter pagerAdapter =  new pageAdapter(interested_modelList, myContext);
            PagerAdapter pagerAdapter =  new pageAdapter(modelList, myContext);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(position);
            viewPager.setPadding(130, 50, 130, 50);
            viewPager.setPageMargin(50);

            listView.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }


       // checkCurrentViewPage(viewPager);
    }

    private void checkCurrentViewPage(final CustomViewPager viewPager) {
      //  viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.right);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
               /* System.out.println("printing pageselected i" + i);
                System.out.println("total size " + viewPager.getAdapter().getCount());
                if (i == (viewPager.getAdapter().getCount() - 1)) {
                    System.out.print("inside allowedswipedirection");
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                } else {
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                } */
            }

            @Override
            public void onPageSelected(int i) {
              //  System.out.println("printing pageselected i" + i);
             //   System.out.println("total size " + viewPager.getAdapter().getCount());
                viewPagerPoition = i;
                int getLastItem = (viewPager.getAdapter().getCount() - 1) - 3;
                int currentItem = viewPager.getCurrentItem();
                System.out.println("printing currentItem " + currentItem);
                if (getLastItem == (viewPager.getAdapter().getCount() - 1)) {
                    System.out.print("inside allowedswipedirection");
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                } else {
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                }
              /*  if (i == (viewPager.getAdapter().getCount() - 1)) {
                    System.out.print("inside allowedswipedirection");
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                } else {
                    viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                } */

              //  viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.right);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
              /*  if (i == ViewPager.SCROLL_STATE_DRAGGING) {
                    System.out.println("inside scroll if " + viewPagerPoition);
                    if (viewPagerPoition == (viewPager.getAdapter().getCount()-1)) {
                        System.out.println("inside pos == if " + viewPagerPoition);
                        viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.left);
                    } else {
                        viewPager.setAllowedSwipeDirection(CustomViewPager.SwipeDirection.all);
                    }
                }*/
            }
        });
    }

    private void setUpListView() {

      /*  for (int i = 0 ; i < interested_modelList.size(); i++) {
            instNameArray.add(interested_modelList.get(i).getProgramname());
            interested_Deadline.add(interested_modelList.get(i).getDeadline());
            programNamesList.add(interested_modelList.get(i).getInstname());
            open_dates.add(interested_modelList.get(i).getInstname());
        } */
       // programNamesList = tinyDB.getListString("interested_program_keyz");
       // interested_Deadline = tinyDB.getListString("interested_deadline_keyz");

        listAdapter = new CustomListAdapter(this.getActivity(), interested_modelList);
        listView.setAdapter(listAdapter);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // System.out.println("inside options");
        switch (item.getItemId()) {
            case android.R.id.home:
              //  System.out.println("inside switch");
                listView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                viewPager.setAdapter(null);
                ((AppCompatActivity) getActivity()).getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDetach() {
        System.out.println("InterestedTabFrag onDetach");
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(false);
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            Log.d("INTERESTEDFRGFAMENT", "Fragment is visible.");
        } else {
            if (viewCreated) {
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar()
                            .setDisplayHomeAsUpEnabled(false);
                }
                listView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                viewPager.setAdapter(null);
                Log.d("INTERESTEDFRGFAMENT", "Fragment is not visible.");
            }
        }
    }

}
