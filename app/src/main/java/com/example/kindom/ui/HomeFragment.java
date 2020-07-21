package com.example.kindom.ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.ui.home.HomeImagePagerAdapter;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.utils.Region;
import com.example.kindom.utils.WeatherLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private DatabaseReference mUserDatabase;
    private String mRegion;
    private ArrayList<String> mWeatherResult;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        mUserDatabase.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @NonNull
    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, @Nullable Bundle args) {
        return new WeatherLoader(Objects.requireNonNull(getContext()), mRegion);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> data) {
        mWeatherResult = data;

        String weather = mWeatherResult.get(0);
        Objects.requireNonNull(getActivity()).findViewById(R.id.home_weather_progress_circular).setVisibility(View.GONE);
        ImageView weatherIcon = getActivity().findViewById(R.id.home_weather_icon);
        TextView weatherText = getActivity().findViewById(R.id.home_weather_text);
        if (weather.equals("Fair (Day)")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_fair_day));
            weatherText.setText(R.string.home_weather_fair);
        } else if (weather.equals("Fair (Night)")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_fair_night));
            weatherText.setText(R.string.home_weather_fair);
        } else if (weather.equals("Partly Cloudy (Day)")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_partly_cloudy_day));
            weatherText.setText(R.string.home_weather_partly_cloudy);
        } else if (weather.equals("Partly Cloudy (Night)")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_partly_cloudy_night));
            weatherText.setText(R.string.home_weather_partly_cloudy);
        } else if (weather.equals("Cloudy")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_cloudy));
            weatherText.setText(R.string.home_weather_cloudy);
        } else if (weather.contains("Hazy")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_haze));
            weatherText.setText(R.string.home_weather_hazy);
        } else if (weather.equals("Windy")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_windy));
            weatherText.setText(R.string.home_weather_windy);
        } else if (weather.equals("Mist")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_mist));
            weatherText.setText(R.string.home_weather_mist);
        } else if (weather.contains("Thundery Showers")) {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_thundery_showers));
            weatherText.setText(R.string.home_weather_thundery_showers);
        } else {
            weatherIcon.setImageDrawable(getActivity().getDrawable(R.drawable.weather_rain));
            String[] split = weather.split("\\s+");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                result.append(split[i]);
                if (i < split.length - 1) {
                    result.append("\n");
                }
            }
            weatherText.setText(result.toString());
        }

        String temperature = mWeatherResult.get(1);
        TextView temperatureText = getActivity().findViewById(R.id.home_weather_temperature);
        temperatureText.setText(temperature);

        getActivity().findViewById(R.id.home_psi_progress_circular).setVisibility(View.GONE);
        getActivity().findViewById(R.id.home_psi_value_header).setVisibility(View.VISIBLE);
        String psi = mWeatherResult.get(2);
        TextView psiValue = getActivity().findViewById(R.id.home_psi_value);
        psiValue.setText(psi);
        int psiInt = Integer.parseInt(psi);
        if (psiInt <= 50) {
            psiValue.setTextColor(getActivity().getColor(R.color.psi_good));
        } else if (psiInt <= 100) {
            psiValue.setTextColor(getActivity().getColor(R.color.psi_moderate));
        } else if (psiInt <= 200) {
            psiValue.setTextColor(getActivity().getColor(R.color.psi_unhealthy));
        } else if (psiInt <= 300) {
            psiValue.setTextColor(getActivity().getColor(R.color.psi_very_unhealthy));
        } else {
            psiValue.setTextColor(getActivity().getColor(R.color.psi_hazardous));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {
        mWeatherResult.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve user's data from Firebase Database
        mUserDatabase.child(FirebaseHandler.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                mRegion = Region.getRegion(user.getPostalCode());

                // Start loader to query weather information
                getLoaderManager().initLoader(0, null, HomeFragment.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });

        initializeNews(view);
    }

    /**
     * Initialize images and slider dots for neighbourhood and Singapore news
     *
     * @param view the view
     */
    private void initializeNews(final View view) {
        // Initialize images
        ArrayList<Integer> neighbourhoodImages = new ArrayList<>();
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_1);
        neighbourhoodImages.add(R.drawable.neighbourhood_sample_image_2);

        ArrayList<Integer> localImages = new ArrayList<>();
        localImages.add(R.drawable.local_sample_image_1);
        localImages.add(R.drawable.local_sample_image_2);

        // Initialize slider dots panel
        final LinearLayout neighbourhoodSliderDotsPanel = view.findViewById(R.id.home_neighbourhood_news_slider);
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