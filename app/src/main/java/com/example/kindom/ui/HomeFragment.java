package com.example.kindom.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import com.example.kindom.R;
import com.example.kindom.User;
import com.example.kindom.home.HomeAdminAddNewsActivity;
import com.example.kindom.ui.home.HomeImagePagerAdapter;
import com.example.kindom.utils.FirebaseHandler;
import com.example.kindom.utils.Region;
import com.example.kindom.utils.WeatherLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<String>> {

    public static boolean IS_ADMIN = false;

    private View mView;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mNewsDatabase;
    private StorageReference mStorageRef;
    private String mRegion;
    private ArrayList<String> mWeatherResult;
    private ViewPager mNeighbourhoodViewPager;
    private ViewPager mSingaporeViewPager;
    private HomeImagePagerAdapter mNeighbourhoodAdapter;
    private HomeImagePagerAdapter mSingaporeAdapter;
    private ArrayList<Uri> mNeighbourhoodImages = new ArrayList<>();
    private ArrayList<Uri> mSingaporeImages = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase components
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        mNewsDatabase = FirebaseDatabase.getInstance().getReference("news");
        mStorageRef = FirebaseStorage.getInstance().getReference("news");
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
        mView = view;

        // Retrieve user's data from Firebase Database
        mUserDatabase.child(FirebaseHandler.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;

                // Grant the admin access to manage news
                if (user.getUserGroup().equals(User.USER_GROUP_ADMIN)) {
                    IS_ADMIN = true;
                    FloatingActionButton manageNewsFab = Objects.requireNonNull(getActivity()).findViewById(R.id.home_admin_add_news_fab);
                    manageNewsFab.setVisibility(View.VISIBLE);
                    manageNewsFab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = getContext();
                            Intent intent = new Intent(context, HomeAdminAddNewsActivity.class);
                            assert context != null;
                            context.startActivity(intent);
                        }
                    });
                }

                // Start loader to query weather information based on the user's region
                mRegion = Region.getRegion(user.getPostalCode());
                getLoaderManager().initLoader(0, null, HomeFragment.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });

        initializeAdapters();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Show text if there are no news
        if (mNeighbourhoodImages.size() == 0) {
            Objects.requireNonNull(getActivity()).findViewById(R.id.home_neighbourhood_no_news).setVisibility(View.VISIBLE);
        }
        if (mSingaporeImages.size() == 0) {
            Objects.requireNonNull(getActivity()).findViewById(R.id.home_singapore_no_news).setVisibility(View.VISIBLE);
        }

        mNeighbourhoodImages.clear();
        mSingaporeImages.clear();
        mNeighbourhoodAdapter.notifyDataSetChanged();
        mSingaporeAdapter.notifyDataSetChanged();

        retrieveNeighbourhoodNews();
        retrieveSingaporeNews();
    }

    /**
     * Initialize view pagers and adapters
     */
    private void initializeAdapters() {
        mNeighbourhoodViewPager = mView.findViewById(R.id.home_neighbourhood_news_view_pager);
        mNeighbourhoodAdapter = new HomeImagePagerAdapter(mView.getContext(), getActivity(), mNeighbourhoodImages);
        mNeighbourhoodViewPager.setAdapter(mNeighbourhoodAdapter);

        mSingaporeViewPager = mView.findViewById(R.id.home_singapore_news_view_pager);
        mSingaporeAdapter = new HomeImagePagerAdapter(mView.getContext(), getActivity(), mSingaporeImages);
        mSingaporeViewPager.setAdapter(mSingaporeAdapter);
    }

    /**
     * Retrieve neighbourhood news images from Firebase Storage
     */
    private void retrieveNeighbourhoodNews() {
        // Get user's RC
        mUserDatabase.child(FirebaseHandler.getCurrentUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                final String rc = user.getRc();
                final StorageReference mNewsStorageRef = mStorageRef.child("neighbourhoodNews").child(rc);
                mNewsDatabase.child("neighbourhoodNews").child(rc).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot newsSnapshot : snapshot.getChildren()) {
                            final String id = newsSnapshot.getKey();
                            assert id != null;
                            mNewsStorageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mNeighbourhoodImages.add(uri);
                                    createNeighbourhoodNews();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mNewsDatabase.child("neighbourhoodNews").child(rc).child(id).removeValue();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Do nothing
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    /**
     * Create images and slider dots for neighbourhood news
     */
    private void createNeighbourhoodNews() {
        // Create slider dots panel
        final LinearLayout neighbourhoodSliderDotsPanel = mView.findViewById(R.id.home_neighbourhood_news_slider);
        neighbourhoodSliderDotsPanel.removeAllViews();
        final int neighbourhoodDotsCount = mNeighbourhoodImages.size();
        final ImageView[] neighbourhoodDots = new ImageView[neighbourhoodDotsCount];
        for (int i = 0; i < neighbourhoodDotsCount; i++) {
            neighbourhoodDots[i] = new ImageView(mView.getContext());
            if (i == 0) {
                neighbourhoodDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_active));
            } else {
                neighbourhoodDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_non_active));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            neighbourhoodSliderDotsPanel.addView(neighbourhoodDots[i], params);
        }

        // Set page change listeners
        mNeighbourhoodAdapter.notifyDataSetChanged();
        mNeighbourhoodViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < neighbourhoodDotsCount; i++) {
                    if (i == position) {
                        neighbourhoodDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_active));
                    } else {
                        neighbourhoodDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_non_active));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
    }

    /**
     * Retrieve Singapore news images from Firebase Storage
     */
    private void retrieveSingaporeNews() {
        final StorageReference mNewsStorageRef = mStorageRef.child("singaporeNews");
        mNewsDatabase.child("singaporeNews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newsSnapshot : snapshot.getChildren()) {
                    final String id = newsSnapshot.getKey();
                    assert id != null;
                    mNewsStorageRef.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mSingaporeImages.add(uri);
                            createSingaporeNews();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mNewsDatabase.child("singaporeNews").child(id).removeValue();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Do nothing
            }
        });
    }

    /**
     * Create images and slider dots for Singapore news
     */
    private void createSingaporeNews() {
        // Create slider dots panel
        LinearLayout singaporeSliderDotsPanel = mView.findViewById(R.id.home_singapore_news_slider);
        singaporeSliderDotsPanel.removeAllViews();
        final int singaporeDotsCount = mSingaporeImages.size();
        final ImageView[] singaporeDots = new ImageView[singaporeDotsCount];
        for (int i = 0; i < singaporeDotsCount; i++) {
            singaporeDots[i] = new ImageView(mView.getContext());
            if (i == 0) {
                singaporeDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_active));
            } else {
                singaporeDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_non_active));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            singaporeSliderDotsPanel.addView(singaporeDots[i], params);
        }

        // Set page change listeners
        mSingaporeAdapter.notifyDataSetChanged();
        mSingaporeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < singaporeDotsCount; i++) {
                    if (i == position) {
                        singaporeDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_active));
                    } else {
                        singaporeDots[i].setImageDrawable(mView.getContext().getDrawable(R.drawable.slider_dot_non_active));
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