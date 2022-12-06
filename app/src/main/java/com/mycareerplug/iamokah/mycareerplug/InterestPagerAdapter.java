package com.mycareerplug.iamokah.mycareerplug;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class InterestPagerAdapter extends FragmentPagerAdapter {

    int numTabs;

    public InterestPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        numTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        System.out.println("INSIDE GETITEM ");
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new InterestedTabFrag();
                break;
            case 1:
                fragment = new UninterestedTabFrag();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Override
    public void destroyItem(ViewGroup viewPager, int position, Object object) {
        viewPager.removeView((View) object);
    }

}
