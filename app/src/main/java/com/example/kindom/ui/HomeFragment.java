package com.example.kindom.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize images
        ArrayList<Integer> neighbourhoodImages = new ArrayList<>();
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_1);
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_2);
        ArrayList<Integer> localImages = new ArrayList<>();
        localImages.add(R.drawable.local_sample_image_1);
        localImages.add(R.drawable.local_sample_image_2);

        // Initialize view pagers and adapters
        ViewPager neighbourhoodViewPager = view.findViewById(R.id.home_neighbourhood_news_view_pager);
        ViewPager localViewPager = view.findViewById(R.id.home_local_news_view_pager);
        HomeImagePagerAdapter neighbourhoodAdapter = new HomeImagePagerAdapter(view.getContext(), neighbourhoodImages);
        HomeImagePagerAdapter localAdapter = new HomeImagePagerAdapter(view.getContext(), localImages);
        neighbourhoodViewPager.setAdapter(neighbourhoodAdapter);
        localViewPager.setAdapter(localAdapter);
    }
}