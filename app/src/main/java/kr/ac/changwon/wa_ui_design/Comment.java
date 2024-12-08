package kr.ac.changwon.wa_ui_design;

public class Comment {
    private String id; // 댓글 작성자 ID
    private String text; // 댓글 내용
    private String date; // 댓글 작성 날짜

    public Comment(String id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id; // 나중에 db로 id 가져와야 함
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
