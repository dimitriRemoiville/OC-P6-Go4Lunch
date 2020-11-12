package com.dimitri.remoiville.go4lunch.ui.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dimitri.remoiville.go4lunch.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapViewFragment extends Fragment
        implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private MainViewModel mMainViewModel;
    private GoogleMap mMap;
    private String API_KEY = BuildConfig.API_KEY;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private Disposable disposable;
    private final int radius = 400;
    private static final String TAG = "MapViewFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        configureViewModel();
        View root = inflater.inflate(R.layout.fragment_mapview, container, false);

        requestLocationPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        // Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        // Async Map
        supportMapFragment.getMapAsync(this);


        // Initialize the SDK
        Places.initialize(getActivity().getApplicationContext(), API_KEY);
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity().getApplicationContext());

        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(getActivity().getApplicationContext(), perms)) {
            Toast.makeText(getActivity().getApplicationContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
        } else {
            requestLocationPermission();
        }
    }

    private void configureObserverPlacesRestaurants() {
        mMainViewModel.streamFetchPlacesRestaurants(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), radius, API_KEY)
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
                });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity().getApplicationContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity().getApplicationContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

}
