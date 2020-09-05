package com.dimitri.remoiville.go4lunch.ui.mapview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dimitri.remoiville.go4lunch.R;

public class MapViewFragment extends Fragment {
    private MapViewViewModel mMapViewViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mMapViewViewModel =
                ViewModelProviders.of(this).get(MapViewViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mapview, container, false);
        final TextView textView = root.findViewById(R.id.text_mapview);
        mMapViewViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
