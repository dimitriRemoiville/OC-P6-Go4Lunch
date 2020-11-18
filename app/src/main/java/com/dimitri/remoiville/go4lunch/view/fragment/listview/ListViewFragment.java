package com.dimitri.remoiville.go4lunch.view.fragment.listview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

        configureViewModel();
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
        mMainViewModel.getRestaurantsRepository(mCurrentLocation,radius,API_KEY).observe(getViewLifecycleOwner(), places -> {
            Collections.sort(places, new Place.PlaceDistanceComparator());
            initList(places);
        });
/*        mMainViewModel.streamFetchPlacesRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), radius, API_KEY)
                .observe(getViewLifecycleOwner(), new Observer<Observable<List<Place>>>() {
                    @Override
                    public void onChanged(Observable<List<Place>> listObservable) {
                        Log.d(TAG, "onChanged: ici");
                        disposable = listObservable.subscribeWith(new DisposableObserver<List<Place>>() {
                            @Override
                            public void onNext(@NonNull List<Place> places) {
                                Log.d(TAG, "onNext 1 : " + places);
                                StringBuilder destinations = new StringBuilder();
                                for (int i = 0; i < places.size();i++) {
                                    destinations.append(places.get(i).getLat().toString()).append(",").append(places.get(i).getLng().toString());
                                    if (i != places.size() - 1 ) {
                                        destinations.append("|");
                                    }
                                }
                                Log.d(TAG, "onNext: " + destinations);
                                mMainViewModel.streamFetchDistances(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(), destinations.toString(),API_KEY)
                                        .observe(getViewLifecycleOwner(), new Observer<Observable<List<Integer>>>() {
                                            @Override
                                            public void onChanged(Observable<List<Integer>> listObservable) {
                                                disposable = listObservable.subscribeWith(new DisposableObserver<List<Integer>>() {
                                                    @Override
                                                    public void onNext(@io.reactivex.annotations.NonNull List<Integer> integers) {
                                                        initList(places, integers);
                                                    }

                                                    @Override
                                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                                    }

                                                    @Override
                                                    public void onComplete() {

                                                    }
                                                });
                                            }
                                        });
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                            }
                        });
                    }
                });*/
    }

    private void initList(List<Place> places) {
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places));
    }
}
