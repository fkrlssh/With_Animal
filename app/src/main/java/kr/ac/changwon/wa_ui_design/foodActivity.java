package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class foodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);

        ImageButton foodReturnHome = findViewById(R.id.food_return_home);
        foodReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(foodActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ListView listView = findViewById(R.id.foodeatlist);
        EditText foodsearch = (EditText)findViewById(R.id.food_search);

        List<foodListItem>itemList = new ArrayList<>();
        itemList.add(new foodListItem("사과","강아지","고양이",true,true));
        itemList.add(new foodListItem("문어","강아지","고양이",true,false));
        itemList.add(new foodListItem("카페인","강아지","고양이",false,false));
        itemList.add(new foodListItem("양파","강아지","고양이",false,false));
        itemList.add(new foodListItem("상추","강아지","고양이",true,true));
        itemList.add(new foodListItem("견과류","강아지","고양이",false,false));
        itemList.add(new foodListItem("체리","강아지","고양이",false,false));
        itemList.add(new foodListItem("마늘","강아지","고양이",false,false));
        itemList.add(new foodListItem("우유","강아지","고양이",false,false));
        itemList.add(new foodListItem("초콜릿","강아지","고양이",false,false));
        itemList.add(new foodListItem("고구마","강아지","고양이",true,true));
        itemList.add(new foodListItem("당근","강아지","고양이",true,true));
        itemList.add(new foodListItem("알코올","강아지","고양이",false,false));
        itemList.add(new foodListItem("옥수수","강아지","고양이",true,true));
        itemList.add(new foodListItem("감자","강아지","고양이",true,true));
        itemList.add(new foodListItem("바나나","강아지","고양이",true,true));
        itemList.add(new foodListItem("오이","강아지","고양이",true,true));
        itemList.add(new foodListItem("족발","강아지","고양이",false,false));
        itemList.add(new foodListItem("포도","강아지","고양이",false,false));
        itemList.add(new foodListItem("아보카도","강아지","고양이",false,false));

        foodAdapter adapter = new foodAdapter(this,itemList);
        listView.setAdapter(adapter);

   foodsearch.addTextChangedListener(new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

       }

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           adapter.search(charSequence.toString());
       }

       @Override
       public void afterTextChanged(Editable editable) {
       }

   });


    }

    public class foodAdapter extends ArrayAdapter<foodListItem>{
        private Context context;
        private List<foodListItem> itemList;
        private List<foodListItem> searchitemList;

        public foodAdapter(Context context, List<foodListItem> itemList) {
            super(context, 0, itemList);
            this.context = context;
            this.itemList = itemList;
            this.searchitemList = new ArrayList<>(itemList);
        }

        public int getCount() {
            return searchitemList.size();
        }

        public foodListItem getItem(int position) {
            return searchitemList.get(position);
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.food_list, parent, false);
            }

            foodListItem currentItem = searchitemList.get(position);

            TextView textView1 = convertView.findViewById(R.id.foodname);
            TextView textView2 = convertView.findViewById(R.id.food_dog);
            TextView textView3 = convertView.findViewById(R.id.food_cat);

            textView1.setText(currentItem.getName());
            textView2.setText(currentItem.getDog());
            textView3.setText(currentItem.getCat());

            if (currentItem.isDog_b()) {
                textView2.setBackgroundResource(R.drawable.orangebox);
            } else {
                textView2.setBackgroundResource(R.drawable.darkgraybox);
            }

            // cat_b 값에 따라 배경색 변경
            if (currentItem.isCat_b()) {
                textView3.setBackgroundResource(R.drawable.orangebox);
            } else {
                textView3.setBackgroundResource(R.drawable.darkgraybox);
            }

            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

            return convertView;
        }

        public void search(String s){
            if(s.isEmpty()){
                searchitemList.clear();
                searchitemList.addAll(itemList);
            }
            else {
                List<foodListItem> searchfood = new ArrayList<>();
                for(foodListItem item : itemList){
                    if(item.getName().toLowerCase().contains(s.toLowerCase())){
                        searchfood.add(item);
                    }
                }
                searchitemList.clear();
                searchitemList.addAll(searchfood);
            }
            notifyDataSetChanged();
        }
    }


}