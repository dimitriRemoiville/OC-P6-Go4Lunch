package com.dimitri.remoiville.go4lunch.view.fragment.listview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.event.AutocompleteEvent;
import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewFragment extends Fragment
        implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private final String API_KEY = BuildConfig.API_KEY;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private final int radius = 400;
    private User currentUser;
    PlacesClient placesClient;
    private static final String TAG = "ListViewFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listview_list, container, false);

        mContext = root.getContext();
        mRecyclerView = (RecyclerView) root;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        // Initialize Place SDK
        Places.initialize(mContext, API_KEY);
        // Create a new PlacesClient instance
        placesClient = Places.createClient(mContext);

        currentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        configureViewModel();

        // Get current location
        getLocation();

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mCurrentLocation = location;
                        configureObserverPlacesRestaurants();
                    }
                }
            });
        }
    }

    private void configureObserverPlacesRestaurants() {
        mMainViewModel.getRestaurantsData(mCurrentLocation, radius, API_KEY).observe(getViewLifecycleOwner(), places -> {
            Collections.sort(places, new PlaceRestaurant.PlaceDistanceComparator());
            loadWorkmatesLists(places);
        });
    }

    private void loadWorkmatesLists(List<PlaceRestaurant> places) {
        mMainViewModel.getUsersPlaceIDNotNull().observe(this, users -> {
            String uid = currentUser.getUserID();

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(uid)
                        || users.get(i).getFirstName() == null
                        || users.get(i).getLastName() == null) {
                    users.remove(users.get(i));
                }
            }

            for (int i = 0; i < places.size(); i++) {
                for (int j = 0; j < users.size(); j++) {
                    if (places.get(i).getPlaceId().equals(users.get(j).getRestaurantID())) {
                        places.get(i).getUserList().add(users.get(j));
                    }
                }
            }
            initList(places);
        });
    }

    private void initList(List<PlaceRestaurant> places) {
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places));
    }

    private void initListAutocomplete(List<PlaceRestaurant> places, Bitmap bitmap) {
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places, bitmap));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onAutocompletePlace(AutocompleteEvent event) {
        PlaceRestaurant placeRestaurant = new PlaceRestaurant(event.place, mCurrentLocation, API_KEY);

        final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(event.place.getPhotoMetadatas().get(0))
                .setMaxWidth(400)
                .build();
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
            @Override
            public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                List<PlaceRestaurant> placeRestaurants = new ArrayList<>();
                placeRestaurants.add(placeRestaurant);
                initListAutocomplete(placeRestaurants, bitmap);
            }
        });
    }
}
