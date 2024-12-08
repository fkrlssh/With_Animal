package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardQuestionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BoardWrite> questionList;

    public BoardQuestionAdapter(Context context, ArrayList<BoardWrite> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.board_list, parent, false);
        }

        TextView title = convertView.findViewById(R.id.board_title);
        TextView text = convertView.findViewById(R.id.board_text);

        BoardWrite boardWrite = questionList.get(position);
        title.setText(boardWrite.getBoardTitle());
        text.setText(boardWrite.getBoardText());

        return convertView;
    }
}
