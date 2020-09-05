package com.dimitri.remoiville.go4lunch.ui.mapview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MapViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MapView fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
