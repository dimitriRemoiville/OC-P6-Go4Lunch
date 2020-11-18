package com.dimitri.remoiville.go4lunch.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityMainBinding;
import com.dimitri.remoiville.go4lunch.view.fragment.listview.ListViewFragment;
import com.dimitri.remoiville.go4lunch.view.fragment.mapview.MapViewFragment;
import com.dimitri.remoiville.go4lunch.view.fragment.workmates.WorkmatesFragment;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mNavigationDrawer;
    private ActivityMainBinding mBinding;
    private DrawerLayout drawer;
    private Context mContext;
    private NavController navController;
    private MainViewModel mMainViewModel;
    private final int REQUEST_LOCATION_PERMISSION = 1;
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
        configureViewModel();

        // Managing permissions
        requestLocationPermission();

        // Bottom navigation
        initBottomNavigation();

        // Drawer navigation
        initDrawerNavigation();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mMainViewModel =  new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            Toast.makeText(mContext, "Permission alrea  dy granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mNavigationDrawer)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
        NavigationView navigationView = mBinding.navViewDrawer;
        mNavigationDrawer = new AppBarConfiguration.Builder(
                R.id.nav_yourLunch, R.id.nav_settings, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mNavigationDrawer);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.nav_logout) {
            logOut();
        }
        if (item.getItemId() == R.id.nav_settings) {
            SettingsActivity.navigate(this);
        }
        if (item.getItemId() == R.id.nav_yourLunch) {
            SettingsActivity.navigate(this);
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(mContext, AuthActivity.class));
                        finish();
                    }
                });
    }

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    /**
     * Used to navigate to this activity
     * @param activity
     */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}