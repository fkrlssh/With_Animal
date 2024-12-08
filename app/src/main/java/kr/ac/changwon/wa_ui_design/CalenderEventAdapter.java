package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CalenderEventAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> events;

    public CalenderEventAdapter(Context context, List<String> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calender_list, parent, false);
        }

        TextView eventTime = convertView.findViewById(R.id.eventTime);
        TextView eventTitle = convertView.findViewById(R.id.eventTitle);

        String[] eventDetails = events.get(position).split("\n");
        if (eventDetails.length > 0) eventTime.setText(eventDetails[0]); // 시간
        if (eventDetails.length > 1) eventTitle.setText(eventDetails[1]); // 제목

        return convertView;
    }
}
