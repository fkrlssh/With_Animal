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
        TextView noEventsText = calenderPopup.findViewById(R.id.noScheduleMessage);

        String formattedDate = new SimpleDateFormat("MM월 dd일 E요일", Locale.getDefault()).format(selectedDate);
        eventDate.setText(formattedDate);

        eventsList.clear();
        if (eventsMap.containsKey(selectedDate)) {
            Collections.addAll(eventsList, eventsMap.get(selectedDate).split("\n\n"));
        }

        if (eventsList.isEmpty()) { // 일정이 없는 경우

            eventListView.setVisibility(View.GONE);
            noEventsText.setVisibility(View.VISIBLE);
            noEventsText.setText("일정이 없습니다");
        }
        else {
            eventListView.setVisibility(View.VISIBLE);
            noEventsText.setVisibility(View.GONE);
        }

        adapter = new CalenderEventAdapter(this, eventsList);
        eventListView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(calenderPopup);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        listAddButton.setOnClickListener(v -> {
            alertDialog.dismiss();

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

                if(eventsMap.containsKey(selectedDate)){
                    existingEvents = new ArrayList<>(Arrays.asList(eventsMap.get(selectedDate).split("\n\n")));
                }
                else{
                    existingEvents = new ArrayList<>();
                }
                 if (!existingEvents.contains(eventDetails)) {
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
