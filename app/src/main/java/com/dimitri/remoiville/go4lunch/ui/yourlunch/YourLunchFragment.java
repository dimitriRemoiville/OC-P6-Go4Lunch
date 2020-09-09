package com.dimitri.remoiville.go4lunch.ui.yourlunch;

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

public class YourLunchFragment extends Fragment {

    private YourLunchViewModel mYourLunchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mYourLunchViewModel =
                ViewModelProviders.of(this).get(YourLunchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_yourlunch, container, false);
        final TextView textView = root.findViewById(R.id.text_yourLunch);
        mYourLunchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
