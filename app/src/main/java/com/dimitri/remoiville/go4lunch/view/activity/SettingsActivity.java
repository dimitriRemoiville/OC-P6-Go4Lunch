package com.dimitri.remoiville.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.databinding.ActivitySettingsBinding;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private MainViewModel mMainViewModel;

    private User mCurrentUser;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        configureViewModel();

        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        if(mCurrentUser.getLastName() != null) {
            mBinding.settingsLastNameInput.setText(mCurrentUser.getLastName());
        }

        if(mCurrentUser.getFirstName() != null) {
            mBinding.settingsFirstNameInput.setText(mCurrentUser.getFirstName());
        }

        Glide.with(this)
                .load(mCurrentUser.getURLProfilePicture())
                .circleCrop()
                .into(mBinding.settingsProfileImg);
        mBinding.settingsEmailInput.setText(mCurrentUser.getMail());

        updateUserData();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel =  new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void updateUserData() {

        mBinding.settingsSaveButton.setOnClickListener(v -> {
            String firstName = Objects.requireNonNull(mBinding.settingsFirstNameLayout.getEditText()).getText().toString();
            String lastName = Objects.requireNonNull(mBinding.settingsLastNameLayout.getEditText()).getText().toString();
            String email = Objects.requireNonNull(mBinding.settingsEmailLayout.getEditText()).getText().toString();

            mBinding.settingsEmailLayout.setErrorEnabled(false);
            mBinding.settingsFirstNameLayout.setErrorEnabled(false);
            mBinding.settingsLastNameLayout.setErrorEnabled(false);

            if (!validateEmail(email)) {
                mBinding.settingsEmailLayout.setError("Not a valid email address!");
            } else if (!validateName(firstName)) {
                mBinding.settingsFirstNameLayout.setError("Not a valid first name!");
            } else if (!validateName(lastName)) {
                mBinding.settingsLastNameLayout.setError("Not a valid last name!");
            } else {
                doChanges(firstName, lastName, email);
            }
        });
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateName(String name) {
        return name.length() > 0;
    }

    private void doChanges(String firstName, String lastName, String email) {
        mCurrentUser.setFirstName(firstName);
        mCurrentUser.setLastName(lastName);
        mCurrentUser.setMail(email);

        mMainViewModel.updateUserData(mCurrentUser.getUserID(), firstName, lastName, email, mCurrentUser.getURLProfilePicture());

        MainActivity.navigate(SettingsActivity.this);
        finish();
    }


    /**
     * Used to navigate to this activity
     * @param activity
     */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}