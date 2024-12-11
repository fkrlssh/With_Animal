package kr.ac.changwon.wa_ui_design;

class BoardWrite {
    private String boardTitle; // 게시물 제목
    private String boardText; // 게시물 내용
    private String boardDate; // 등록 날짜
    private boolean isQuestionBoard; // 질문 게시물 여부
    private boolean isTipBoard; // 팁 게시물 여부


    public BoardWrite(String title, String text, String date, boolean isQuestionBoard, boolean isTipBoard) {
        this.boardTitle = title;
        this.boardText = text;
        this.boardDate = date;
        this.isQuestionBoard = isQuestionBoard;
        this.isTipBoard = isTipBoard;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardText() {
        return boardText;
    }

    public void setBoardText(String boardText) {
        this.boardText = boardText;
    }

    public String getBoardDate() {
        return boardDate;
    }

    public void setBoardDate(String boardDate) {
        this.boardDate = boardDate;
    }

    public boolean isQuestionBoard() {
        return isQuestionBoard;
    }

    public boolean isTipBoard() {
        return isTipBoard;
    }


}
