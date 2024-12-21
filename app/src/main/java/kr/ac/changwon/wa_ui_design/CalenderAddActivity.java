package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;

public class CalenderAddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender_add);

        EditText titleInput = findViewById(R.id.plan_title);
        TimePicker timePicker = findViewById(R.id.plan_time_picker);
        EditText memoInput = findViewById(R.id.plan_memo);
        Spinner alarmInput = findViewById(R.id.plan_alarm);
        Button addButton = findViewById(R.id.add_list);
        Button cancelButton = findViewById(R.id.add_list_cancel);

        long dateMillis = getIntent().getLongExtra("selectedDate", -1);
        Date selectedDate = new Date(dateMillis);

        timePicker.setIs24HourView(true);
        String[] items = {"5분 전", "10분 전", "20분 전", "30분 전"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.calender_spinner_item,
                items
        );

        adapter.setDropDownViewResource(R.layout.calender_spinner_dropdown_item);
        alarmInput.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            String memo = memoInput.getText().toString();
            String alarm = alarmInput.getSelectedItem().toString();


            if (title.isEmpty()) {
                titleInput.setError(null);
                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                titleInput.requestFocus();
                return;
            }

            if (memo.isEmpty()) {
                memo = " ";
            }

            String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            String eventDetails = time + "\n" + title + "\n" + memo + "\n" + alarm;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedDate", dateMillis);
            resultIntent.putExtra("eventDetails", eventDetails);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "일정이 추가되었습니다!", Toast.LENGTH_SHORT).show();
            finish();
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}