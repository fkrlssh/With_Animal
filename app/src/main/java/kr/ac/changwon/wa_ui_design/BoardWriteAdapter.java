package kr.ac.changwon.wa_ui_design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
public class BoardWriteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BoardWrite> boardList;

    public BoardWriteAdapter(Context context, ArrayList<BoardWrite> boardList) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_list, parent, false);
        }

        TextView title = convertView.findViewById(R.id.board_title);
        TextView text = convertView.findViewById(R.id.board_text);

        BoardWrite boardWrite = boardList.get(position);
        title.setText(boardWrite.getBoardTitle());
        text.setText(boardWrite.getBoardText());

        return convertView;
    }

    public void updateData(ArrayList<BoardWrite> filteredList) {
        this.boardList = filteredList;
        notifyDataSetChanged();
    }

}

