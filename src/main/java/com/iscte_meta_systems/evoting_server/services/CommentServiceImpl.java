package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Answer;
import com.iscte_meta_systems.evoting_server.entities.HelpComment;
import com.iscte_meta_systems.evoting_server.entities.User;
import com.iscte_meta_systems.evoting_server.enums.Role;
import com.iscte_meta_systems.evoting_server.repositories.AnswerRepository;
import com.iscte_meta_systems.evoting_server.repositories.HelpCommentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private HelpCommentRepository helpCommentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VoterService voterService;

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

    @Override
    public boolean likeComment(Long id) {
        HelpComment comment = getCommentById(id);
        String voterHash = userService.getCurrentUser().getUsername();
        if (comment.hasLiked(voterHash)) {
            return false;
        }
        comment.addLike(voterHash);
        helpCommentRepository.save(comment);
        return true;
    }

    @Override
    public void deleteComment(Long id) {
        User user = userService.getCurrentUser();
        if (Role.ADMIN != user.getRole()) {
            throw new RuntimeException("Only admins can delete comments.");
        }
        HelpComment comment = getCommentById(id);
        if (comment.getAnswer() != null) {
            answerRepository.delete(comment.getAnswer());
        }
        helpCommentRepository.delete(comment);
    }

    @Override
    public List<HelpComment> getAllComments() {
        return helpCommentRepository.findAll();
    }

    @Override
    public boolean hasUserLiked(Long commentId) {
        HelpComment comment = getCommentById(commentId);
        String voterHash = voterService.getLoggedVoter().getNif().toString();
        return comment.getVoterHashLike().contains(voterHash);
    }

    @PostConstruct
    public void init() {
        if (helpCommentRepository.count() != 0) {
            return;
        }
        HelpComment comment1 = new HelpComment();
        comment1.setComment("Posso votar com 16 anos?");
        helpCommentRepository.save(comment1);

        HelpComment comment2 = new HelpComment();
        comment2.setComment("Como posso alterar o meu voto?");
        helpCommentRepository.save(comment2);

        Answer answer = new Answer();
        answer.setAnswer("O voto, assim que registado, já não pode ser alterado.");
        answer.setComment(comment2);
        answer.setAdminId(1L);

        answerRepository.save(answer);

        comment2.setAnswer(answer);
        helpCommentRepository.save(comment2);

    }
}