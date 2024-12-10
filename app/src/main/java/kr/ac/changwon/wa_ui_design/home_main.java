package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

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

        petPagerAdapter = new PetPagerAdapter(getContext(), petList);
        viewPager.setAdapter(petPagerAdapter);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String petName = intent.getStringExtra("petName");
            String petSpecies = intent.getStringExtra("petSpecies");
            String petAge = intent.getStringExtra("petAge");
            String gender = intent.getStringExtra("gender");

            if (petName != null && petSpecies != null && petAge != null && gender != null) {
                Pet newPet = new Pet(petName, petSpecies, petAge, gender);
                petList.add(newPet);
                petPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addPetToViewPager(String petName, String petSpecies, String petAge, String gender, String photoPath) {
        Pet newPet = new Pet(petName, petSpecies, petAge, gender);
        newPet.setPhoto(photoPath); // 사진 경로 추가
        petList.add(newPet);
        petPagerAdapter.notifyDataSetChanged();
    }

}
