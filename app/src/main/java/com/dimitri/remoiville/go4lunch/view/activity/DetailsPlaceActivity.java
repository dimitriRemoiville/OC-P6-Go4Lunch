package com.dimitri.remoiville.go4lunch.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.databinding.ActivityDetailsPlaceBinding;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

public class DetailsPlaceActivity extends AppCompatActivity {

    private ActivityDetailsPlaceBinding mBinding;
    private MainViewModel mMainViewModel;
    private final String API_KEY = BuildConfig.API_KEY;
    private static final String TAG = "DetailsPlaceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailsPlaceBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        configureViewModel();

        // place to display
        String placeId = getIntent().getStringExtra("placeId");
        Log.d(TAG, "onCreate: place id = " + placeId);

        mMainViewModel.getRestaurantDetailsRepository(placeId, API_KEY)
                .observe(this, new Observer<Place>() {
                    @Override
                    public void onChanged(Place place) {
                        updateUI(place);
                    }
                });

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void updateUI(Place place) {
        Log.d(TAG, "onCreate: place.getUrlPicture() " + place.getUrlPicture());
        Glide.with(this)
                .load(place.getUrlPicture())
                .into(mBinding.activityDetailsImg);

        mBinding.activityDetailsName.setText(place.getName());
        Log.d(TAG, "onCreate: place.getRating() " + place.getRating());
        switch (place.getRating()) {
            case 1:
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                Log.d(TAG, "onCreate: place.getRating() 1 " + place.getRating());
                break;
            case 2:
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar2.setVisibility(View.VISIBLE);
                Log.d(TAG, "onCreate: place.getRating() 2 " + place.getRating());
                break;
            case 3:
                Log.d(TAG, "onCreate: place.getRating() 3 " + place.getRating());
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar2.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar3.setVisibility(View.VISIBLE);
                break;
        }

        mBinding.activityDetailsAddress.setText(place.getAddress());
    }
}