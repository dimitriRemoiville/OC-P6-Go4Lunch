package com.dimitri.remoiville.go4lunch.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityDetailsPlaceBinding;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.view.fragment.workmates.WorkmatesRecyclerViewAdapter;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class DetailsPlaceActivity extends AppCompatActivity {

    private ActivityDetailsPlaceBinding mBinding;
    private MainViewModel mMainViewModel;
    private final String API_KEY = BuildConfig.API_KEY;
    private User currentUser;
    private boolean lunchBooked = false;
    private boolean favoriteRestaurant = false;
    private String placeID;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private static final String TAG = "DetailsPlaceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailsPlaceBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mContext = view.getContext();
        mRecyclerView = mBinding.activityDetailsRecyclerviewList;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));

        configureViewModel();

        // place to display
        placeID = getIntent().getStringExtra("placeId");

        // get the current user
        currentUser = SingletonCurrentUser.getInstance().getCurrentUser();

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
        if (currentUser.getRestaurantID() != null && currentUser.getRestaurantID().equals(placeID)) {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_green_24);
            lunchBooked = true;
        } else {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            lunchBooked = false;
        }

        // like button
        for (int i = 0; i < currentUser.getLikesList().size(); i++) {
            if (currentUser.getLikesList().get(i).equals(placeID)) {
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
                currentUser.setRestaurantID(null);
                currentUser.setRestaurantName(null);
                mMainViewModel.updateLunchID(currentUser.getUserID(), null, null);
                lunchBooked = false;
                mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            } else {
                currentUser.setRestaurantID(place.getPlaceId());
                currentUser.setRestaurantName(place.getName());
                mMainViewModel.updateLunchID(currentUser.getUserID(), place.getPlaceId(), place.getName());
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
                for (int i = 0; i < currentUser.getLikesList().size(); i++) {
                    if (currentUser.getLikesList().get(i).equals(placeID)) {
                        currentUser.getLikesList().remove(i);
                        mMainViewModel.updateLikesList(currentUser.getUserID(), currentUser.getLikesList());
                        mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_border_orange_24);
                        favoriteRestaurant = false;
                    }
                }

            } else {
                currentUser.getLikesList().add(placeID);
                mMainViewModel.updateLikesList(currentUser.getUserID(), currentUser.getLikesList());
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

        loadWorkmatesLists();
    }

    private void loadWorkmatesLists() {
        mMainViewModel.getUsersWithPlaceID(placeID).observe(this, users -> {
            String uid = currentUser.getUserID();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(uid)
                        || users.get(i).getFirstName() == null
                        || users.get(i).getLastName() == null) {
                    users.remove(users.get(i));
                }
            }
            initList(users);
        });
    }

    private void initList(List<User> users) {
        mRecyclerView.setAdapter(new WorkmatesRecyclerViewAdapter(users));
    }
}