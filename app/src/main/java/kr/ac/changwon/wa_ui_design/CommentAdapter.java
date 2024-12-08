package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Comment> commentList;

    public CommentAdapter(Context context, ArrayList<Comment> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position; // position을 고유 ID로 사용
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.board_comment_list, parent, false);
        }

        TextView commentId = convertView.findViewById(R.id.comment_id);
        TextView commentText = convertView.findViewById(R.id.comment_text);
        TextView commentDate = convertView.findViewById(R.id.comment_date);

        Comment comment = commentList.get(position);

        commentId.setText(comment.getId());
        commentText.setText(comment.getText());
        commentDate.setText(comment.getDate());

        return convertView;
    }
}
