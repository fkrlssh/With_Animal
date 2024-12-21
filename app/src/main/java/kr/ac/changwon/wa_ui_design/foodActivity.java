package kr.ac.changwon.wa_ui_design;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class foodActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "popup";  // SharedPreferences 파일 이름
    private static final String KEY_LAST_SHOWN_DATE = "lastdate";

    private  ArrayList<Quiz> quizlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food);

        String today = getTodayDate();

        SharedPreferences day = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastShownDate = day.getString(KEY_LAST_SHOWN_DATE, "");

        if(!today.equals(lastShownDate)){
            showQuizpopup();
            SharedPreferences.Editor ed = day.edit();
            ed.putString(KEY_LAST_SHOWN_DATE, today);
            ed.apply();
        }

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
        itemList.add(new foodListItem("우유","강아지","고양이",true,false));
        itemList.add(new foodListItem("생선","강아지","고양이",true,false));
        itemList.add(new foodListItem("오이","강아지","고양이",true,false));
        itemList.add(new foodListItem("바나나","강아지","고양이",true,false));
        itemList.add(new foodListItem("블루베리","강아지","고양이",true,false));
        itemList.add(new foodListItem("버섯","강아지","고양이",true,false));
        itemList.add(new foodListItem("고양이 사료","강아지","고양이",false,true));
        itemList.add(new foodListItem("고구마","강아지","고양이",true,true));
        itemList.add(new foodListItem("사과","강아지","고양이",true,true));
        itemList.add(new foodListItem("옥수수","강아지","고양이",true,true));
        itemList.add(new foodListItem("호박","강아지","고양이",true,true));
        itemList.add(new foodListItem("계란 노른자","강아지","고양이",true,true));
        itemList.add(new foodListItem("소고기","강아지","고양이",true,true));
        itemList.add(new foodListItem("완두콩","강아지","고양이",true,true));
        itemList.add(new foodListItem("초콜릿","강아지","고양이",false,false));
        itemList.add(new foodListItem("아보카도","강아지","고양이",false,false));
        itemList.add(new foodListItem("양파","강아지","고양이",false,false));
        itemList.add(new foodListItem("생감자","강아지","고양이",false,false));
        itemList.add(new foodListItem("건포도","강아지","고양이",false,false));
        itemList.add(new foodListItem("카페인","강아지","고양이",false,false));
        itemList.add(new foodListItem("아보카도","강아지","고양이",false,false));

        Collections.sort(itemList, new Comparator<foodListItem>() {
            @Override
            public int compare(foodListItem food1, foodListItem food2) {
                return food1.getName().compareTo(food2.getName());
            }
        });

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
    private String getTodayDate() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        return date.format(new Date());
    }

    private static class Quiz{
        private String quizstring;
        private boolean answer;

        public Quiz(String quizstring, boolean answer) {
            this.quizstring = quizstring;
            this.answer = answer;
        }

        public String getquiz() {
            return quizstring;
        }

        public boolean getanswer() {
            return answer;
        }
    }

    private void quizex() {
        quizlist = new ArrayList<>();
        quizlist.add(new Quiz("Q.강아지는 초콜릿을 먹어도 된다.", false));
        quizlist.add(new Quiz("Q.강아지는 사과를 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.강아지는 고구마를 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.강아지는 생감자를 먹어도 된다.", false));
        quizlist.add(new Quiz("Q.강아지는 고양이 사료를 먹어도 된다.", false));
        quizlist.add(new Quiz("Q.강아지는 옥수수를 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.고양이는 계란 노른자를 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.고양이는 호박을 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.고양이는 소고기를 먹어도 된다.", true));
        quizlist.add(new Quiz("Q.고양이는 건포도를 먹어도 된다.", false));
        quizlist.add(new Quiz("Q.고양이는 버섯을 먹어도 된다.", false));
        quizlist.add(new Quiz("Q.고양이는 바나나를 먹어도 된다.", false));
    }

    private void showQuizpopup(){
        quizex();
        View quizdialog = getLayoutInflater().inflate(R.layout.food_popup_quiz, null);
        TextView textquiz = quizdialog.findViewById(R.id.quiz);
        ImageButton xquiz = quizdialog.findViewById(R.id.xquiz);
        Button rightB = quizdialog.findViewById(R.id.rightB);
        Button wrongB = quizdialog.findViewById(R.id.wrongB);
        Random random = new Random();
        int index = random.nextInt(quizlist.size());
        Quiz randomquiz = quizlist.get(index);

        textquiz.setText(randomquiz.getquiz());
        rightB.setTag(randomquiz.getanswer());
        wrongB.setTag(!randomquiz.getanswer());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(quizdialog)
                .setCancelable(false);
        AlertDialog dialog = builder.create();

        rightB.setOnClickListener(view -> answercheck(true,randomquiz,dialog));

        wrongB.setOnClickListener(view -> answercheck(false,randomquiz,dialog));

        xquiz.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void answercheck(boolean isSelectedO, Quiz selectedQuiz, AlertDialog dialog) {
        boolean correctAnswer = selectedQuiz.getanswer();
        if ((isSelectedO && correctAnswer) || (!isSelectedO && !correctAnswer)) {
            Toast.makeText(this, "정답입니다!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            Toast.makeText(this, "오답입니다!", Toast.LENGTH_SHORT).show();
        }
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