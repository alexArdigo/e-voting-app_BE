package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;

import java.util.List;

public interface CommentService {
    HelpComment comment(String comentario);

    Answer answerComment(String answer, Long id);

    HelpComment getCommentById(Long id);

    boolean likeComment(Long id, String voterHash);

    List<HelpComment> getAllComments();
}
