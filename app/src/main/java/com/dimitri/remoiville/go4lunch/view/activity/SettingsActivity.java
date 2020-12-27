package com.dimitri.remoiville.go4lunch.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private MainViewModel mMainViewModel;

    private User mCurrentUser;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private ConstraintLayout constraintLayout;
    private Context mContext;
    private boolean mIsCreation;

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

        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();

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

        mBinding.settingsSwitchNotif.setChecked(mCurrentUser.hasChosenNotification());
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

            if (!validateEmail(email)) {
                mBinding.settingsEmailLayout.setError("Not a valid email address!");
            } else if (validateName(firstName)) {
                mBinding.settingsFirstNameLayout.setError("Not a valid first name!");
            } else if (validateName(lastName)) {
                mBinding.settingsLastNameLayout.setError("Not a valid last name!");
            } else {
                doChanges(firstName, lastName, email);
            }
        });
    }

    public boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateName(String name) {
        return name.length() <= 0;
    }

    private void doChanges(String firstName, String lastName, String email) {
        mCurrentUser.setFirstName(firstName);
        mCurrentUser.setLastName(lastName);
        mCurrentUser.setMail(email);

        if (mBinding.settingsSwitchNotif.isChecked() != mCurrentUser.hasChosenNotification()) {
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
        mCurrentUser.setHasChosenNotification(true);
        mMainViewModel.updateHasChosenNotification(mCurrentUser.getUserID(), mCurrentUser.hasChosenNotification());
    }

    private void unsubscribeToTopic() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.topic));
        mCurrentUser.setHasChosenNotification(false);
        mMainViewModel.updateHasChosenNotification(mCurrentUser.getUserID(), mCurrentUser.hasChosenNotification());
    }

    private void sendPasswordResetEmail() {
        mBinding.settingsResetPassword.setOnClickListener(v ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(mCurrentUser.getMail())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showSnackBar(constraintLayout, "We sent you an email !");
                                mBinding.settingsResetPassword.setEnabled(false);
                            }
                        }));
    }

    private void deleteUserOnClick() {
        mBinding.settingsDeleteAccount.setOnClickListener(v -> deleteUserWithAlert());
    }

    private void deleteUserWithAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you sure ?")
                .setMessage("Do you really want to delete your account ?")
                .setPositiveButton("OK", (dialog, which) -> deleteUser())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteUser() {
        mMainViewModel.deleteUser(mCurrentUser.getUserID()).addOnSuccessListener(aVoid ->
                FirebaseAuth.getInstance().getCurrentUser().delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showSnackBar(constraintLayout, "Account deleted !");
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