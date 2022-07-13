package com.example.studentarena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.studentarena.fragments.ComposeFragment;
import com.example.studentarena.fragments.FeedFragment;
import com.example.studentarena.fragments.MessageFragment;
import com.example.studentarena.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseGeoPoint;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ParseGeoPoint userLocationGeoPoint;
    private final FeedFragment fragmentHome = new FeedFragment(MainActivity.this);
    private final ComposeFragment fragmentCompose = new ComposeFragment(MainActivity.this);
    private final MessageFragment fragmentMessage = new MessageFragment();
    private final ProfileFragment fragmentProfile = new ProfileFragment(MainActivity.this);
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentToShow;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentToShow = fragmentHome;
                        break;
                    case R.id.action_compose:
                        fragmentToShow = fragmentCompose;
                        break;
                    case R.id.action_message:
                        fragmentToShow = fragmentMessage;
                        break;
                    case R.id.action_profile:
                        fragmentToShow = fragmentProfile;
                        break;
                    default: fragmentToShow = fragmentHome;
                }
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragmentToShow).commit();
                return true;
            }
        });
        setUserLocation();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
    private void setUserLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        long UPDATE_INTERVAL = 10000;  /* 10 secs */
        long MIN_DISTANCE = 1 * 1609; /* 1 mile */
        int REQUEST_CODE = 1;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Double userLongitude = location.getLongitude();
                Double userLatitude = location.getLatitude();
                userLocationGeoPoint = new ParseGeoPoint(userLatitude, userLongitude);
                Log.i(TAG, "User's location is " + userLocationGeoPoint);
                fragmentHome.notifyAdapter();
            }
        });
    }

    public ParseGeoPoint getUserLocation() {
        return userLocationGeoPoint;
    }
}
