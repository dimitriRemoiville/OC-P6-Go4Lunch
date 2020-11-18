package com.dimitri.remoiville.go4lunch.view.fragment.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapViewFragment extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private MainViewModel mMainViewModel;
    private GoogleMap mMap;
    private final String API_KEY = BuildConfig.API_KEY;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private final int radius = 400;
    private static final String TAG = "MapViewFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configureViewModel();
        View root = inflater.inflate(R.layout.fragment_mapview, container, false);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // Async Map
        supportMapFragment.getMapAsync(this);

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mCurrentLocation = location;
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                        mMap.animateCamera(update);
                        configureObserverPlacesRestaurants();
                    }
                }
            });
        }
    }

    private void configureObserverPlacesRestaurants() {
        mMainViewModel.getRestaurantsRepository(mCurrentLocation, radius, API_KEY).observe(getViewLifecycleOwner(), places -> {
                    Log.d(TAG, "configureObserverPlacesRestaurants places.size() : " + places.size());
                    for (int i = 0; i < places.size(); i++) {
                        LatLng position = new LatLng(places.get(i).getLat(), places.get(i).getLng());
                        mMap.addMarker(new MarkerOptions().position(position)
                                .title(places.get(i).getName())
                                .alpha(0.9f)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                });
/*        mMainViewModel.streamFetchPlacesRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), radius, API_KEY)
                .observe(getViewLifecycleOwner(), new Observer<Observable<List<Place>>>() {
                    @Override
                    public void onChanged(Observable<List<Place>> listObservable) {
                        Log.d(TAG, "onChanged: ");
                        disposable = listObservable.subscribeWith(new DisposableObserver<List<Place>>() {
                            @Override
                            public void onNext(@NonNull List<Place> places) {
                                Log.d(TAG, "onNext: ");
                                for (int i = 0; i < places.size(); i++) {
                                    LatLng position = new LatLng(places.get(i).getLat(), places.get(i).getLng());
                                    mMap.addMarker(new MarkerOptions().position(position)
                                            .title(places.get(i).getName())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                }
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

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        if (mCurrentLocation != null) {
            configureObserverPlacesRestaurants();
        }
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

}
