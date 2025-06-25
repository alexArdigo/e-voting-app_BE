package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.repositories.HelpCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private HelpCommentRepository helpCommentRepository;

    @Override
    public HelpComment comment(String comentario) {
        HelpComment helpComment = new HelpComment();
        helpComment.setComment(comentario);
        helpCommentRepository.save(helpComment);
        return helpComment;
    }

    @Override
    public Answer answerComment(String answer, Long id) {
        HelpComment helpComment = getCommentById(id);
        Answer answerEntity = new Answer();
        answerEntity.setAnswer(answer);
        answerEntity.setCommentId(helpComment.getId());

        helpComment.setAnswer(answerEntity);
        helpCommentRepository.save(helpComment);

        return answerEntity;
    }

    @Override
    public HelpComment getCommentById(Long id) {
        return helpCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }
}
