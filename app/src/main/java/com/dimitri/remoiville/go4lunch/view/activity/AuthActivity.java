package com.dimitri.remoiville.go4lunch.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityAuthBinding;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private ConstraintLayout constraintLayout;
    private ActivityAuthBinding mBinding;
    private MainViewModel mMainViewModel;

    private static final String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        constraintLayout = mBinding.activityAuth;
        configureViewModel();

        // Launch authentication screen
        startSignIn();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel =  new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void startSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.TwitterBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(true,true)
                        .setTheme(R.style.AuthTheme)
                        .setLogo(R.drawable.logo_go4lunch_txt)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(constraintLayout, getString(R.string.connection_succeed));
                managingCurrentUser();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(constraintLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(constraintLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(constraintLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void managingCurrentUser() {
        Log.d(TAG, "managingCurrentUser: ");
        mMainViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid()).observe(this, user -> {
            if (user == null) {
                FirebaseUser fUser = getCurrentUser();
                mMainViewModel.createNewUser(fUser.getUid(),fUser.getDisplayName(), fUser.getDisplayName(), fUser.getEmail());
            }
            MainActivity.navigate(this);
        });
    }

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // Show Snack Bar with a message
    private void showSnackBar(ConstraintLayout constraintLayout, String message){
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}