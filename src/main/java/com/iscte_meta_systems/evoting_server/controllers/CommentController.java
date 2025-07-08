package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.services.CommentService;
import com.iscte_meta_systems.evoting_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("/comment")
    public HelpComment comment(@RequestParam("text") String commentText){
        return commentService.comment(commentText);
    }

    @GetMapping("/comment/{id}")
    public HelpComment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/comment/{id}/answer")
    public Answer answerComment(@RequestParam("text") String answer, @PathVariable Long id) {
        return commentService.answerComment(answer, id);
    }

    @PostMapping("/comment/{id}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long id) {
        System.out.println("Like comment with ID: " + id);
        boolean liked = commentService.likeComment(id);

        if (liked) {
            return ResponseEntity.ok("Comment liked successfully.");
        } else {
            return ResponseEntity.status(400).body("You have already liked this comment.");
        }
    }

    @GetMapping("/comments")
    public List<HelpComment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/comment/{id}/hasLiked")
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable Long id) {
        boolean liked = commentService.hasUserLiked(id);
        return ResponseEntity.ok(liked);
    }

}
