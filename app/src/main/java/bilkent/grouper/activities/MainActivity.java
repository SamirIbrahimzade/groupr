package bilkent.grouper.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupr.groupr.R;

import java.util.ArrayList;

import bilkent.grouper.classes.Group;
import bilkent.grouper.dialogs.LoadingDialog;
import bilkent.grouper.dialogs.NavigationDrawer;
import bilkent.grouper.fragments.GroupFragment;
import bilkent.grouper.fragments.NewsFragment;
import bilkent.grouper.fragments.ProfileFragment;


public class MainActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog = new LoadingDialog();
    public static ArrayList<Group> userGroups = new ArrayList<>();
    public static ArrayList<String> groupIDs = new ArrayList<>();
    private boolean connected_internet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();
        if (connected_internet){
            mLoadingDialog.show(getSupportFragmentManager(),"Loading Dialog");
            getGroups();
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.no_internet,Toast.LENGTH_SHORT).show();
        }


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        // starting from one of the fragments

         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();


    }

    private void checkConnection(){

    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //we are connected to a network
            connected_internet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        }

    }

     private void getGroups(){
         FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
         DocumentReference userRef = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
         userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                 if (documentSnapshot != null && documentSnapshot.contains("Groups")) {
                     ArrayList<String> groupID = (ArrayList<String>) documentSnapshot.get("Groups");
                     saveGroups(groupID);
                 }else {
                     mLoadingDialog.dismiss();
                 }
             }
         });
     }

    private void saveGroups(final ArrayList<String> groupID) {
        groupIDs.addAll(groupID);
        for (int i = 0; i < groupID.size(); i++){
            final Group group = new Group();
            group.setGroupID(groupID.get(i));
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Groups").document(groupID.get(i));
            final int finalI = i;
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        group.setGroupName((String) documentSnapshot.get("groupName"));
                    }
                    userGroups.add(group);
                        if (finalI == groupID.size()-1){
                            mLoadingDialog.dismiss();
                        }
                }
            });
        }
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
        NavigationDrawer navigationDrawer = new NavigationDrawer();
        navigationDrawer.setGroups(userGroups);
        navigationDrawer.show(getSupportFragmentManager(),"Navigation Drawer");
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
