package com.example.studentarena;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.studentarena.fragments.ComposeFragment;
import com.example.studentarena.fragments.FeedFragment;
import com.example.studentarena.fragments.MessageFragment;
import com.example.studentarena.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //toolbar = findViewById(R.id.postToolbar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentToShow = null;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentToShow = new FeedFragment();
                        break;
                    case R.id.action_compose:
                        fragmentToShow = new ComposeFragment();
                        break;
                    case R.id.action_message:
                        fragmentToShow = new MessageFragment();
                        break;
                    case R.id.action_profile:
                        fragmentToShow = new ProfileFragment();
                        break;
                    default: break;
                }
                if (fragmentToShow != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragmentToShow).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}