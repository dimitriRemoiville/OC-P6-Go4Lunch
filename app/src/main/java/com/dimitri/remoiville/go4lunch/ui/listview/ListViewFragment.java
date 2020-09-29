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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.Place;
import com.dimitri.remoiville.go4lunch.model.PlacesPOJO;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class ListViewFragment extends Fragment {

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<PlacesPOJO> mPlaces;
    private Location mCurrentLocation;
    private final String API_KEY = BuildConfig.API_KEY;
    private static final String TAG = "ListViewFragment";
    private Disposable listdisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listview, container, false);

        mContext = root.getContext();
        //mRecyclerView = (RecyclerView) root;
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        configureViewModel();
        Log.d(TAG, "onCreateView: ici");
        String location = -33.8864322 + "," + 151.1933985;
        mMainViewModel.streamFetchPlacesRestaurants(location,1000, API_KEY)
                .observe(getViewLifecycleOwner(), new Observer<List<PlacesPOJO>>() {
                    @Override
                    public void onChanged(List<PlacesPOJO> placesPOJOS) {
                        Log.d(TAG, "onChanged: this is a live data response!");
                        Log.d(TAG, "onChanged: " + placesPOJOS.toString());
                    }
                });
        //initList(mPlaces);
        Log.d(TAG, "onCreateView: test" + mPlaces);
        return root;
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        Log.d(TAG, "configureViewModel: ici");
    }

    private void initList(List<PlacesPOJO> places) {
        mRecyclerView.setAdapter(new ListViewRecyclerViewAdapter(places));
    }
}
