package kr.ac.changwon.wa_ui_design;

public class BoardPost {
    private int category_id; // 카테고리 ID
    private String title;    // 게시물 제목
    private String content;  // 게시물 내용
    private String photo_url; // 사진 URL

    // 생성자
    public BoardPost(int category_id, String title, String content, String photo_url) {
        this.category_id = category_id;
        this.title = title;
        this.content = content;
        this.photo_url = photo_url;
    }

    // Getter와 Setter
    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
