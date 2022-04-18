package com.mycareerplug.iamokah.mycareerplug;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class InterestsFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    InterestPagerAdapter interestPagerAdapter;

    public InterestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interests, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set Toolbar title.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Interests");

        tabLayout = view.findViewById(R.id.interest_tab_layout);
        viewPager = view.findViewById(R.id.interest_pager);

        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Interested".toUpperCase()), true);
        tabLayout.addTab(tabLayout.newTab().setText("Not Interested".toUpperCase()));

        interestPagerAdapter = new InterestPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(interestPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
