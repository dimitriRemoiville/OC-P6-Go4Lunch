package com.dimitri.remoiville.go4lunch.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityMainBinding;
import com.dimitri.remoiville.go4lunch.event.AutocompleteEvent;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.view.activity.chat.ChatActivity;
import com.dimitri.remoiville.go4lunch.viewmodel.SingletonCurrentUser;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;
    private DrawerLayout drawer;
    private Context mContext;
    private NavController navController;
    private MenuItem searchItem;
    private MenuItem chatItem;

    private final String API_KEY = BuildConfig.API_KEY;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;

    private User mCurrentUser;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setSupportActionBar(mBinding.appBarMain.toolbar);
        mContext = view.getContext();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // Initialize Place SDK
        Places.initialize(getApplicationContext(), API_KEY);
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Managing permissions
        requestLocationPermission();

        // Get current user and update drawer's header
        mCurrentUser = SingletonCurrentUser.getInstance().getCurrentUser();
        updateDrawerUI();

        // Bottom navigation
        initBottomNavigation();

        // Drawer navigation
        initDrawerNavigation();

    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
/*        if (EasyPermissions.hasPermissions(mContext, perms)) {
            Toast.makeText(mContext, R.string.permissions_granted, Toast.LENGTH_SHORT).show();
        } else {*/
        if (!EasyPermissions.hasPermissions(mContext, perms)) {
            String request = getString(R.string.request_permissions);
            EasyPermissions.requestPermissions(this, request, REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        searchItem = menu.findItem(R.id.action_search);
        chatItem = menu.findItem(R.id.action_chat);
        configureAutocomplete();
        configureChatItemClick();
        return super.onCreateOptionsMenu(menu);
    }

    private void initBottomNavigation() {
        BottomNavigationView navView = mBinding.appBarMain.contentMain.navView;
        AppBarConfiguration bottomNavigationBar = new AppBarConfiguration.Builder(
                R.id.nav_mapview, R.id.nav_listview, R.id.nav_workmates)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, bottomNavigationBar);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void initDrawerNavigation() {
        drawer = mBinding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mBinding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mBinding.navViewDrawer.setNavigationItemSelectedListener(this);
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(mContext, AuthActivity.class));
                    finish();
                });
    }

    private void updateDrawerUI() {
        View header = mBinding.navViewDrawer.getHeaderView(0);
        ImageView profilePicture = header.findViewById(R.id.drawer_picture);
        TextView name = header.findViewById(R.id.drawer_name);
        TextView eMail = header.findViewById(R.id.drawer_email);

        Glide.with(this)
                .load(mCurrentUser.getURLProfilePicture())
                .circleCrop()
                .into(profilePicture);
        String fullName = mCurrentUser.getFirstName() + " " + mCurrentUser.getLastName();
        name.setText(fullName);
        eMail.setText(mCurrentUser.getMail());

    }

    private void configureAutocomplete() {
        searchItem.setOnMenuItemClickListener(item -> {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.RATING, Place.Field.PHOTO_METADATAS, Place.Field.WEBSITE_URI);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setCountry("AU")
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(mContext);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            return false;
        });
    }

    private void configureChatItemClick() {
        chatItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(mContext, ChatActivity.class);
            startActivity(intent);
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                EventBus.getDefault().post(new AutocompleteEvent(place));
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Used to navigate to this activity
     *
     * @param activity
     */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            logOut();
        }
        if (item.getItemId() == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("creation", false);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.nav_yourLunch) {
            Intent intent = new Intent(this, DetailsPlaceActivity.class);
            intent.putExtra("placeId", mCurrentUser.getRestaurantID());
            startActivity(intent);
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}