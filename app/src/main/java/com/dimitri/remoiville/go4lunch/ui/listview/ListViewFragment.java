package com.dimitri.remoiville.go4lunch.ui.listview;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.model.Result;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

import java.util.List;

public class ListViewFragment extends Fragment {

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<Place> mPlaces;
    private Location mCurrentLocation;
    private final String API_KEY = BuildConfig.API_KEY;
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
        configureObserverPlacesRestaurants();
        return root;
    }

    private void configureObserverPlacesRestaurants() {
        String location = -33.8864322 + "," + 151.1933985;
        mMainViewModel.streamFetchPlacesRestaurants(location,1000, API_KEY)
                .observe(getViewLifecycleOwner(), places -> {
                    initList(places);
                    Log.d(TAG, "configureObserverPlacesRestaurants: ici");
                });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    private void initList(List<Place> places) {
        Log.d(TAG, "initList: ici");
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places));
    }
}
