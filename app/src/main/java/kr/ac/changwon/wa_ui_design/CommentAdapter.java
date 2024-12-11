package kr.ac.changwon.wa_ui_design;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> commentList;

    public CommentAdapter(ArrayList<Comment> commentList){
        this.commentList = commentList;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentId;
        TextView commentText;
        TextView commentDate;

        public CommentViewHolder(View view){
            super(view);
            commentId = view.findViewById(R.id.comment_id);
            commentText = view.findViewById(R.id.comment_text);
            commentDate = view.findViewById(R.id.comment_date);
        }
    }

    public CommentViewHolder onCreateViewHolder(ViewGroup group, int viewtype) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.board_comment_list, group, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.commentId.setText(comment.getId());
        holder.commentText.setText(comment.getText());
        holder.commentDate.setText(comment.getDate());
    }

    public int getItemCount(){
        return commentList.size();
    }

}

