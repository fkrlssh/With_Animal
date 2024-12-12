package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardTipAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BoardWrite> tipList;

    public BoardTipAdapter(Context context, ArrayList<BoardWrite> questionList) {
        this.context = context;
        this.tipList = questionList;
    }

    @Override
    public int getCount() {
        return tipList.size();
    }

    @Override
    public Object getItem(int position) {
        return tipList.get(position);
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

        BoardWrite boardWrite = tipList.get(position);
        title.setText(boardWrite.getBoardTitle());
        text.setText(boardWrite.getBoardText());

        return convertView;
    }

    public void updateData(ArrayList<BoardWrite> filteredList) {
        this.tipList = filteredList;
        notifyDataSetChanged();
    }

}
