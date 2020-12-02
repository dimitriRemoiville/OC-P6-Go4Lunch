package com.dimitri.remoiville.go4lunch.view.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityDetailsPlaceBinding;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

public class DetailsPlaceActivity extends AppCompatActivity {

    private ActivityDetailsPlaceBinding mBinding;
    private MainViewModel mMainViewModel;
    private final String API_KEY = BuildConfig.API_KEY;
    private User mCurrentUser;
    private boolean lunchBooked = false;
    private boolean favoriteRestaurant = false;
    private String placeID;
    private static final String TAG = "DetailsPlaceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailsPlaceBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        configureViewModel();

        // place to display
        placeID = getIntent().getStringExtra("placeId");

        // get the current user
        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        // start display
        startDisplay();

        // get place details
        mMainViewModel.getRestaurantDetailsData(placeID, API_KEY)
                .observe(this, place -> {
                    place.setPlaceId(placeID);
                    updateUI(place);
                });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void startDisplay() {

        // Booking action button
        if (mCurrentUser.getRestaurantID() != null && mCurrentUser.getRestaurantID().equals(placeID)) {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_green_24);
            lunchBooked = true;
        } else {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            lunchBooked = false;
        }

        // like button
        for (int i = 0; i < mCurrentUser.getLikesList().size(); i++) {
            if (mCurrentUser.getLikesList().get(i).equals(placeID)) {
                mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_orange_24);
                favoriteRestaurant = true;
            }
        }
        if (!favoriteRestaurant) {
            mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_border_orange_24);
        }


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

        mBinding.activityDetailsActBtn.setOnClickListener(v -> {
            if (lunchBooked) {
                mCurrentUser.setRestaurantID(null);
                mCurrentUser.setRestaurantName(null);
                mMainViewModel.updateLunchID(mCurrentUser.getUserID(), null, null);
                lunchBooked = false;
                mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            } else {
                mCurrentUser.setRestaurantID(place.getPlaceId());
                mCurrentUser.setRestaurantName(place.getName());
                mMainViewModel.updateLunchID(mCurrentUser.getUserID(), place.getPlaceId(), place.getName());
                lunchBooked = true;
                mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_green_24);
            }
        });

        mBinding.activityDetailsCallImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + place.getPhoneNumbers()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        mBinding.activityDetailsLikeImg.setOnClickListener(v -> {
            if (favoriteRestaurant) {
                for (int i = 0; i < mCurrentUser.getLikesList().size(); i++) {
                    if (mCurrentUser.getLikesList().get(i).equals(placeID)) {
                        mCurrentUser.getLikesList().remove(i);
                        mMainViewModel.updateLikesList(mCurrentUser.getUserID(),mCurrentUser.getLikesList());
                        mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_border_orange_24);
                        favoriteRestaurant = false;
                    }
                }

            } else {
                mCurrentUser.getLikesList().add(placeID);
                mMainViewModel.updateLikesList(mCurrentUser.getUserID(),mCurrentUser.getLikesList());
                mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_orange_24);
                favoriteRestaurant = true;
            }
        });

        mBinding.activityDetailsWebsiteImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, place.getWebsite());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }
}