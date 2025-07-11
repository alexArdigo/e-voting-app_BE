package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class HelpComment {
    @Id
    @GeneratedValue
    private Long id;
    private String comment;
    private LocalDateTime localDateTime;

    @OneToOne
    private Answer answer;

    @ManyToMany
    private Set<VoterHash> voterHashLike = new HashSet<>();

    public HelpComment() {
        this.localDateTime = LocalDateTime.now();
    }

    public void addLike(VoterHash voterHash) {
        voterHashLike.add(voterHash);
    }

    public boolean hasLiked(VoterHash voterHash) {
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

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Set<VoterHash> getVoterHashLike() {
        return voterHashLike;
    }

    public void setVoterHashLike(Set<VoterHash> voterHashLike) {
        this.voterHashLike = voterHashLike;
    }

    public void removeLike(VoterHash voterHash) {
        if (voterHashLike.contains(voterHash)) {
            voterHashLike.remove(voterHash);
        } else {
            throw new RuntimeException("User has not liked this comment.");
        }
    }
}
