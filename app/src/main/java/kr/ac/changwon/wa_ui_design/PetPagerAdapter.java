package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class PetPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Pet> petList;

    public PetPagerAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public int getCount() {
        return petList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pet_item, container, false); // 새로운 레이아웃 파일 사용
        Pet pet = petList.get(position);


        TextView petName = view.findViewById(R.id.pet_name);
        TextView petSpecies = view.findViewById(R.id.pet_species);
        TextView petAge = view.findViewById(R.id.pet_age);
        TextView petGender = view.findViewById(R.id.pet_gender);
        ImageView petPhoto = view.findViewById(R.id.pet_photo);
        if (pet.getPhoto() != null) {
            petPhoto.setImageURI(Uri.parse(pet.getPhoto()));
        }


        petName.setText(pet.getName());
        petSpecies.setText(pet.getSpecies());
        petAge.setText(pet.getAge());
        petGender.setText(pet.getGender());

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}

