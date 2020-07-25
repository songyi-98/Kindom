package com.example.kindom.ui.helpMe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HelpMePagerAdapter extends FragmentStatePagerAdapter {

    final int mNumOfTabs;
    private final boolean mIsAdmin;

    public HelpMePagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs, boolean isAdmin) {
        super(fm, behavior);
        mNumOfTabs = numOfTabs;
        mIsAdmin = isAdmin;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HelpMeAllListingFragment();
        } else if (position == 1 && mIsAdmin) {
            return new HelpMeReportedListingFragment();
        } else {
            return new HelpMeUserListingFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}