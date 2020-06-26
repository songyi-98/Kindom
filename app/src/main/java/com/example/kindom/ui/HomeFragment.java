package com.example.kindom.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kindom.R;
import com.example.kindom.ui.home.HomeImagePagerAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize images
        ArrayList<Integer> neighbourhoodImages = new ArrayList<>();
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_1);
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_2);

        ArrayList<Integer> localImages = new ArrayList<>();
        localImages.add(R.drawable.local_sample_image_1);
        localImages.add(R.drawable.local_sample_image_2);

        // Initialize slider dots panel
        final LinearLayout neighbourhoodSliderDotsPanel= view.findViewById(R.id.home_neighbourhood_news_slider);
        final int neighbourhoodDotsCount = neighbourhoodImages.size();
        final ImageView[] neighbourhoodDots = new ImageView[neighbourhoodDotsCount];
        for (int i = 0; i < neighbourhoodDotsCount; i++) {
            neighbourhoodDots[i] = new ImageView(view.getContext());
            if (i == 0) {
                neighbourhoodDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_active));
            } else {
                neighbourhoodDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_non_active));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            neighbourhoodSliderDotsPanel.addView(neighbourhoodDots[i], params);
        }

        LinearLayout localSliderDotsPanel = view.findViewById(R.id.home_local_news_slider);
        final int localDotsCount = localImages.size();
        final ImageView[] localDots = new ImageView[localDotsCount];
        for (int i = 0; i < localDotsCount; i++) {
            localDots[i] = new ImageView(view.getContext());
            if (i == 0) {
                localDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_active));
            } else {
                localDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_non_active));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            localSliderDotsPanel.addView(localDots[i], params);
        }

        // Initialize view pagers and adapters
        ViewPager neighbourhoodViewPager = view.findViewById(R.id.home_neighbourhood_news_view_pager);
        HomeImagePagerAdapter neighbourhoodAdapter = new HomeImagePagerAdapter(view.getContext(), neighbourhoodImages);
        neighbourhoodViewPager.setAdapter(neighbourhoodAdapter);

        ViewPager localViewPager = view.findViewById(R.id.home_local_news_view_pager);
        HomeImagePagerAdapter localAdapter = new HomeImagePagerAdapter(view.getContext(), localImages);
        localViewPager.setAdapter(localAdapter);

        // Set page change listeners
        neighbourhoodViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < neighbourhoodDotsCount; i++) {
                    if (i == position) {
                        neighbourhoodDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_active));
                    } else {
                        neighbourhoodDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_non_active));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });

        localViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < localDotsCount; i++) {
                    if (i == position) {
                        localDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_active));
                    } else {
                        localDots[i].setImageDrawable(view.getContext().getDrawable(R.drawable.slider_dot_non_active));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
    }
}