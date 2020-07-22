package com.example.kindom.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.kindom.HomeActivity;
import com.example.kindom.R;
import com.example.kindom.ui.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeImagePagerAdapter extends PagerAdapter {

    private final Context context;
    private final Activity activity;
    private final LayoutInflater layoutInflater;
    private final ArrayList<Uri> images;

    public HomeImagePagerAdapter(Context context, Activity activity, ArrayList<Uri> images) {
        this.context = context;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(R.layout.home_image_slider, null);
        ImageView imageView = view.findViewById(R.id.home_image);
        Glide.with(context).load(images.get(position)).into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.IS_ADMIN) {
                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to delete this news?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked YES button
                                    Uri uri = images.get(position);
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString());
                                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, R.string.home_admin_news_deleted, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, HomeActivity.class);
                                            activity.finish();
                                            activity.startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, R.string.home_admin_news_deleted_error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked NO button
                                }
                            })
                            .show();
                }
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}