package com.iscte_meta_systems.evoting_server.controllers;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public HelpComment comment(@RequestParam("text") String commentText){
        return commentService.comment(commentText);
    }

    @GetMapping("/comment/{id}")
    public HelpComment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/comment/{id}/answer")
    public Answer answerComment(@RequestBody String answer, @PathVariable Long id) {
        return commentService.answerComment(answer, id);
    }
}
