package com.dimitri.remoiville.go4lunch.view.fragment.yourlunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;

public class YourLunchFragment extends Fragment {

    private MainViewModel mMainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mMainViewModel =
                ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yourlunch, container, false);

        return root;
    }
}
