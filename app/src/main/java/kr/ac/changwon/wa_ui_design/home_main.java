package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home_main extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<Pet> petList = new ArrayList<>();
    private PetPagerAdapter petPagerAdapter;
    private ViewPager viewPager;

    public home_main() {
        // Required empty public constructor
    }

    public static home_main newInstance(String param1, String param2) {
        home_main fragment = new home_main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton homeAnimalPlusButton = view.findViewById(R.id.homeplusB);
        ImageButton homeCalenderButton = view.findViewById(R.id.home_calender);

        Button hospitalCategoryButton = view.findViewById(R.id.hospital_category);
        Button foodCategoryButton = view.findViewById(R.id.food_category);
        Button missingCategoryButton = view.findViewById(R.id.missing_category);
        Button calendarCategoryButton = view.findViewById(R.id.calender_category);

        homeAnimalPlusButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PetRegisterActivity.class);
            startActivity(intent);
        });

        homeCalenderButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CalenderActivity.class);
            startActivity(intent);
        });

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        ImageButton homeCategoryB = view.findViewById(R.id.home_categoryB);

        homeCategoryB.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        hospitalCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HospitalActivity.class);
            startActivity(intent);
        });

        foodCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), foodActivity.class);
            startActivity(intent);
        });

        missingCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MissingActivity.class);
            startActivity(intent);
        });

        calendarCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CalenderActivity.class);
            startActivity(intent);
        });

        viewPager = view.findViewById(R.id.home_view_pager);
        if (viewPager == null) {
            Toast.makeText(getContext(), "ViewPager 초기화 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        if (petList == null) {
            petList = new ArrayList<>();
        }

        petPagerAdapter = new PetPagerAdapter(getContext(), petList);
        if (petPagerAdapter == null) {
            Toast.makeText(getContext(), "Adapter 초기화 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        viewPager.setAdapter(petPagerAdapter);

        // 서버에서 데이터를 가져옵니다.
        fetchPetsFromServer();
    }

    /**
     * 서버에서 동물 데이터를 가져오는 메서드
     */
    private void fetchPetsFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);

        if (userId == null) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Call<List<Pet>> call = apiService.getPets(userId); // `user_id`를 쿼리로 전달

        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pet> pets = response.body();
                    if (!pets.isEmpty()) {
                        for (Pet pet : pets) {
                            if (!petList.contains(pet)) {
                                petList.add(pet);
                            }
                        }
                        petPagerAdapter.notifyDataSetChanged();
                    } else {
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "등록된 동물이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (isAdded() && getContext() != null) {
                        Toast.makeText(getContext(), "동물 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "서버 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * ViewPager에 동물을 추가하는 메서드
     */
    public void addPetToViewPager(String petName, String petSpecies, String petAge, String gender, String photoPath) {
        if (petList == null || petPagerAdapter == null) {
            if (isAdded() && getContext() != null) {
                Toast.makeText(getContext(), "초기화되지 않은 객체가 있습니다.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        Pet newPet = new Pet(petName, petSpecies, petAge, gender);
        newPet.setPhoto(photoPath); // 사진 경로 추가
        petList.add(newPet);
        petPagerAdapter.notifyDataSetChanged();
    }
}