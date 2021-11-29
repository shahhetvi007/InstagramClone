package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.instagramclone.home.HomeFragment;
import com.example.instagramclone.liked.LikedFragment;
import com.example.instagramclone.profile.ProfileFragment;
import com.example.instagramclone.reels.ReelsFragment;
import com.example.instagramclone.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigationHome:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.navigationSearch:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.navigationReels:
                        selectorFragment = new ReelsFragment();
                        break;
                    case R.id.navigationLiked:
                        selectorFragment = new LikedFragment();
                        break;
                    case R.id.navigationProfile:
                        selectorFragment = new ProfileFragment();
                        break;
                }

                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, selectorFragment).commit();
                }
                return true;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String profileId = bundle.getString("publisherId");
            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new ProfileFragment()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HomeFragment()).commit();
        }
    }
}