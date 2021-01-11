package com.dimitri.remoiville.go4lunch.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivitySettingsBinding;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.utils.UtilsSettings;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private MainViewModel mMainViewModel;

    private ConstraintLayout constraintLayout;
    private Context mContext;

    private User mCurrentUser;
    private boolean mIsCreation;
    SharedPreferences sharedPref;
    public static final String PREFERENCES = "com.dimitri.remoiville.go4lunch.Notification";
    public static final String notification = "notifKey";
    private boolean hasChosenNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        constraintLayout = mBinding.settingsConstraintLayout;
        mContext = view.getContext();
        configureViewModel();

        // is it the first connexion
        mIsCreation = getIntent().getBooleanExtra("creation", false);

        // get current user
        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();

        // get notification choice with the shared preferences
        sharedPref = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        hasChosenNotification = sharedPref.getBoolean(notification,false);

        if (mIsCreation) {
            setFirstConnexionUI();
        } else {
            setSettingsUI();
        }

        Glide.with(this)
                .load(mCurrentUser.getURLProfilePicture())
                .circleCrop()
                .into(mBinding.settingsProfileImg);
        mBinding.settingsEmailInput.setText(mCurrentUser.getMail());

        updateUserData();

        if (!mIsCreation) {
            sendPasswordResetEmail();
            deleteUserOnClick();
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void setFirstConnexionUI() {
        mBinding.settingsSwitchNotif.setChecked(true);
        mBinding.settingsDeleteAccount.setVisibility(View.GONE);
        mBinding.settingsResetPassword.setVisibility(View.GONE);
    }

    private void setSettingsUI() {
        if (mCurrentUser.getLastName() != null) {
            mBinding.settingsLastNameInput.setText(mCurrentUser.getLastName());
        }

        if (mCurrentUser.getFirstName() != null) {
            mBinding.settingsFirstNameInput.setText(mCurrentUser.getFirstName());
        }

        mBinding.settingsSwitchNotif.setChecked(hasChosenNotification);
        mBinding.settingsDeleteAccount.setVisibility(View.VISIBLE);
        mBinding.settingsResetPassword.setVisibility(View.VISIBLE);
    }

    private void updateUserData() {

        mBinding.settingsSaveButton.setOnClickListener(v -> {
            String firstName = Objects.requireNonNull(mBinding.settingsFirstNameLayout.getEditText()).getText().toString();
            String lastName = Objects.requireNonNull(mBinding.settingsLastNameLayout.getEditText()).getText().toString();
            String email = Objects.requireNonNull(mBinding.settingsEmailLayout.getEditText()).getText().toString();

            mBinding.settingsEmailLayout.setErrorEnabled(false);
            mBinding.settingsFirstNameLayout.setErrorEnabled(false);
            mBinding.settingsLastNameLayout.setErrorEnabled(false);

            if (!UtilsSettings.validateEmail(email)) {
                mBinding.settingsEmailLayout.setError(getString(R.string.settings_validity_email));
            } else if (UtilsSettings.validateName(firstName)) {
                mBinding.settingsFirstNameLayout.setError(getString(R.string.settings_validity_first_name));
            } else if (UtilsSettings.validateName(lastName)) {
                mBinding.settingsLastNameLayout.setError(getString(R.string.settings_validity_last_name));
            } else {
                doChanges(firstName, lastName, email);
            }
        });
    }

    private void doChanges(String firstName, String lastName, String email) {
        mCurrentUser.setFirstName(firstName);
        mCurrentUser.setLastName(lastName);
        mCurrentUser.setMail(email);

        if (mBinding.settingsSwitchNotif.isChecked() != hasChosenNotification) {
            if (mBinding.settingsSwitchNotif.isChecked()) {
                subscribeToTopic();
            } else {
                unsubscribeToTopic();
            }
        }

        mMainViewModel.updateUserData(mCurrentUser.getUserID(), firstName, lastName, email, mCurrentUser.getURLProfilePicture());

        MainActivity.navigate(SettingsActivity.this);
        finish();
    }

    private void subscribeToTopic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String channelId = getString(R.string.default_notification_channel_id);
            CharSequence channelName = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.topic));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notification, true);
        editor.apply();
    }

    private void unsubscribeToTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.topic));
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(notification, false);
        editor.apply();
    }

    private void sendPasswordResetEmail() {
        mBinding.settingsResetPassword.setOnClickListener(v ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(mCurrentUser.getMail())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showSnackBar(constraintLayout, getString(R.string.settings_email_sent));
                                mBinding.settingsResetPassword.setEnabled(false);
                            }
                        }));
    }

    private void deleteUserOnClick() {
        mBinding.settingsDeleteAccount.setOnClickListener(v -> deleteUserWithAlert());
    }

    private void deleteUserWithAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.settings_alert_title)
                .setMessage(R.string.settings_alert_message)
                .setPositiveButton(R.string.settings_alert_validation, (dialog, which) -> deleteUser())
                .setNegativeButton(R.string.settings_alert_cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteUser() {
        mMainViewModel.deleteUser(mCurrentUser.getUserID()).addOnSuccessListener(aVoid ->
                FirebaseAuth.getInstance().getCurrentUser().delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                sharedPref.edit().clear();
                                sharedPref.edit().apply();
                                showSnackBar(constraintLayout, getString(R.string.settings_alert_confirmation));
                                mBinding.settingsDeleteAccount.setEnabled(false);
                                logOut();
                            }
                        }));
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(mContext, AuthActivity.class));
                    finish();
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mIsCreation) {
            deleteUser();
            startActivity(new Intent(mContext, AuthActivity.class));
            finish();
        }
    }

    // Show Snack Bar with a message
    private void showSnackBar(ConstraintLayout constraintLayout, String message) {
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}