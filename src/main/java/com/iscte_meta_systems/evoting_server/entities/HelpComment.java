package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class HelpComment {
    @Id
    @GeneratedValue
    private Long id;
    private String comment;

    @OneToOne
    private Answer answer;

    @ElementCollection
    private Set<String> voterHashLike = new HashSet<>();

    public boolean addLike(String voterHash) {
        return voterHashLike.add(voterHash);
    }

    public boolean hasLiked(String voterHash) {
        return voterHashLike.contains(voterHash);
    }

    public int getLikeCount() {
        return voterHashLike.size();
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
