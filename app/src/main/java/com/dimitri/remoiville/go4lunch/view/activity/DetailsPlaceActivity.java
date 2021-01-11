package com.dimitri.remoiville.go4lunch.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityDetailsPlaceBinding;
import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
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
    private User mCurrentUser;
    private boolean mLunchBooked = false;
    private boolean mFavoriteRestaurant = false;
    private String mPlaceID;
    private RecyclerView mRecyclerView;
    private Context mContext;

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
        mPlaceID = getIntent().getStringExtra("placeId");

        if (mPlaceID == null) {
            mBinding.activityDetailsMessage.setVisibility(View.VISIBLE);
            mBinding.activityDetailsScrollview.setVisibility(View.INVISIBLE);
        } else {
            mBinding.activityDetailsMessage.setVisibility(View.INVISIBLE);
            mBinding.activityDetailsScrollview.setVisibility(View.VISIBLE);
            // get the current user
            mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();
            // start display
            startDisplay();
            // get place details
            mMainViewModel.setRestaurantDetails(mPlaceID,API_KEY);
            mMainViewModel.getRestaurantDetails()
                    .observe(this, place -> {
                        place.setPlaceId(mPlaceID);
                        updateUI(place);
                    });

        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void startDisplay() {

        // Booking action button
        if (mCurrentUser.getRestaurantID() != null && mCurrentUser.getRestaurantID().equals(mPlaceID)) {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_green_24);
            mLunchBooked = true;
        } else {
            mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            mLunchBooked = false;
        }

        // like button
        for (int i = 0; i < mCurrentUser.getLikesList().size(); i++) {
            if (mCurrentUser.getLikesList().get(i).equals(mPlaceID)) {
                mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_orange_24);
                mFavoriteRestaurant = true;
            }
        }
        if (!mFavoriteRestaurant) {
            mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_border_orange_24);
        }


    }

    private void updateUI(PlaceRestaurant place) {
        Glide.with(this)
                .load(place.getUrlPicture())
                .into(mBinding.activityDetailsImg);

        mBinding.activityDetailsName.setText(place.getName());
        switch (place.getRating()) {
            case 1:
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar2.setVisibility(View.VISIBLE);
                break;
            case 3:
                mBinding.activityDetailsStar1.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar2.setVisibility(View.VISIBLE);
                mBinding.activityDetailsStar3.setVisibility(View.VISIBLE);
                break;
        }

        mBinding.activityDetailsAddress.setText(place.getAddress());

        mBinding.activityDetailsActBtn.setOnClickListener(v -> {
            if (mLunchBooked) {
                mCurrentUser.setRestaurantID(null);
                mCurrentUser.setRestaurantName(null);
                mMainViewModel.updateLunchID(mCurrentUser.getUserID(), null, null);
                mLunchBooked = false;
                mBinding.activityDetailsActBtn.setImageResource(R.drawable.check_circle_outline_grey_24);
            } else {
                mCurrentUser.setRestaurantID(place.getPlaceId());
                mCurrentUser.setRestaurantName(place.getName());
                mMainViewModel.updateLunchID(mCurrentUser.getUserID(), place.getPlaceId(), place.getName());
                mLunchBooked = true;
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
            if (mFavoriteRestaurant) {
                for (int i = 0; i < mCurrentUser.getLikesList().size(); i++) {
                    if (mCurrentUser.getLikesList().get(i).equals(mPlaceID)) {
                        mCurrentUser.getLikesList().remove(i);
                        mMainViewModel.updateLikesList(mCurrentUser.getUserID(), mCurrentUser.getLikesList());
                        mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_border_orange_24);
                        mFavoriteRestaurant = false;
                    }
                }

            } else {
                mCurrentUser.getLikesList().add(mPlaceID);
                mMainViewModel.updateLikesList(mCurrentUser.getUserID(), mCurrentUser.getLikesList());
                mBinding.activityDetailsLikeImg.setImageResource(R.drawable.star_orange_24);
                mFavoriteRestaurant = true;
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
        mMainViewModel.getUsersWithPlaceID(mPlaceID).observe(this, users -> {
            String uid = mCurrentUser.getUserID();
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