package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment homeF;
    Fragment boardF;
    Fragment notificationF;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

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

        requestPermissionsIfNeeded();
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
            // Intent로 전달된 추가 데이터를 확인하고 처리
            String fragmentToLoad = intent.getStringExtra("fragmentToLoad");

            if ("boardFragment".equals(fragmentToLoad)) {
                // 게시판 관련 데이터 처리
                String boardTitle = intent.getStringExtra("boardTitle");
                String boardtext = intent.getStringExtra("boardtext");
                String boardDate = intent.getStringExtra("boardDate");

                boolean isQuestionBoard = intent.getBooleanExtra("isQuestionBoard", false);
                boolean isTipBoard = intent.getBooleanExtra("isTipBoard", false);

                if (boardTitle != null && boardtext != null) {
                    // 게시판 프래그먼트에 데이터 추가
                    board_main fragment = (board_main) getSupportFragmentManager()
                            .findFragmentById(R.id.fragmentContainerView);
                    if (fragment != null) {
                        fragment.addBoardList(boardTitle, boardtext, boardDate, isQuestionBoard, isTipBoard);
                    }
                }

                loadFragment(boardF); // 게시판 프래그먼트 로드
                bottomNavigationView.setSelectedItemId(R.id.board_Fragment);
            } else {
                // Home Fragment와 관련된 Intent 처리
                String petName = intent.getStringExtra("petName");
                String petSpecies = intent.getStringExtra("petSpecies");
                String petAge = intent.getStringExtra("petAge");
                String gender = intent.getStringExtra("gender");

                String photoPath = intent.getStringExtra("photoPath");

                if (petName != null && petSpecies != null && petAge != null && gender != null && photoPath != null) {
                    home_main fragment = (home_main) getSupportFragmentManager()
                            .findFragmentById(R.id.fragmentContainerView);
                    if (fragment != null) {
                        fragment.addPetToViewPager(petName, petSpecies, petAge, gender, photoPath);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            StringBuilder deniedPermissions = new StringBuilder();
            boolean allGranted = true;

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    deniedPermissions.append(permissions[i]).append("\n");
                }
            }

            if (allGranted) {
                Toast.makeText(this, "모든 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "일부 권한이 거부되었습니다. 앱의 일부 기능이 제한될 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13
            String[] permissions = {
                    android.Manifest.permission.READ_MEDIA_IMAGES, // 이미지 읽기 권한
                    android.Manifest.permission.ACCESS_FINE_LOCATION, // 고정밀 위치 권한
                    android.Manifest.permission.POST_NOTIFICATIONS // 알림 권한
            };

            boolean allGranted = true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Android 6
            String[] permissions = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE, // 기존 저장소 읽기 권한
                    android.Manifest.permission.ACCESS_FINE_LOCATION // 고정밀 위치 권한
            };

            boolean allGranted = true;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
            }
        }
    }



}
