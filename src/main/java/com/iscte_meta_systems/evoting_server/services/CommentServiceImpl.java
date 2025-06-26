package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.repositories.AnswerRepository;
import com.iscte_meta_systems.evoting_server.repositories.HelpCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private HelpCommentRepository helpCommentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserService userService;

    @Override
    public HelpComment comment(String comentario) {
        HelpComment helpComment = new HelpComment();
        helpComment.setComment(comentario);
        helpCommentRepository.save(helpComment);
        return helpComment;
    }

    @Override
    public Answer answerComment(String answer, Long id) {
        User user = userService.getCurrentUser();
        if (Role.ADMIN!= user.getRole()) {
            throw new RuntimeException("Only admins can answer comments.");
        }

        HelpComment helpComment = getCommentById(id);

        Answer answerEntity = new Answer();
        answerEntity.setAnswer(answer);
        answerEntity.setComment(helpComment);
        answerEntity.setAdminId(user.getId());

        answerRepository.save(answerEntity);

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
