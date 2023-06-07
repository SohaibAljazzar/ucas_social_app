package com.example.ucasinsta62;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.ucasinsta62.Fragments.HomeFragment;
import com.example.ucasinsta62.Fragments.NotificationFragment;
import com.example.ucasinsta62.Fragments.ProfileFragment;
import com.example.ucasinsta62.Fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_home) {
                        selectedfragment = new HomeFragment();
                    } else if (itemId == R.id.nav_search) {
                        selectedfragment = new SearchFragment();
                    } else if (itemId == R.id.nav_add) {
                        selectedfragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    } else if (itemId == R.id.nav_heart) {
                        selectedfragment = new NotificationFragment();
                    } else if (itemId == R.id.nav_profile) {
                        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                        editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        editor.apply();
                        selectedfragment = new ProfileFragment();
                    }


                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedfragment).commit();
                    }

                    return true;
                }
            };
}
