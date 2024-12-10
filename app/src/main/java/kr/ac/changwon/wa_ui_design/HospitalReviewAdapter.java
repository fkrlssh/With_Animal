package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class HospitalReviewAdapter extends BaseAdapter {
    private Context context;
    private List<HospitalReviewItem> reviewList;
    private LayoutInflater inflater;

    public HospitalReviewAdapter(Context context, List<HospitalReviewItem> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.hospital_review_list, parent, false);
            holder = new ViewHolder();
            holder.reviewId = convertView.findViewById(R.id.review_id);
            holder.reviewText = convertView.findViewById(R.id.review_text);
            holder.reviewRating = convertView.findViewById(R.id.rating_num);
            holder.reviewDate = convertView.findViewById(R.id.review_date);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        HospitalReviewItem review = reviewList.get(position);
        holder.reviewId.setText(review.getReviewId());
        holder.reviewText.setText(review.getReviewText());
        holder.reviewRating.setText(String.valueOf(review.getReviewRating()));
        holder.reviewDate.setText(review.getReviewDate());

        return convertView;
    }

    public List<HospitalReviewItem> getReviewList() {
        return reviewList;
    }

    public void addReview(HospitalReviewItem review) {
        reviewList.add(0, review); // 최근에 적은 리뷰를 가장 먼저 보이도록
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView reviewId;
        TextView reviewText;
        TextView reviewRating;
        TextView reviewDate;
    }
}
