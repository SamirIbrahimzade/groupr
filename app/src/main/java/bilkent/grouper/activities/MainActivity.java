package bilkent.grouper.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import bilkent.grouper.classes.Group;
import bilkent.grouper.dialogs.NavigationDrawer;
import bilkent.grouper.fragments.GroupFragment;
import bilkent.grouper.fragments.NewsFragment;
import bilkent.grouper.fragments.ProfileFragment;
import com.groupr.groupr.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    public static ArrayList<Group> userGroups = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // starting from one of the fragments

         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_bar)
            openNavigationDrawer();
        return true;
    }

    private void openNavigationDrawer() {
        // TODO
        Toast.makeText(getApplicationContext(),"Nav bar icon clicked", Toast.LENGTH_SHORT).show();
        NavigationDrawer navigationDrawer = new NavigationDrawer();
        navigationDrawer.show(getSupportFragmentManager(),"Navigation Drawer");
        findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
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

                        case R.id.nav_groups:
                            selectedFragment = new GroupFragment();
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
