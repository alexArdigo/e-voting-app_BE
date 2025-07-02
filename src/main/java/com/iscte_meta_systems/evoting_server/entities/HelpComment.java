package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoterHash> likedBy;

    public HelpComment() {
        this.localDateTime = LocalDateTime.now();
    }

    public void addLike(VoterHash voterHash) {
        likedBy.add(voterHash);
    }

    public boolean hasLiked(VoterHash voterHash) {
        return likedBy.contains(voterHash);
    }

    public int getLikeCount() {
        return likedBy.size();
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

    public Set<VoterHash> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<VoterHash> likedBy) {
        this.likedBy = likedBy;
    }
}
