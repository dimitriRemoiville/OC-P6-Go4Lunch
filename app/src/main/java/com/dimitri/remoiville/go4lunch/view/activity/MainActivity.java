package com.dimitri.remoiville.go4lunch.view.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.databinding.ActivityMainBinding;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.viewmodel.Injection;
import com.dimitri.remoiville.go4lunch.viewmodel.MainViewModel;
import com.dimitri.remoiville.go4lunch.viewmodel.ViewModelFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mNavigationDrawer;
    private ActivityMainBinding mBinding;
    private DrawerLayout drawer;
    private Context mContext;
    private ConstraintLayout constraintLayout;
    private NavController navController;
    private MainViewModel mMainViewModel;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private User mCurrentUser;

/*    private User currentUser;*/
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        setSupportActionBar(mBinding.appBarMain.toolbar);
        mContext = view.getContext();
        constraintLayout = mBinding.appBarMain.contentMain.contentMain;
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        configureViewModel();

        // Managing permissions
        requestLocationPermission();

        // Get current user information from Firestore and drawer's header
        if (mCurrentUser == null) {
            getCurrentUserFirestore();
        }

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

    private void getCurrentUserFirestore() {
        if (isCurrentUserLogged()) {
            mMainViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid())
                    .observe(this, user -> {
                        mCurrentUser = user;
                        updateDrawerUI();
                    });
        } else {
            showSnackBar(constraintLayout, "User is not logged");
        }
    }

    private void updateDrawerUI() {
        View header = mBinding.navViewDrawer.getHeaderView(0);
        ImageView profilePicture = header.findViewById(R.id.drawer_picture);
        TextView name = header.findViewById(R.id.drawer_name);
        TextView eMail = header.findViewById(R.id.drawer_email);

        Glide.with(this)
                .load(mCurrentUser.getURLProfilePicture())
                .centerCrop()
                .into(profilePicture);
        String fullName = mCurrentUser.getFirstName() + " " + mCurrentUser.getLastName();
        name.setText(fullName);
        eMail.setText(mCurrentUser.getMail());

    }


    // Show Snack Bar with a message
    private void showSnackBar(ConstraintLayout constraintLayout, String message){
        Snackbar.make(constraintLayout, message, Snackbar.LENGTH_SHORT).show();
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