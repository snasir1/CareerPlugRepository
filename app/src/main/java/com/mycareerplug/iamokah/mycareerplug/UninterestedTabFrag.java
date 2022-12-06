package com.mycareerplug.iamokah.mycareerplug;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
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
public class UninterestedTabFrag extends Fragment {

    Context myContext;
    CustomListAdapter listAdapter;
    ListView listView;
    ViewPager viewPager;
    Activity myActivity;


    boolean viewCreated = false;


    ArrayList<CardModel> thrash_modelList = new ArrayList<>();

    ArrayList<CardModel> modelList = new ArrayList<>();

    public UninterestedTabFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uninterested_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        listView = view.findViewById(R.id.listviewid);
        viewPager = view.findViewById(R.id.viewPager);

        viewCreated = true;
        myActivity = this.getActivity();
        if (myContext != null && isAdded()) {
           /* TinyDB tinyDB = new TinyDB(myContext);
            thrash_modelList = tinyDB.getListObject("thrash_modelList_key", CardModel.class);
            thrash_modelList = removeDuplicates(thrash_modelList);

            setUpListView();
            listViewOnClick(); */

            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user").child("user_data").child(mAuth.getUid()).child("thrash").addListenerForSingleValueEvent(new ValueEventListener() {
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

        }


    }

    private void listViewOnClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((AppCompatActivity) getActivity()).getSupportActionBar()
                        .setDisplayHomeAsUpEnabled(true);
                setviewPager(position);
            }
        });
    }

    private void setviewPager(int position) {
        if (myContext != null && isAdded()) {
           // PagerAdapter pagerAdapter =  new pageAdapter(thrash_modelList, myContext);
            PagerAdapter pagerAdapter =  new pageAdapter(modelList, myContext);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(position);
            viewPager.setPadding(130, 50, 130, 50);
            viewPager.setPageMargin(50);


            listView.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);

        }

    }

    private void setUpListView() {

      /*  for (int i = 0 ; i < thrash_modelList.size(); i++) {
            instNameArray.add(thrash_modelList.get(i).getProgramname());
            thrash_Deadlines.add(thrash_modelList.get(i).getDeadline());
            thrash_programNames.add(thrash_modelList.get(i).getInstname());
        } */
       // System.out.println(thrash_programNames.toString());
        listAdapter = new CustomListAdapter(this.getActivity(), thrash_modelList);
        listView.setAdapter(listAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
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
         System.out.println("inside options");
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
        System.out.println("UninterestedTab onDetach");
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(false);
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("UNINTERESTEDfragment", "Fragment is visible.");
        } else {
            if (viewCreated) {
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar()
                            .setDisplayHomeAsUpEnabled(false);
                }
                listView.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                viewPager.setAdapter(null);
                Log.d("UNINTERESTEDfragment", "Fragment is not visible.");
            }
        }
    }

}
