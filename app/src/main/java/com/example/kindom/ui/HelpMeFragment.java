package com.example.kindom.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.ui.helpMe.HelpMePagerAdapter;
import com.example.kindom.utils.FirebaseHandler;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HelpMeFragment extends Fragment {

    public HelpMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViewPager(view);
    }

    /**
     * Initialize view pager
     *
     * @param view the view
     */
    private void initializeViewPager(final View view) {
        // Create an instance of the tab layout from the view
        final TabLayout tabLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.help_me_tab_layout);

        // Initialize tabs
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseHandler.getCurrentUserUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        boolean isAdmin = user.getUserGroup().equals(User.USER_GROUP_ADMIN);

                        if(tabLayout.getTabCount() < 2) {
                            // Set text for each tab
                            tabLayout.addTab(tabLayout.newTab().setText(R.string.help_me_all_listing_tab));
                            if (isAdmin) {
                                // Add Reported Listing tab for admin
                                tabLayout.addTab(tabLayout.newTab().setText(R.string.help_me_reported_listing_tab));
                            } else {
                                // Add My Listing tab for user
                                tabLayout.addTab(tabLayout.newTab().setText(R.string.help_me_my_listing_tab));
                            }
                        }

                        // Use PagerAdapter to manage page views in fragments.
                        // Each page is represented by its own fragment.
                        final ViewPager viewPager = view.findViewById(R.id.help_me_pager);
                        if (isAdded()) {
                            final HelpMePagerAdapter adapter = new HelpMePagerAdapter(
                                    getChildFragmentManager(),
                                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                    tabLayout.getTabCount(),
                                    isAdmin);
                            viewPager.setAdapter(adapter);
                        }

                        // Sett page change listener
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {
                                // Do nothing
                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {
                                // Do nothing
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Do nothing
                    }
                });
    }
}