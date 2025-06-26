package com.iscte_meta_systems.evoting_server.model;

public class HelpCommentDTO {
    private Long id;
    private String comment;
    private String answer;
    private int likeCount;

    public HelpCommentDTO() {
    }

    public HelpCommentDTO(Long id, String comment, String answer, int likeCount) {
        this.id = id;
        this.comment = comment;
        this.answer = answer;
        this.likeCount = likeCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
