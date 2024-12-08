package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        EditText alarmInput = findViewById(R.id.plan_alarm);
        Button addButton = findViewById(R.id.add_list);
        Button cancelButton = findViewById(R.id.add_list_cancel);

        // 선택한 날짜 가져오기
        long dateMillis = getIntent().getLongExtra("selectedDate", -1);
        Date selectedDate = new Date(dateMillis);

        timePicker.setIs24HourView(true);

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();
            String memo = memoInput.getText().toString();
            String alarm = alarmInput.getText().toString();

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