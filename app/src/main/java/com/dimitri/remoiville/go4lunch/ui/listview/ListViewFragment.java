package com.dimitri.remoiville.go4lunch.ui.listview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ListViewFragment extends Fragment
        implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String API_KEY = BuildConfig.API_KEY;
    private Disposable disposable;
    private Location mCurrentLocation;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
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
        requestLocationPermission();
        getLocation();
        return root;
    }

    private void getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        Log.d(TAG, "getLocation: ici");
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: là");
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d(TAG, "getLocation: re");
                    if (location != null) {
                        mCurrentLocation = location;
                        configureObserverPlacesRestaurants();
                    }
                }
            });
        } else {
            requestLocationPermission();
        }
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity().getApplicationContext(), perms)) {
            Toast.makeText(getActivity().getApplicationContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void configureObserverPlacesRestaurants() {
        Log.d(TAG, "configureObserverPlacesRestaurants: " + mCurrentLocation.getLatitude() + " ; " + mCurrentLocation.getLongitude());
        mMainViewModel.streamFetchPlacesRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), radius, API_KEY)
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
                });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    private void initList(List<Place> places, List<Integer> distances) {
        Log.d(TAG, "initList: ici");
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places, distances));
    }
}
