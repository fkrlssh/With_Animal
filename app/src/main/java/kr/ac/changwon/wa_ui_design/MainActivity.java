package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

        loadFragment(homeF);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                Fragment fragment = null;

                if (itemId == R.id.home_Fragment) {
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

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToLoad = intent.getStringExtra("fragmentToLoad");

            if ("boardFragment".equals(fragmentToLoad)) {
                String boardTitle = intent.getStringExtra("boardTitle");
                String boardtext = intent.getStringExtra("boardtext");
                String boardDate = intent.getStringExtra("boardDate");

                boolean isQuestionBoard = intent.getBooleanExtra("isQuestionBoard", false);
                boolean isTipBoard = intent.getBooleanExtra("isTipBoard", false);

                if (boardTitle != null && boardtext != null) {
                    board_main fragment = (board_main) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                    if (fragment != null) {
                        fragment.addBoardList(boardTitle, boardtext, boardDate, isQuestionBoard, isTipBoard);
                    }
                }

                loadFragment(boardF);
                bottomNavigationView.setSelectedItemId(R.id.board_Fragment);
            } else {
                String petName = intent.getStringExtra("petName");
                String petSpecies = intent.getStringExtra("petSpecies");
                String petAge = intent.getStringExtra("petAge");
                String gender = intent.getStringExtra("gender");

                if (petName != null && petSpecies != null && petAge != null && gender != null) {
                    home_main fragment = (home_main) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                    if (fragment != null) {
                        fragment.addPetToViewPager(petName, petSpecies, petAge, gender);
                    }
                }
            }
        }
    }
}
