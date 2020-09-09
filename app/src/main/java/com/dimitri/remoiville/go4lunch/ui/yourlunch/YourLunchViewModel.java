package com.dimitri.remoiville.go4lunch.ui.yourlunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YourLunchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YourLunchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Your Lunch fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
