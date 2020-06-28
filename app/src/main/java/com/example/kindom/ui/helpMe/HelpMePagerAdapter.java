package com.example.kindom.ui.helpMe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HelpMePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HelpMePagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs) {
        super(fm, behavior);
        mNumOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new HelpMeAllListingFragment();
            case 1: return new HelpMeUserListingFragment();
            default: return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}