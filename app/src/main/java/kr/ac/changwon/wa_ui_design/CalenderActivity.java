package kr.ac.changwon.wa_ui_design;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalenderActivity extends AppCompatActivity {

    private TextView monthYearText;
    private Calendar calendar;
    private CalenderAdapter calendarAdapter;
    private GridView calendarGridView;
    private Map<Date, String> eventsMap = new HashMap<>();
    private CalenderEventAdapter adapter;
    private List<String> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        ImageButton calenderReturnHomeButton = findViewById(R.id.calendar_return_home);
        calenderReturnHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton leftButton = findViewById(R.id.leftButton);
        ImageButton rightButton = findViewById(R.id.rightButton);

        calendar = Calendar.getInstance();
        monthYearText = findViewById(R.id.monthYearText);
        calendarGridView = findViewById(R.id.calendarGridView);

        calendarAdapter = new CalenderAdapter(this, calendar.getTime(), eventsMap);
        calendarGridView.setAdapter(calendarAdapter);

        leftButton.setOnClickListener(view -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        rightButton.setOnClickListener(view -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        calendarGridView.setOnItemClickListener((parent, view, position, id) -> {
            Date selectedDate = calendarAdapter.getItem(position);
            calendarAdapter.setSelectedDate(selectedDate);
            showCalenderPopup(selectedDate);
        });

        updateCalendar();
    }

    private void updateCalendar() {
        String monthYear = new SimpleDateFormat("yyyy MMMM", Locale.getDefault()).format(calendar.getTime());
        monthYearText.setText(monthYear);
        calendarAdapter.setCalendarDate(calendar.getTime());
        calendarAdapter.notifyDataSetChanged();
    }

    private void showCalenderPopup(Date selectedDate) {
        View calenderPopup = getLayoutInflater().inflate(R.layout.calender_popup, null);
        TextView eventDate = calenderPopup.findViewById(R.id.eventDate);
        ListView eventListView = calenderPopup.findViewById(R.id.popup_list);
        ImageButton listAddButton = calenderPopup.findViewById(R.id.list_add_Button);
        TextView noEventsText = calenderPopup.findViewById(R.id.noScheduleMessage); // "일정이 없습니다" 표시할 TextView

        String formattedDate = new SimpleDateFormat("MM월 dd일 E요일", Locale.getDefault()).format(selectedDate);
        eventDate.setText(formattedDate);

        // 기존 이벤트 목록 초기화 및 업데이트
        eventsList.clear(); // 중복 방지를 위해 리스트 초기화
        if (eventsMap.containsKey(selectedDate)) { // 해당 날짜에 일정이 있을 때
            Collections.addAll(eventsList, eventsMap.get(selectedDate).split("\n\n"));
        }

        // 일정이 없는 경우와 있는 경우를 구분
        if (eventsList.isEmpty()) {
            // 일정이 없는 경우
            eventListView.setVisibility(View.GONE);
            noEventsText.setVisibility(View.VISIBLE);
            noEventsText.setText("일정이 없습니다");
        } else {
            // 일정이 있는 경우
            eventListView.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
        }

        // 어댑터 설정 (한 번만 설정)
        adapter = new CalenderEventAdapter(this, eventsList);
        eventListView.setAdapter(adapter);

        // 일정 추가 버튼 클릭 시 팝업 닫고 일정 추가 화면으로 이동
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(calenderPopup);
        builder.setNegativeButton("닫기", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        listAddButton.setOnClickListener(v -> {
            // 팝업창 닫기
            alertDialog.dismiss();

            // 일정 추가 페이지로 이동
            Intent intent = new Intent(CalenderActivity.this, CalenderAddActivity.class);
            intent.putExtra("selectedDate", selectedDate.getTime());
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            long dateMillis = data.getLongExtra("selectedDate", -1);
            String eventDetails = data.getStringExtra("eventDetails");

            if (dateMillis != -1 && eventDetails != null) {
                Date selectedDate = new Date(dateMillis);

                List<String> existingEvents;

                // 기존 이벤트를 업데이트하는 로직에서 중복되지 않도록 변경
                if(eventsMap.containsKey(selectedDate)){
                    existingEvents = new ArrayList<>(Arrays.asList(eventsMap.get(selectedDate).split("\n\n")));
                }
                else{
                    existingEvents = new ArrayList<>();
                }
                //List<String> existingEvents = eventsMap.containsKey(selectedDate) ? new ArrayList<>(Arrays.asList(eventsMap.get(selectedDate).split("\n\n"))) : new ArrayList<>();
                if (!existingEvents.contains(eventDetails)) {
                    // 중복되지 않는 경우에만 추가
                    existingEvents.add(eventDetails);

                    StringBuilder updatedEvents = new StringBuilder();
                    for (String event : existingEvents) {
                        if (updatedEvents.length() > 0) {
                            updatedEvents.append("\n\n");
                        }
                        updatedEvents.append(event);
                    }

                    eventsMap.put(selectedDate, updatedEvents.toString());

                    showCalenderPopup(selectedDate);
                }
            }
        }
    }


}
