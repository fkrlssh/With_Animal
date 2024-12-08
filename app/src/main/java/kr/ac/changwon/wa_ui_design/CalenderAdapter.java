package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CalenderAdapter extends BaseAdapter {
    private final Context context; // 액티비티 컨텍스트
    private List<Date> dates; // 캘린더에 표시될 날짜 리스트
    private Calendar currentCalendar; // 현재 날짜와 시간 정보 관리
    private int currentMonth; // 현재 표시 중인 달
    private Date selectedDate; // 사용자가 선택한 날짜
    private final Date todayDate; // 오늘 날짜
    private final Map<Date, String> eventsMap; // 날짜별 이벤트 저장


    public CalenderAdapter(Context context, Date date, Map<Date, String> eventsMap) {
        this.context = context;
        this.eventsMap = eventsMap;
        this.currentCalendar = Calendar.getInstance();
        this.currentCalendar.setTime(date);
        this.currentMonth = currentCalendar.get(Calendar.MONTH);
        this.todayDate = Calendar.getInstance().getTime();
        this.selectedDate = todayDate;
        initCalendar(); // 캘린더 초기화
    }


    private void initCalendar() {   // 달력 모양 7행 6줄
        dates = new ArrayList<>();
        Calendar calendar = (Calendar) currentCalendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.add(Calendar.DAY_OF_MONTH, -monthStartDay);

        for (int i = 0; i < 42; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


    public void setCalendarDate(Date date) { // 날짜 업데이트 시 어댑터 초기화 및 변경 반영
        this.currentCalendar.setTime(date);
        this.currentMonth = currentCalendar.get(Calendar.MONTH);
        initCalendar();
        notifyDataSetChanged();
    }


    public void setSelectedDate(Date date) { // 사용자가 선택한 날짜 설정
        this.selectedDate = date;
        notifyDataSetChanged();
    }


    public Date getSelectedDate() {
        return selectedDate;
    }

    @Override
    public int getCount() {
        return dates.size(); // 날짜 개수 반환
    }

    @Override
    public Date getItem(int position) {
        if (position >= 0 && position < dates.size()) {
            return dates.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // 각 날짜의 뷰 설정(요일별 색, 일정 표시 등)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calender_day, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.dayText); // 날짜 텍스트뷰
        View eventDot = convertView.findViewById(R.id.listDot); // 이벤트 표시 점
        Date date = getItem(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        int dayOfMonth = dateCalendar.get(Calendar.DAY_OF_MONTH);
        dayText.setText(String.valueOf(dayOfMonth)); // 날짜 숫자 설정

        // 날짜 색상 설정 (현재 달, 주말, 오늘 여부에 따라 다르게 표시)
        if (dateCalendar.get(Calendar.MONTH) != currentMonth){
            dayText.setTextColor(Color.GRAY); // 해당 달 날짜가 아닌 경우 회색을 출력
        }
        else if (dateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            dayText.setTextColor(Color.RED); // 일요일
        }
        else if (dateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            dayText.setTextColor(Color.BLUE); // 토요일
        }
        else {
            dayText.setTextColor(Color.BLACK);
        }

        // 날짜 배경 및 테두리 설정 (선택된 날짜, 오늘 날짜 등)
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setShape(GradientDrawable.RECTANGLE);

        if (date.equals(selectedDate)){
            backgroundDrawable.setColor(Color.parseColor("#FF5E00")); // 클릭한 날짜 표시
            backgroundDrawable.setStroke(0, Color.TRANSPARENT);
        }
        else if (date.equals(todayDate)){
            backgroundDrawable.setColor(Color.TRANSPARENT);
            backgroundDrawable.setStroke(3, Color.parseColor("#FF5E00")); // 다른 날짜를 클릭했을 때 오늘 날짜 테두리로 표시
        }
        else {
            backgroundDrawable.setColor(Color.TRANSPARENT);
        }

        dayText.setBackground(backgroundDrawable);

        return convertView;
    }


}
