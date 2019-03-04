package com.groupr.groupr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // starting from one of the fragments

         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                Fragment oldFragment = new NewsFragment();
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new NewsFragment();

                    switch (item.getItemId()){
                        case R.id.nav_news:
                            selectedFragment = new NewsFragment();
                            break;
                        case R.id.nav_addPost:
                            selectedFragment = new PostFragment();
                            break;
                        case R.id.nav_groups:
                            GroupDialog dialog = new GroupDialog();
                            dialog.show(getSupportFragmentManager(),"GroupDialog");
                            selectedFragment = oldFragment;
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    oldFragment = selectedFragment;
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    return true;
                }
            };

}
