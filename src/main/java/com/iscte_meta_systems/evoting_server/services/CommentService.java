package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;

public interface CommentService {
    HelpComment comment(String comentario);

    Answer answerComment(String answer, Long id);
}
