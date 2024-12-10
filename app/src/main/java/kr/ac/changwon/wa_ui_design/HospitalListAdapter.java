package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HospitalListAdapter extends BaseAdapter {
    private Context context;
    private List<Hospital> hospitalList;

    public HospitalListAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @Override
    public int getCount() {
        return hospitalList.size();
    }

    @Override
    public Object getItem(int position) {
        return hospitalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.hospital_list, parent, false);
        }

        Hospital hospital = hospitalList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.hospital_title);
        TextView addressTextView = convertView.findViewById(R.id.hospital_text);
        TextView distanceTextView = convertView.findViewById(R.id.hospital_distance);
        TextView ratingTextView = convertView.findViewById(R.id.hospital_rating); // 평균 별점 표시 추가

        nameTextView.setText(hospital.getName());
        addressTextView.setText(hospital.getAddress());
        distanceTextView.setText(String.format("%.1f km", hospital.getDistance() / 1000));
        ratingTextView.setText(String.format("%.1f", hospital.getAverageRating())); // 평균 별점 표시

        return convertView;
    }


    public void updateList(List<Hospital> newHospitalList) {
        this.hospitalList = newHospitalList;
        notifyDataSetChanged();
    }

}
