package kr.ac.changwon.wa_ui_design;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment homeF;
    Fragment boardF;
    Fragment notificationF;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.NavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home_Fragment);
        homeF = new home_main();
        boardF = new board_main();
        notificationF = new notification_main();


        if (savedInstanceState == null) {
            loadFragment(new home_main());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                Fragment fragment = null;

                if(itemId == R.id.home_Fragment){

                    fragment = homeF;

                } else if (itemId == R.id.notification_Fragment) {

                    fragment = notificationF;

                } else if (itemId == R.id.board_Fragment) {

                    fragment = boardF;

                }

                return loadFragment(fragment);
            }
        });
    }

    boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView,fragment)
                    .commit();
            return true;
        }else {
            return false;
        }
    }
}